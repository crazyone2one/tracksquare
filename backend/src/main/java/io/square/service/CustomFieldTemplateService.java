package io.square.service;

import io.square.common.ResponseResult;
import io.square.entity.CustomFieldTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-27
 */
public interface CustomFieldTemplateService extends IService<CustomFieldTemplate> {

    void create(List<CustomFieldTemplate> customFields, String templateId, String scene);

    ResponseResult<List<CustomFieldTemplate>> pageList(CustomFieldTemplate request);
}
