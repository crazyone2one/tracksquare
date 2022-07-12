package io.square.controller.request;

import lombok.Data;

import java.util.List;

/**
 * @author by 11's papa on 2022/7/12 0012
 * @version 1.0.0
 */
@Data
public class AddMemberRequest {
    private String workspaceId;
    private List<String> userIds;
    private List<String> roleIds;
    private List<String> groupIds;
    private String projectId;
}
