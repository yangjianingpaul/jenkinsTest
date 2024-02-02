package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

public interface WmMaterialService extends IService<WmMaterial> {
    /**
     * image upload
     * @param multipartFile
     * @return
     */
    public ResponseResult uploadPicture(MultipartFile multipartFile);


    /**
     * Material list query
     * @param dto
     * @return
     */
    public ResponseResult findList(WmMaterialDto dto);

    /**
     * Delete material
     * @param parseInt
     * @return
     */
    ResponseResult deletePicture(int parseInt);

    /**
     * Photo collection
     * @param id
     * @return
     */
    ResponseResult collectPicture(String id);

    /**
     * cancel collection
     * @param id
     * @return
     */
    ResponseResult cancelCollectPicture(String id);
}