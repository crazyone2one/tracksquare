package io.square.controller.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author by 11's papa on 2022/6/16 0016
 * @version 1.0.0
 */
@Getter
@Setter
public class QueryMemberRequest {
    private String name;
    private String workspaceId;
    private String projectId;
}
