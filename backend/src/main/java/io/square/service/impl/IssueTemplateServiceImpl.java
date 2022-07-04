package io.square.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.square.common.ResponseResult;
import io.square.entity.CustomField;
import io.square.entity.IssueTemplate;
import io.square.entity.Project;
import io.square.exception.BizException;
import io.square.mapper.IssueTemplateMapper;
import io.square.mapper.ProjectMapper;
import io.square.service.CustomFieldService;
import io.square.service.IssueTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IssueTemplateServiceImpl extends ServiceImpl<IssueTemplateMapper, IssueTemplate> implements IssueTemplateService {
    @Resource
    ProjectMapper projectMapper;
    @Resource
    CustomFieldService customFieldService;
    @Override
    public ResponseResult<IssueTemplate> getTemplate(String projectId) {
        Project project = projectMapper.selectById(projectId);
        String issueTemplateId = project.getIssueTemplateId();
        IssueTemplate issueTemplate;
        if (StringUtils.isNotBlank(issueTemplateId)) {
            issueTemplate = baseMapper.selectById(issueTemplateId);
            if (Objects.isNull(issueTemplate)) {
                issueTemplate = getDefaultTemplate(project.getWorkspaceId());
            }

        } else {
            issueTemplate = getDefaultTemplate(project.getWorkspaceId());
        }
        assert issueTemplate != null;
        if (!project.getPlatform().equals(issueTemplate.getPlatform())) {
            BizException.throwException("请在项目中配置缺陷模板");
        }
        List<CustomField> result = customFieldService.getCustomFieldByTemplateId(issueTemplate.getId());
        issueTemplate.setCustomFields(result);
        return ResponseResult.success(issueTemplate);
    }

    private IssueTemplate getDefaultTemplate(String workspaceId) {
        return null;
    }
}
