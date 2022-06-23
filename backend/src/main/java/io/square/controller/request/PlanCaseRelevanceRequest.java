package io.square.controller.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by 11's papa on 2022/6/22 0022
 * @version 1.0.0
 */
@Data
public class PlanCaseRelevanceRequest {
    /**
     * 测试计划ID
     */
    private String planId;
    private String userId;
    private String executor;

    private List<String> ids;

    /**
     * 当选择关联全部用例时把加载条件送到后台，从后台查询
     */
    private QueryTestCaseRequest request;

    /**
     * 具体要关联的用例
     */
    private List<String> testCaseIds = new ArrayList<>();

    private Boolean checked;
}
