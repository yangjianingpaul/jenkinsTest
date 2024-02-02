package com.heima.model.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskTypeEnum {

    NEWS_SCAN_TIME(1001, 1,"articles are reviewed regularly"),
    REMOTE_ERROR(1002, 2,"if the third party api call fails try again");
    private final int taskType; //corresponding to specific businesses
    private final int priority; //different levels of business
    private final String desc; //descriptive information
}