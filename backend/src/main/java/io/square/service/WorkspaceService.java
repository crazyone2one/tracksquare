package io.square.service;

import io.square.common.ResponseResult;
import io.square.dto.WorkspaceResource;
import io.square.entity.Workspace;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-27
 */
public interface WorkspaceService extends IService<Workspace> {

    ResponseResult<Workspace> saveWorkspace(Workspace workspace);

    ResponseResult<Workspace> addWorkspaceByAdmin(Workspace workspace);

    ResponseResult<Map<String, Object>> getAllWorkspaceList(Workspace workspace, long page, long limit);

    ResponseResult<List<Workspace>> getWorkspaceList();

    ResponseResult<WorkspaceResource> listResource(String groupId, String type);

    ResponseResult<List<Workspace>> getWorkspaceListByUserId(String userId);
}
