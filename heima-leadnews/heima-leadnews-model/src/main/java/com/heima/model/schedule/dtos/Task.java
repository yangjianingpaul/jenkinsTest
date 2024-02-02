package com.heima.model.schedule.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class Task implements Serializable {

    /**
     * task id
     */
    private Long taskId;
    /**
     * type
     */
    private Integer taskType;

    /**
     * priority
     */
    private Integer priority;

    /**
     * execution id
     */
    private long executeTime;

    /**
     * task parameter
     */
    private byte[] parameters;
    
}