package io.square.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.square.common.ResponseResult;
import io.square.entity.Project;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-14
 */
public interface ProjectService extends IService<Project> {

    ResponseResult<Map<String, Object>> getProjectList(Project project, long page, long limit);

    ResponseResult<List<Project>> getUserProject(Project project);

    ResponseResult<Project> addProject(Project project);

    ResponseResult<Project> updateProject(Project project);

    ResponseResult<String> deleteProject(String projectId);

    ResponseResult<List<Project>> getProjectList();

    Map<String, Project> getProjectMap(List<String> ids);

    Map<String, String> getProjectNameMap(List<String> ids);

    void deleteByWorkspace(String workspaceId);

    List<Project> getProjectByWrapper(LambdaQueryWrapper<Project> wrapper);
}
