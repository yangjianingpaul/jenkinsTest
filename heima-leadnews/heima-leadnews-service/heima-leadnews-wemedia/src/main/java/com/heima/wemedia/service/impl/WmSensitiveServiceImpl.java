package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.SensitiveDto;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import com.heima.wemedia.service.WmSensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WmSensitiveServiceImpl extends ServiceImpl<WmSensitiveMapper, WmSensitive> implements WmSensitiveService {

    @Autowired
    private WmSensitiveMapper wmSensitiveMapper;

    /**
     * get the sensitive list
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult sensitiveList(SensitiveDto dto) {
        dto.checkParam();
        LambdaQueryWrapper<WmSensitive> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (dto.getName().equals("")) {
            lambdaQueryWrapper.ge(WmSensitive::getId, 1);
        } else {
            int count = count(lambdaQueryWrapper.like(WmSensitive::getSensitives, dto.getName()));
            if (count == 0) {
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
            }
        }

        lambdaQueryWrapper.orderByDesc(WmSensitive::getCreatedTime);
        IPage page = new Page(dto.getPage(), dto.getSize());
        page = page(page, lambdaQueryWrapper);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    /**
     * create a sensitive word
     *
     * @param wmSensitive
     * @return
     */
    @Override
    public ResponseResult sensitiveSave(WmSensitive wmSensitive) {
        if (wmSensitive.getSensitives() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        int count = count(new LambdaQueryWrapper<WmSensitive>().eq(WmSensitive::getSensitives, wmSensitive.getSensitives()));
        if (count > 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST);
        }

        wmSensitive.setCreatedTime(new Date());
        boolean result = save(wmSensitive);
        return ResponseResult.okResult(result);
    }

    /**
     * delete the sensitive word
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult sensitiveDelete(Integer id) {
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        boolean result = removeById(id);
        return ResponseResult.okResult(result);
    }

    /**
     * update the sensitive word
     *
     * @param wmSensitive
     * @return
     */
    @Override
    public ResponseResult sensitiveUpdate(WmSensitive wmSensitive) {
        if (wmSensitive.getSensitives() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        LambdaQueryWrapper<WmSensitive> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        int count = count(lambdaQueryWrapper.eq(WmSensitive::getSensitives, wmSensitive.getSensitives()));
        if (count > 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST);
        }

        wmSensitive.setCreatedTime(new Date());
        boolean result = updateById(wmSensitive);
        return ResponseResult.okResult(result);
    }
}
