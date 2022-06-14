package io.square.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.square.common.ResponseResult;
import io.square.entity.Project;
import io.square.entity.ProjectVersion;
import io.square.exception.BizException;
import io.square.mapper.ProjectMapper;
import io.square.mapper.ProjectVersionMapper;
import io.square.service.ProjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-14
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {
    @Resource
    ProjectVersionMapper projectVersionMapper;
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
        wrapper.eq(Project::getName, project.getName());
        if (baseMapper.selectCount(wrapper) > 0) {
            BizException.throwException("项目名称已存在");
        }
        Project build = Project.builder().name(project.getName()).description(project.getDescription()).createUser(project.getCreateUser())
                .createTime(LocalDate.now()).updateTime(LocalDate.now()).systemId(genSystemId()).build();
        baseMapper.insert(build);
        addProjectVersion(build);
        return ResponseResult.success(build);
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
        baseMapper.deleteById(projectId);
        return ResponseResult.success();
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
            wrapper.eq(Project::getName, project.getName()).ne(Project::getId,project.getId());
            if (baseMapper.selectCount(wrapper) > 0) {
                BizException.throwException("项目名称已存在");
            }
        }
    }
}
