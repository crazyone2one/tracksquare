package io.square.service;

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
}
