package io.square.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.square.common.ResponseResult;
import io.square.common.constants.TestPlanTestCaseStatus;
import io.square.controller.request.QueryTestPlanCaseRequest;
import io.square.dto.TestPlanCaseDTO;
import io.square.entity.TestPlanTestCase;
import io.square.mapper.TestPlanTestCaseMapper;
import io.square.service.ProjectService;
import io.square.service.TestPlanTestCaseService;
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
    ProjectService projectService;

    @Override
    public ResponseResult<Map<String, Object>> pageList(QueryTestPlanCaseRequest request, long page, long limit) {
        Map<String, Object> result = new LinkedHashMap<>();
        Page<Object> objectPage = new Page<>(page, limit);
        IPage<TestPlanCaseDTO> list = baseMapper.list(request, objectPage);
        if (list.getRecords().isEmpty()) {
            result.put("total", 0);
            result.put("records", new ArrayList<>());
            return ResponseResult.success(result);
        }
        List<String> projectIds = list.getRecords().stream().map(TestPlanCaseDTO::getProjectId)
                .distinct()
                .collect(Collectors.toList());
        Map<String, String> projectMap = projectService.getProjectNameMap(projectIds);
        list.getRecords().forEach(item -> {
            item.setProjectName(projectMap.get(item.getProjectId()));
            item.setCustomNum(item.getNum().toString());
        });
        //  // TODO: 2022/6/24 0024  设置版本信息
        result.put("total", list.getTotal());
        result.put("records", list.getRecords());
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
