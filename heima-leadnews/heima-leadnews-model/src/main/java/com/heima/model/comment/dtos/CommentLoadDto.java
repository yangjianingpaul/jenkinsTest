package com.heima.model.comment.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class CommentLoadDto {
    Long articleId;

    Integer index;

    Date minDate;
}
