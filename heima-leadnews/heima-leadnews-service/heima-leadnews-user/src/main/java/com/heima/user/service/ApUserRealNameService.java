package com.heima.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.pojos.ApUserRealname;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.AuthDto;

public interface ApUserRealNameService extends IService<ApUserRealname> {
    ResponseResult authList(AuthDto dto);

    ResponseResult authFail(AuthDto dto);

    ResponseResult authPass(AuthDto dto);
}
