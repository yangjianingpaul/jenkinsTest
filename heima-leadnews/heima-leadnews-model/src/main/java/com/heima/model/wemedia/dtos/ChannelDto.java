package com.heima.model.wemedia.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChannelDto extends PageRequestDto {
    /**
     * Channel name
     */
    @ApiModelProperty(value = "频道名称")
    private String name;

    @ApiModelProperty(value = "账号状态")
    private Boolean status;
}
