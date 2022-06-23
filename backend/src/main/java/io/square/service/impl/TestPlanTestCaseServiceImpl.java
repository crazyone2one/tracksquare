package io.square.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.square.common.ResponseResult;
import io.square.common.constants.TestPlanTestCaseStatus;
import io.square.controller.request.QueryTestPlanCaseRequest;
import io.square.entity.TestCase;
import io.square.entity.TestPlanTestCase;
import io.square.mapper.TestCaseMapper;
import io.square.mapper.TestPlanTestCaseMapper;
import io.square.service.TestPlanTestCaseService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 测试计划-测试用例关联 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-22
 */
@Service
public class TestPlanTestCaseServiceImpl extends ServiceImpl<TestPlanTestCaseMapper, TestPlanTestCase> implements TestPlanTestCaseService {

    @Resource
    TestCaseMapper testCaseMapper;
    @Override
    public ResponseResult<Map<String, Object>> pageList(QueryTestPlanCaseRequest request, long page, long limit) {
        Map<String, Object> result = new LinkedHashMap<>();
        LambdaQueryWrapper<TestPlanTestCase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestPlanTestCase::getPlanId, request.getPlanId());
        List<TestPlanTestCase> testPlanTestCases = baseMapper.selectList(wrapper);
        List<String> caseIds = testPlanTestCases.stream().map(TestPlanTestCase::getCaseId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(caseIds)) {
            LambdaQueryWrapper<TestCase> testCaseWrapper = new LambdaQueryWrapper<>();
            testCaseWrapper.in(TestCase::getId, caseIds);
            Page<TestCase> casePage = testCaseMapper.selectPage(new Page<>(page, limit), testCaseWrapper);
            result.put("total", casePage.getTotal());
            result.put("records", casePage.getRecords());
            return ResponseResult.success(result);
        }
        result.put("total", 0);
        result.put("records", new ArrayList<>());
        return ResponseResult.success(result);
    }

    @Override
    public ResponseResult<String> editTestCase(TestPlanTestCase request) {
        if (StringUtils.equals(TestPlanTestCaseStatus.Prepare.name(), request.getStatus())) {
            request.setStatus(TestPlanTestCaseStatus.Underway.name());
        }
        request.setUpdateTime(LocalDate.now());
        request.setRemark(null);
        baseMapper.updateById(request);
        return ResponseResult.success();
    }


}
