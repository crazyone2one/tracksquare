package io.square.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.square.common.ResponseResult;
import io.square.common.constants.TemplateConstants;
import io.square.controller.request.BaseQueryRequest;
import io.square.entity.TestCaseTemplate;
import io.square.exception.BizException;
import io.square.mapper.TestCaseTemplateMapper;
import io.square.service.CustomFieldTemplateService;
import io.square.service.TestCaseTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;
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
public class TestCaseTemplateServiceImpl extends ServiceImpl<TestCaseTemplateMapper, TestCaseTemplate> implements TestCaseTemplateService {

    @Resource
    CustomFieldTemplateService customFieldTemplateService;
    @Override
    public ResponseResult<Map<String, Object>> pageList(BaseQueryRequest request, Long page, Long limit) {
        LambdaQueryWrapper<TestCaseTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(request.getProjectId()), TestCaseTemplate::getProjectId, request.getProjectId());
        wrapper.orderByDesc(TestCaseTemplate::getUpdateTime);
        Page<TestCaseTemplate> selectPage = baseMapper.selectPage(new Page<>(page, limit), wrapper);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", selectPage.getTotal());
        result.put("records", selectPage.getRecords());
        return ResponseResult.success(result);
    }

    @Override
    public ResponseResult<String> add(TestCaseTemplate request) {
        checkExist(request);
        request.setGlobal(false);
        if (Objects.isNull(request.getSystem())) {
            request.setSystem(false);
        }
        baseMapper.insert(request);
        customFieldTemplateService.create(request.getCustomFields(),request.getId(), TemplateConstants.FieldTemplateScene.TEST_CASE.name());
        return ResponseResult.success();
    }

    @Override
    public ResponseResult<String> delete(String id) {
        return null;
    }

    private void checkExist(TestCaseTemplate request) {
        if (Objects.nonNull(request.getName())) {
            LambdaQueryWrapper<TestCaseTemplate> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TestCaseTemplate::getName, request.getName());
            wrapper.ne(StringUtils.isNotBlank(request.getProjectId()), TestCaseTemplate::getProjectId, request.getProjectId());
            if (baseMapper.selectCount(wrapper) > 0) {
                BizException.throwException("工作空间下已存在该模板" + request.getName());
            }
        }
    }

}
