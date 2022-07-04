package io.square.service;

import io.square.common.ResponseResult;
import io.square.controller.request.BaseQueryRequest;
import io.square.entity.TestCaseTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-27
 */
public interface TestCaseTemplateService extends IService<TestCaseTemplate> {

    /**
     * 分页查询
     *
     * @param request 参数
     * @param page    当前页数
     * @param limit   每页展示数量
     * @return io.square.common.ResponseResult<java.util.Map < java.lang.String, java.lang.Object>>
     */
    ResponseResult<Map<String, Object>> pageList(BaseQueryRequest request, Long page, Long limit);

    /**
     * 添加模板
     *
     * @param request 请求参数
     * @return io.square.common.ResponseResult<java.lang.String>
     */

    ResponseResult<String> add(TestCaseTemplate request);

    ResponseResult<String> delete(String id);
}
