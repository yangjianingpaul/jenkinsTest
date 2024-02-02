package com.heima.comment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.comment.dtos.CommentLikeDto;
import com.heima.model.comment.dtos.CommentLoadDto;
import com.heima.model.comment.dtos.CommentSaveDto;
import com.heima.model.comment.pojos.ApComment;
import com.heima.model.common.dtos.ResponseResult;

/**
 * get article comment's list
 */
public interface CommentLoadService extends IService<ApComment> {
    ResponseResult loadList(CommentLoadDto dto);

    ResponseResult saveComment(CommentSaveDto dto);

    ResponseResult commentLike(CommentLikeDto dto);

    void updateReply(Integer commentId);
}
