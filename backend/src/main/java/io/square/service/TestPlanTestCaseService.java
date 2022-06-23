package io.square.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.square.common.ResponseResult;
import io.square.controller.request.QueryTestPlanCaseRequest;
import io.square.entity.TestPlanTestCase;

import java.util.Map;

/**
 * <p>
 * 测试计划-测试用例关联 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-22
 */
public interface TestPlanTestCaseService extends IService<TestPlanTestCase> {

    /**
     * 分页查询
     *
     * @param request 参数
     * @param page    当前页数
     * @param limit   每页展示数量
     * @return io.square.common.ResponseResult<java.util.Map < java.lang.String, java.lang.Object>>
     */
    ResponseResult<Map<String, Object>> pageList(QueryTestPlanCaseRequest request, long page, long limit);

    /**
     * 测试计划关联测试用例执行结果保存
     *
     * @param request 参数
     * @return io.square.common.ResponseResult<java.lang.String>
     */
    ResponseResult<String> editTestCase(TestPlanTestCase request);
}
