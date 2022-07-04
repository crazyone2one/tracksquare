package io.square.controller.request;

import lombok.Data;

import java.util.List;

/**
 * @author by 11's papa on 2022/6/27 0027
 * @version 1.0.0
 */
@Data
public class QueryCustomFieldRequest extends BaseQueryRequest {
    private String templateId;
    private String workspaceId;
    private String scene;
    private List<String> templateContainIds;
}
