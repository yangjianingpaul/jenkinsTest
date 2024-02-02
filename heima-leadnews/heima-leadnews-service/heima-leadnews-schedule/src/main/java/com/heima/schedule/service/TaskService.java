package com.heima.schedule.service;

import com.heima.model.schedule.dtos.Task;

public interface TaskService {


    /**
     * delay task
     * @param task
     * @return
     */
    public long addTask(Task task);

    /**
     * cancel the task
     * @param taskId
     * @return
     */
    public boolean cancelTask(long taskId);

    /**
     * pull tasks by type and priority
     * @param type
     * @param priority
     * @return
     */
    public Task poll(int type, int priority);
}
