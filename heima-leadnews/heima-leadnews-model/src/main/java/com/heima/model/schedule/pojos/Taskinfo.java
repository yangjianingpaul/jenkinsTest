package com.heima.model.schedule.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("taskinfo")
public class Taskinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * task id
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
     * taskType
     */
    @TableField("task_type")
    private Integer taskType;


}
