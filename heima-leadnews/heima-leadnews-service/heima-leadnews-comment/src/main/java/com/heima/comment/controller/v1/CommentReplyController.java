package com.heima.comment.controller.v1;

import com.heima.comment.service.CommentReplyService;
import com.heima.model.comment.dtos.CommentReplyDto;
import com.heima.model.comment.dtos.ReplyLikeDto;
import com.heima.model.comment.dtos.ReplyLoadDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/comment_repay")
public class CommentReplyController {

    @Autowired
    private CommentReplyService commentReplyService;

    @PostMapping("/save")
    public ResponseResult commentReplySave(@RequestBody CommentReplyDto dto) {
        return commentReplyService.commentReplySave(dto);
    }

    @PostMapping("/load")
    public ResponseResult commentReplyLoad(@RequestBody ReplyLoadDto dto) {
        return commentReplyService.commentReplyLoad(dto);
    }

    @PostMapping("/like")
    public ResponseResult commentReplyLike(@RequestBody ReplyLikeDto dto) {
        return commentReplyService.commentReplyLike(dto);
    }
}
