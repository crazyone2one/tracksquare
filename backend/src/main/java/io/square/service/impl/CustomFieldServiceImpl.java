package io.square.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.square.common.ResponseResult;
import io.square.controller.request.QueryCustomFieldRequest;
import io.square.entity.CustomField;
import io.square.exception.BizException;
import io.square.mapper.CustomFieldMapper;
import io.square.service.CustomFieldService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomFieldServiceImpl extends ServiceImpl<CustomFieldMapper, CustomField> implements CustomFieldService {

    @Override
    public ResponseResult<CustomField> add(CustomField customField) {
        checkExist(customField);
        customField.setGlobal(false);
        baseMapper.insert(customField);
        return ResponseResult.success(customField);
    }

    @Override
    public ResponseResult<Map<String, Object>> listPageData(QueryCustomFieldRequest request, Long page, Long limit) {
        LambdaQueryWrapper<CustomField> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(request.getProjectId()), CustomField::getProjectId, request.getProjectId());
        wrapper.orderByDesc(CustomField::getUpdateTime);
        Page<CustomField> selectPage = baseMapper.selectPage(new Page<>(page, limit), wrapper);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", selectPage.getTotal());
        result.put("records", selectPage.getRecords());
        return ResponseResult.success(result);
    }

    @Override
    public List<CustomField> getCustomFieldByTemplateId(String id) {
        return null;
    }

    private void checkExist(CustomField customField) {
        if (StringUtils.isNotBlank(customField.getName())) {
            LambdaQueryWrapper<CustomField> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CustomField::getName, customField.getName());
            wrapper.eq(CustomField::getProjectId, customField.getProjectId());
            wrapper.eq(CustomField::getScene, customField.getScene());
            wrapper.ne(StringUtils.isNotBlank(customField.getId()), CustomField::getId, customField.getId());
            if (baseMapper.selectList(wrapper).size() > 0) {
                BizException.throwException("工作空间下已存在该字段" + customField.getName());
            }
        }
    }
}
