package io.square.service;

/**
 * @author by 11's papa on 2022/7/12 0012
 * @version 1.0.0
 */
public interface CheckPermissionService {
    void checkProjectBelongToWorkspace(String projectId, String workspaceId);
}
