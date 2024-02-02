package com.heima.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.admin.pojos.ApUserRealname;
import com.heima.model.user.pojos.ApUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApUserMapper extends BaseMapper<ApUser> {
}
