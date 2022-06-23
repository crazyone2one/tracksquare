package io.square.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.square.common.ResponseResult;
import io.square.controller.request.QueryTestCaseRequest;
import io.square.entity.TestCase;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-15
 */
public interface TestCaseService extends IService<TestCase> {

    /**
     * 分页查询
     *
     * @param request
     * @param page
     * @param limit
     * @return io.square.common.ResponseResult<java.util.Map < java.lang.String, java.lang.Object>>
     */


    ResponseResult<Map<String, Object>> listTestCase(QueryTestCaseRequest request, long page, long limit);

    ResponseResult<TestCase> addTestCase(TestCase request);

    ResponseResult<TestCase> getTestCase(String testCaseId);

    ResponseResult<TestCase> saveCase(TestCase request, List<MultipartFile> fileList);
    ResponseResult<TestCase> saveCase(TestCase request);

    ResponseResult<Map<String, Object>> getTestCaseRelateList(long page, long limit, QueryTestCaseRequest request);
}
