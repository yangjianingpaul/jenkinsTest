package com.heima.model.comment.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class ReplyLoadDto {
    Integer commentId;

    Date minDate;
}
