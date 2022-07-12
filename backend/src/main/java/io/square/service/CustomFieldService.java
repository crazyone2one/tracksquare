package io.square.service;

import io.square.common.ResponseResult;
import io.square.controller.request.QueryCustomFieldRequest;
import io.square.entity.CustomField;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-20
 */
public interface CustomFieldService extends IService<CustomField> {

    ResponseResult<CustomField> add(CustomField customField);

    ResponseResult<Map<String, Object>> listPageData(QueryCustomFieldRequest request, Long page, Long limit);

    List<CustomField> getCustomFieldByTemplateId(String id);

    ResponseResult<Map<String, Object>> listRelate(QueryCustomFieldRequest request, Long page, Long limit);

    ResponseResult<List<CustomField>> list(QueryCustomFieldRequest request);

    ResponseResult<List<CustomField>> getDefaultField(QueryCustomFieldRequest request);
}
