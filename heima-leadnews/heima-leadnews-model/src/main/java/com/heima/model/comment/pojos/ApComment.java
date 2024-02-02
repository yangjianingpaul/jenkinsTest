package com.heima.model.comment.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ap_comment")
public class ApComment {
    @TableId(value = "id", type = IdType.AUTO)
    Integer id;

    @TableField("article_id")
    Long articleId;

    @TableField("content")
    String content;

    @TableField("created_time")
    Date createdTime;

    @TableField("author_name")
    String authorName;

    @TableField("operation")
    Integer operation;

    @TableField("likes")
    Integer likes;

    @TableField("reply")
    Integer reply;
}
