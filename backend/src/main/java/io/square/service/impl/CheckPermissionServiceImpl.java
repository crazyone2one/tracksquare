package io.square.service.impl;

import io.square.entity.Project;
import io.square.exception.BizException;
import io.square.mapper.ProjectMapper;
import io.square.service.CheckPermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author by 11's papa on 2022/7/12 0012
 * @version 1.0.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CheckPermissionServiceImpl implements CheckPermissionService {
    @Resource
    ProjectMapper projectMapper;
    @Override
    public void checkProjectBelongToWorkspace(String projectId, String workspaceId) {
        Project project = projectMapper.selectById(projectId);
        if (project == null || !StringUtils.equals(project.getWorkspaceId(), workspaceId)) {
            BizException.throwException("当前用户没有操作此项目的权限");
        }
    }
}
