package com.heima.model.comment.dtos;

import lombok.Data;

@Data
public class CommentSaveDto {
    Long articleId;

    String content;
}
