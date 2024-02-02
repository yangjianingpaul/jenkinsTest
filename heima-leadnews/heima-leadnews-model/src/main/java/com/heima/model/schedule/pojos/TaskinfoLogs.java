package com.heima.model.schedule.pojos;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author yang
 */
@Data
@TableName("taskinfo_logs")
public class TaskinfoLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * taskID
     */
    @TableId(type = IdType.ID_WORKER)
    private Long taskId;

    /**
     * execution time
     */
    @TableField("execute_time")
    private Date executeTime;

    /**
     * parameter
     */
    @TableField("parameters")
    private byte[] parameters;

    /**
     * priority
     */
    @TableField("priority")
    private Integer priority;

    /**
     * task type
     */
    @TableField("task_type")
    private Integer taskType;

    /**
     * version number with optimistic locks
     */
    @Version
    private Integer version;

    /**
     * status 0=init 1=EXECUTED 2=CANCELLED
     */
    @TableField("status")
    private Integer status;


}
