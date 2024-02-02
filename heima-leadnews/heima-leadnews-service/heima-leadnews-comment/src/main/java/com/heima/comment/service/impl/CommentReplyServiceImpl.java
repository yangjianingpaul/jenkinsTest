package com.heima.comment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.apis.user.IUserClient;
import com.heima.comment.mapper.CommentReplyMapper;
import com.heima.comment.service.CommentLoadService;
import com.heima.comment.service.CommentReplyService;
import com.heima.model.comment.dtos.CommentReplyDto;
import com.heima.model.comment.dtos.ReplyLikeDto;
import com.heima.model.comment.dtos.ReplyLoadDto;
import com.heima.model.comment.pojos.ApReply;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.pojos.ApUser;
import com.heima.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CommentReplyServiceImpl extends ServiceImpl<CommentReplyMapper, ApReply> implements CommentReplyService {

    @Autowired
    private CommentLoadService commentLoadService;

    @Autowired
    private IUserClient userClient;
    @Override
    public ResponseResult commentReplySave(CommentReplyDto dto) {
        ApReply apReply = new ApReply();
        BeanUtils.copyProperties(dto, apReply);
        apReply.setOperation(0);

        ApUser user = AppThreadLocalUtil.getUser();
        String username = userClient.getUserName(user.getId()).getData().toString();

        apReply.setAuthorName(username);
        apReply.setCreatedTime(new Date());
        apReply.setOperation(1);
        boolean result = save(apReply);
        commentLoadService.updateReply(dto.getCommentId());
        return ResponseResult.okResult(result);
    }

    @Override
    public ResponseResult commentReplyLoad(ReplyLoadDto dto) {
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        LambdaQueryWrapper<ApReply> wrapper = new LambdaQueryWrapper<ApReply>().eq(ApReply::getCommentId, dto.getCommentId()).orderByDesc(ApReply::getCreatedTime);
        List<ApReply> apReplyList = list(wrapper);
        if (apReplyList.size() == 0 || apReplyList.get(0).getCreatedTime().compareTo(dto.getMinDate()) >= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.okResult(apReplyList);
    }

    @Override
    public ResponseResult commentReplyLike(ReplyLikeDto dto) {
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApReply apReply = getById(dto.getCommentRepayId());
        apReply.setOperation(dto.getOperation());

        if (dto.getOperation() == 0) {
            if (apReply.getLikes() == null) {
                apReply.setLikes(1);
            } else {
                apReply.setLikes(apReply.getLikes() + 1);
            }
        } else {
            apReply.setLikes(apReply.getLikes() - 1);
        }

        boolean result = updateById(apReply);
        return ResponseResult.okResult(result);
    }
}
