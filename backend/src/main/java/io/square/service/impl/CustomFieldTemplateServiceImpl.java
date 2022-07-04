package io.square.service.impl;

import io.square.common.ResponseResult;
import io.square.entity.CustomFieldTemplate;
import io.square.mapper.CustomFieldTemplateMapper;
import io.square.service.CustomFieldTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-27
 */
@Service
public class CustomFieldTemplateServiceImpl extends ServiceImpl<CustomFieldTemplateMapper, CustomFieldTemplate> implements CustomFieldTemplateService {

    @Override
    public void create(List<CustomFieldTemplate> customFields, String templateId, String scene) {
        if (CollectionUtils.isNotEmpty(customFields)) {
            customFields.forEach(item -> {
                CustomFieldTemplate build = CustomFieldTemplate.builder().templateId(templateId).scene(scene).build();
                if (Objects.isNull(item.getRequired())) {
                    build.setRequired(false);
                }
                baseMapper.insert(build);
            });
        }
    }

    @Override
    public ResponseResult<List<CustomFieldTemplate>> pageList(CustomFieldTemplate request) {
        return ResponseResult.success(baseMapper.listAllByTemplateId(request));
    }
}
