package com.heima.comment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.comment.dtos.CommentReplyDto;
import com.heima.model.comment.dtos.ReplyLikeDto;
import com.heima.model.comment.dtos.ReplyLoadDto;
import com.heima.model.comment.pojos.ApReply;
import com.heima.model.common.dtos.ResponseResult;

public interface CommentReplyService extends IService<ApReply> {
    ResponseResult commentReplySave(CommentReplyDto dto);

    ResponseResult commentReplyLoad(ReplyLoadDto dto);

    ResponseResult commentReplyLike(ReplyLikeDto dto);
}
