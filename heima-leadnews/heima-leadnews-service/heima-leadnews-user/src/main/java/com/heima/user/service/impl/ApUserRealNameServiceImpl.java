package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.UserConstants;
import com.heima.model.admin.pojos.ApUserRealname;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.AuthDto;
import com.heima.user.mapper.ApUserRealNameMapper;
import com.heima.user.service.ApUserRealNameService;
import org.springframework.stereotype.Service;

@Service
public class ApUserRealNameServiceImpl extends ServiceImpl<ApUserRealNameMapper, ApUserRealname> implements ApUserRealNameService {

    /**
     * Get audit list
     * @param dto
     * @return
     */
    @Override
    public ResponseResult authList(AuthDto dto) {
        dto.checkParam();
        LambdaQueryWrapper<ApUserRealname> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (dto.getStatus() == null) {
            lambdaQueryWrapper.ge(ApUserRealname::getId, 1);
        } else {
            lambdaQueryWrapper.eq(ApUserRealname::getStatus, dto.getStatus());
        }

        IPage page = new Page(dto.getPage(), dto.getSize());
        page = page(page, lambdaQueryWrapper);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    /**
     * Review and reject
     * @param dto
     * @return
     */
    @Override
    public ResponseResult authFail(AuthDto dto) {
        dto.checkParam();
        if (dto.getId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        ApUserRealname userRealName = getById(dto.getId());
        userRealName.setStatus(UserConstants.FAIL_AUTH);
        userRealName.setReason(dto.getMsg());
        boolean result = updateById(userRealName);
        return ResponseResult.okResult(result);
    }

    /**
     * pass the audit
     * @param dto
     * @return
     */
    @Override
    public ResponseResult authPass(AuthDto dto) {
        dto.checkParam();
        if (dto.getId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        ApUserRealname userRealName = getById(dto.getId());
        userRealName.setStatus(UserConstants.PASS_AUTH);
        boolean result = updateById(userRealName);
        return ResponseResult.okResult(result);
    }
}
