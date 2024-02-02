package com.heima.search.pojos;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * app user search information table
 * </p>
 * @author itheima
 */
@Data
@Document("ap_user_search")
public class ApUserSearch implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * primary key
     */
    private String id;

    /**
     * user id
     */
    private Integer userId;

    /**
     * keywords
     */
    private String keyword;

    /**
     * creation time
     */
    private Date createdTime;

}