package io.square.controller.request;

import lombok.Data;

/**
 * @author by 11's papa on 2022/6/22 0022
 * @version 1.0.0
 */
@Data
public class QueryNodeRequest extends BaseQueryRequest {
    private String id;
    private String testPlanId;
    private String reviewId;
    private String excludeId;
    private String moduleId;
    private String name;
    private String userId;
    private String planId;
    private boolean recent = false;
    private boolean isSelectThisWeedData;
    private long createTime = 0;
    private String executeStatus;
    private boolean notInTestPlan;
    private String versionId;
    private String refId;

    //操作人
    private String operator;
    //操作时间
    private Long operationTime;
    /**
     * 是否需要查询环境字段
     */
    private boolean selectEnvironment = false;
}
