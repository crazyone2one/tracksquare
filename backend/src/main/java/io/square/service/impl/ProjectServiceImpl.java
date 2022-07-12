package io.square.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.square.common.ResponseResult;
import io.square.common.constants.UserGroupConstants;
import io.square.entity.Project;
import io.square.entity.ProjectVersion;
import io.square.entity.UserGroup;
import io.square.exception.BizException;
import io.square.mapper.ProjectMapper;
import io.square.mapper.ProjectVersionMapper;
import io.square.mapper.UserGroupMapper;
import io.square.mapper.UserMapper;
import io.square.service.ProjectService;
import io.square.service.TestCaseService;
import io.square.service.TestPlanService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-14
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {
    @Resource
    ProjectVersionMapper projectVersionMapper;
    @Resource
    UserGroupMapper userGroupMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    TestPlanService testPlanService;
    @Resource
    TestCaseService testCaseService;

    @Override
    public ResponseResult<Map<String, Object>> getProjectList(Project project, long page, long limit) {
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(project.getName()), Project::getName, project.getName());
        IPage<Project> projectPage = baseMapper.selectPage(new Page<>(page, limit), wrapper);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", projectPage.getTotal());
        result.put("records", projectPage.getRecords());
        return ResponseResult.success(result);
    }

    @Override
    public ResponseResult<List<Project>> getUserProject(Project project) {
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(project.getName()), Project::getName, project.getName());
        return ResponseResult.success(baseMapper.selectList(wrapper));
    }

    @Override
    public ResponseResult<Project> addProject(Project project) {
        if (StringUtils.isBlank(project.getName())) {
            BizException.throwException("项目名称为空");
        }
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Project::getName, project.getName()).eq(Project::getWorkspaceId, project.getWorkspaceId());
        if (baseMapper.selectCount(wrapper) > 0) {
            BizException.throwException("项目名称已存在");
        }
        project.setSystemId(genSystemId());
        baseMapper.insert(project);
        // 创建项目为当前用户添加用户组
        UserGroup build = UserGroup.builder().groupId(UserGroupConstants.PROJECT_ADMIN).userId(project.getCreateUser()).sourceId(project.getId()).build();
        userGroupMapper.insert(build);
        // 创建新项目检查当前用户 last_project_id
        userMapper.updateLastProjectIdIfNull(project.getId(), project.getCreateUser());
        addProjectVersion(project);
        return ResponseResult.success(project);
    }

    @Override
    public ResponseResult<Project> updateProject(Project project) {
        project.setUpdateTime(LocalDate.now());
        checkProjectExist(project);
        baseMapper.updateById(project);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult<String> deleteProject(String projectId) {
        // TODO: 2022/6/14 0014 删除项目下相关的数据资源
        // 删除项目下 测试跟踪 相关
        deleteTrackResourceByProjectId(projectId);
        baseMapper.deleteById(projectId);
        return ResponseResult.success();
    }

    private void deleteTrackResourceByProjectId(String projectId) {
        // 删除相关测试计划
        List<String> testPlanIds = testPlanService.getPlanIdByProjectId(projectId);
        if (CollectionUtils.isNotEmpty(testPlanIds)) {
            testPlanIds.forEach(testPlanId -> {
                testPlanService.deleteTestPlan(testPlanId);
            });
        }
        // 删除测试用例
        testCaseService.deleteTestCaseByProjectId(projectId);
    }

    @Override
    public ResponseResult<List<Project>> getProjectList() {
        return ResponseResult.success(baseMapper.selectList(null));
    }

    @Override
    public Map<String, Project> getProjectMap(List<String> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            return baseMapper.queryNameByIds(ids);
        }
        return new HashMap<>();
    }

    @Override
    public Map<String, String> getProjectNameMap(List<String> ids) {
        Map<String, Project> projectMap = getProjectMap(ids);
        HashMap<String, String> nameMap = new HashMap<>();
        projectMap.forEach((k, v) -> {
            nameMap.put(k, v.getName());
        });
        return nameMap;
    }

    @Override
    public void deleteByWorkspace(String workspaceId) {
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Project::getWorkspaceId, workspaceId);
        List<Project> projects = baseMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(projects)) {
            List<String> projectIdList = projects.stream().map(Project::getId).collect(Collectors.toList());
            projectIdList.forEach(id -> baseMapper.deleteById(id));
        }
    }

    @Override
    public List<Project> getProjectByWrapper(LambdaQueryWrapper<Project> wrapper) {
        return baseMapper.selectList(wrapper);
    }

    public void addProjectVersion(Project project) {
        ProjectVersion projectVersion = ProjectVersion.builder().name("v1.0.0").projectId(project.getId())
                .createTime(LocalDateTime.now()).startTime(LocalDateTime.now()).publishTime(LocalDateTime.now())
                .latest(true).status("open").build();
        String name = projectVersion.getName();
        LambdaQueryWrapper<ProjectVersion> projectVersionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        projectVersionLambdaQueryWrapper.eq(ProjectVersion::getProjectId, project.getId())
                .eq(ProjectVersion::getName, name);
        if (projectVersionMapper.selectCount(projectVersionLambdaQueryWrapper) > 0) {
            BizException.throwException("当前版本已经存在");
        }
        projectVersion.setCreateUser(project.getCreateUser());
        projectVersionMapper.insert(projectVersion);
    }

    private String genSystemId() {
        String maxSystemIdInDb = baseMapper.getMaxSystemId();
        String systemId = "10001";
        if (StringUtils.isNotEmpty(maxSystemIdInDb)) {
            systemId = String.valueOf(Long.parseLong(maxSystemIdInDb) + 1);
        }
        return systemId;
    }

    private void checkProjectExist(Project project) {
        if (StringUtils.isNotBlank(project.getName())) {
            LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Project::getName, project.getName()).ne(Project::getId, project.getId());
            if (baseMapper.selectCount(wrapper) > 0) {
                BizException.throwException("项目名称已存在");
            }
        }
    }
}
