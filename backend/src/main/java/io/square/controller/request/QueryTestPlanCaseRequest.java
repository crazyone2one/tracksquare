package io.square.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author by 11's papa on 2022/6/22 0022
 * @version 1.0.0
 */
@Getter
@Setter
public class QueryTestPlanCaseRequest extends BaseQueryRequest {
    private List<String> nodePaths;

    private List<String> planIds;

    private List<String> projectIds;

    private String workspaceId;

    private String status;

    private String node;

    private String method;

    private String nodeId;

    private String planId;

    private String executor;

    private String id;

    private Boolean isCustomNum = false;

    private String projectName;

    private Map<String, Object> combine;
}
