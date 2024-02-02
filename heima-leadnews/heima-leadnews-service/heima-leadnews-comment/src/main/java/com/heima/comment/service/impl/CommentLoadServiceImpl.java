package com.heima.comment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.apis.user.IUserClient;
import com.heima.comment.mapper.CommentMapper;
import com.heima.comment.service.CommentLoadService;
import com.heima.model.comment.dtos.CommentLikeDto;
import com.heima.model.comment.dtos.CommentLoadDto;
import com.heima.model.comment.dtos.CommentSaveDto;
import com.heima.model.comment.pojos.ApComment;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.pojos.ApUser;
import com.heima.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CommentLoadServiceImpl extends ServiceImpl<CommentMapper, ApComment> implements CommentLoadService {

    /**
     * get the list of comment
     * @param dto
     * @return
     */
    @Override
    public ResponseResult loadList(CommentLoadDto dto) {
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        LambdaQueryWrapper<ApComment> wrapper = new LambdaQueryWrapper<ApComment>()
                .eq(ApComment::getArticleId, dto.getArticleId())
                .orderByDesc(ApComment::getCreatedTime);
        List<ApComment> commentList = list(wrapper);
        if (dto.getIndex() > 1 || commentList.get(0).getCreatedTime().compareTo(dto.getMinDate()) >= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.COMMENT_NOT_EXIST);
        }
        return ResponseResult.okResult(commentList);
    }

    @Autowired
    private IUserClient userClient;

    /**
     * create comment
     * @param dto
     * @return
     */
    @Override
    public ResponseResult saveComment(CommentSaveDto dto) {
        if (dto.getArticleId().equals(null) || dto.getContent().equals(null)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }

        ApUser user = AppThreadLocalUtil.getUser();
        String userName = (String) userClient.getUserName(user.getId()).getData();

        ApComment comment = new ApComment();
        comment.setArticleId(dto.getArticleId());
        comment.setContent(dto.getContent());
        comment.setCreatedTime(new Date());
        comment.setLikes(0);
        comment.setReply(0);
        comment.setAuthorName(userName);
        comment.setOperation(1);

        boolean result = save(comment);
        return ResponseResult.okResult(result);
    }

    /**
     * comment like
     * @param dto
     * @return
     */
    @Override
    public ResponseResult commentLike(CommentLikeDto dto) {
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApComment comment = getById(dto.getCommentId());
        comment.setOperation(dto.getOperation());

        if (dto.getOperation() == 0) {
            if (comment.getLikes() == null) {
                comment.setLikes(1);
            } else {
                comment.setLikes(comment.getLikes() + 1);
            }
        } else {
            comment.setLikes(comment.getLikes() - 1);
        }


        boolean result = updateById(comment);
        return ResponseResult.okResult(result);
    }

    @Override
    public void updateReply(Integer commentId) {
        ApComment comment = getById(commentId);
        if (comment.getReply() == null) {
            comment.setReply(1);
        } else {
            comment.setReply( comment.getReply() + 1);
        }
        updateById(comment);
    }
}
