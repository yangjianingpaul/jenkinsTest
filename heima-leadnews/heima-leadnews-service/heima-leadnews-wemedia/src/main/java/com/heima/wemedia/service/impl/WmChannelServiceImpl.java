package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.WemediaConstants;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.ChannelDto;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.service.WmChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@Slf4j
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements WmChannelService {


    /**
     * Query all channels
     *
     * @return
     */
    @Override
    public ResponseResult findAll() {
        return ResponseResult.okResult(list());
    }

    /**
     * channel management
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult channelList(ChannelDto dto) {
//        1。check the parameters
        dto.checkParam();
//        2。paginated queries
        IPage page = new Page(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmChannel> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (dto.getName().equals("")) {
            lambdaQueryWrapper.ge(WmChannel::getId, 1);
        } else {
//        follow the user query
            lambdaQueryWrapper.eq(WmChannel::getName, dto.getName());
        }
//        in reverse chronological order
        lambdaQueryWrapper.orderByDesc(WmChannel::getCreatedTime);
        page = page(page, lambdaQueryWrapper);
//        3。the results are returned
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    /**
     * add a new channel
     * @param wmChannel
     * @return
     */
    @Override
    public ResponseResult saveChannel(WmChannel wmChannel) {
        if (wmChannel == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        int count = count(new LambdaQueryWrapper<WmChannel>().eq(WmChannel::getName, wmChannel.getName()));
        if (count > 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST);
        }

        wmChannel.setCreatedTime(new Date());
        wmChannel.setIsDefault(WemediaConstants.WM_CHANNEL_IS_DEFAULT);
        save(wmChannel);
        return ResponseResult.okResult(wmChannel);
    }

    /**
     * update the channel
     * @param wmChannel
     * @return
     */
    @Override
    public ResponseResult updateChannel(WmChannel wmChannel) {
        if (wmChannel.getId() == null && wmChannel.getName() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        WmChannel oldChannel = getById(wmChannel.getId());
        if (wmChannel.getDescription() == null || wmChannel.getDescription().equals("")) {
            wmChannel.setDescription(oldChannel.getDescription());
        }

        if (wmChannel.getOrd() == null) {
            wmChannel.setOrd(oldChannel.getOrd());
        }
        wmChannel.setCreatedTime(new Date());
        wmChannel.setIsDefault(WemediaConstants.WM_CHANNEL_IS_DEFAULT);
        boolean result = updateById(wmChannel);
        return ResponseResult.okResult(result);
    }

    /**
     * delete the channel
     * @param channelId
     * @return
     */
    @Override
    public ResponseResult deleteChannel(int channelId) {
        WmChannel wmChannel = getById(channelId);
        if (wmChannel.getStatus()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CHANNEL_NOT_FORBID);
        }

        boolean result = removeById(channelId);
        return ResponseResult.okResult(result);
    }
}