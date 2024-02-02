package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.WemediaConstants;
import com.heima.file.service.FileStorageService;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import com.heima.utils.thread.WmThreadLocalUtil;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;


@Slf4j
@Service
@Transactional
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * image upload
     * @param multipartFile
     * @return
     */
    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
//        1。Check parameter
        if (multipartFile == null || multipartFile.getSize() == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
//        2。Upload pictures to minio
        String fileName = UUID.randomUUID().toString().replace("-", "");
        String originalFilename = multipartFile.getOriginalFilename();
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileId = null;
        try {
            fileId = fileStorageService.uploadImgFile("", fileName + postfix, multipartFile.getInputStream());
            log.info("Upload pictures to minio，filed:{}", fileId);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("WmMaterialServiceImp-Failed to upload file");
        }
//        3.Save to the database
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(WmThreadLocalUtil.getUser().getId());
        wmMaterial.setUrl(fileId);
        wmMaterial.setIsCollection((short)0);
        wmMaterial.setType((short)0);
        wmMaterial.setCreatedTime(new Date());
        save(wmMaterial);
//        4。return result
        return ResponseResult.okResult(wmMaterial);
    }

    /**
     *
     * Material list query
     * @param dto
     * @return
     */
    @Override
    public ResponseResult findList(WmMaterialDto dto) {
//        1。Check parameter
        dto.checkParam();
//        2。paging query
        IPage page = new Page(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        Collect or not
        if (dto.getIsCollection() != null && dto.getIsCollection() == 1) {
            lambdaQueryWrapper.eq(WmMaterial::getIsCollection, dto.getIsCollection());
        }
//        Query by user
        lambdaQueryWrapper.eq(WmMaterial::getUserId, WmThreadLocalUtil.getUser().getId());
//        In reverse chronological order
        lambdaQueryWrapper.orderByDesc(WmMaterial::getCreatedTime);
        page = page(page, lambdaQueryWrapper);
//        3。Result return
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),
                dto.getSize(),
                (int)page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    /**
     * Delete material
     * @param id
     * @return
     */
    @Override
    public ResponseResult deletePicture(int id) {
        Integer count = wmNewsMaterialMapper.selectCount(Wrappers.<WmNewsMaterial>lambdaQuery()
                .eq(WmNewsMaterial::getMaterialId, id));
        if (count != 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_IMAGE_WAS_USED, "The image is cited in the article");
        }

        String url = getById(id).getUrl();
        fileStorageService.delete(url);
        boolean result = removeById(id);
        return ResponseResult.okResult(result);
    }

    /**
     * Photo collection
     * @param id
     * @return
     */
    @Override
    public ResponseResult collectPicture(String id) {
        WmMaterial wmMaterial = getById(id);
        wmMaterial.setIsCollection(WemediaConstants.COLLECT_MATERIAL);
        boolean result = updateById(wmMaterial);
        return ResponseResult.okResult(result);
    }

    /**
     * cancel collection
     * @param id
     * @return
     */
    @Override
    public ResponseResult cancelCollectPicture(String id) {
        WmMaterial wmMaterial = getById(id);
        wmMaterial.setIsCollection(WemediaConstants.CANCEL_COLLECT_MATERIAL);
        boolean result = updateById(wmMaterial);
        return ResponseResult.okResult(result);
    }
}
