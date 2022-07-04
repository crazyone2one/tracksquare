package io.square.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import io.square.common.ResponseResult;
import io.square.common.constants.TestPlanStatus;
import io.square.common.constants.TestPlanTestCaseStatus;
import io.square.controller.request.PlanCaseRelevanceRequest;
import io.square.dto.CountMapDTO;
import io.square.dto.ParamsDTO;
import io.square.entity.*;
import io.square.exception.BizException;
import io.square.mapper.*;
import io.square.service.TestPlanFollowService;
import io.square.service.TestPlanPrincipalService;
import io.square.service.TestPlanService;
import io.square.utils.CommonUtils;
import io.square.utils.JacksonUtils;
import io.square.utils.ServiceUtils;
import io.square.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TestPlanServiceImpl extends ServiceImpl<TestPlanMapper, TestPlan> implements TestPlanService {
    @Resource
    CustomFieldMapper customFieldMapper;
    @Resource
    SessionUtils sessionUtils;
    @Resource
    TestPlanPrincipalService testPlanPrincipalService;
    @Resource
    TestPlanFollowService testPlanFollowService;
    @Resource
    TestPlanTestCaseMapper testPlanTestCaseMapper;
    @Resource
    TestCaseMapper testCaseMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    ProjectMapper projectMapper;


    @Override
    public ResponseResult<JsonNode> getStageOption(String projectId) {
        QueryWrapper<CustomField> customFieldQueryWrapper = new QueryWrapper<>();
        customFieldQueryWrapper.lambda().eq(CustomField::getProjectId, projectId)
                .eq(CustomField::getScene, "PLAN").eq(CustomField::getName, "测试阶段");
        List<CustomField> customFields = customFieldMapper.selectList(customFieldQueryWrapper);
        if (CollectionUtils.isEmpty(customFields)) {
            customFieldQueryWrapper.clear();
            customFieldQueryWrapper.lambda().eq(CustomField::getGlobal, true).eq(CustomField::getScene, "PLAN").eq(CustomField::getName, "测试阶段");
            customFields = customFieldMapper.selectList(customFieldQueryWrapper);
        }
        JsonNode jsonNode = JacksonUtils.stringToJsonObject(customFields.get(0).getOptions());
        return ResponseResult.success(jsonNode);
    }

    @Override
    public ResponseResult<TestPlan> addTestPlan(TestPlan testPlan) {
        testPlan.setId(UUID.randomUUID().toString());
        if (CollectionUtils.isNotEmpty(getTestPlanByName(testPlan))) {
            BizException.throwException("测试计划名称已存在");
        }
        testPlan.setStatus(TestPlanStatus.Prepare.name());
        testPlan.setCreateTime(LocalDate.now());
        testPlan.setUpdateTime(LocalDate.now());
        if (StringUtils.isBlank(testPlan.getProjectId())) {
            testPlan.setProjectId(sessionUtils.getCurrentProjectId());
        }
        String planId = testPlan.getId();
        List<String> principals = testPlan.getPrincipals();
        if (CollectionUtils.isNotEmpty(principals)) {
            principals.forEach(p -> {
                TestPlanPrincipal planPrincipal = new TestPlanPrincipal();
                planPrincipal.setPrincipalId(p);
                planPrincipal.setTestPlanId(planId);
                testPlanPrincipalService.insertTestPlanPrincipal(planPrincipal);
            });
        }
        List<String> follows = testPlan.getFollows();
        if (CollectionUtils.isNotEmpty(follows)) {
            follows.forEach(f -> {
                TestPlanFollow follow = new TestPlanFollow();
                follow.setFollowId(f);
                follow.setTestPlanId(planId);
                testPlanFollowService.insertTestPlanFollow(follow);
            });
        }
        testPlan.setWorkspaceId("");
        baseMapper.insert(testPlan);
        return ResponseResult.success(testPlan);
    }

    @Override
    public ResponseResult<Map<String, Object>> pageList(TestPlan request, Long page, Long limit) {
        LambdaQueryWrapper<TestPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(request.getProjectId()), TestPlan::getProjectId, request.getProjectId());
        IPage<TestPlan> iPage = baseMapper.selectPage(new Page<>(page, limit), wrapper);
        Map<String, Object> result = new LinkedHashMap<>();

        List<TestPlan> testPlans = iPage.getRecords();
        Set<String> ids = testPlans.stream().map(TestPlan::getId).collect(Collectors.toSet());
        Map<String, ParamsDTO> planTestCaseCountMap = testPlanTestCaseMapper.testPlanTestCaseCount(ids);
        //Map<String, ParamsDTO> planApiCaseMap = testPlanTestCaseMapper.testPlanApiCaseCount(ids);
        //Map<String, ParamsDTO> planApiScenarioMap = testPlanTestCaseMapper.testPlanApiCaseCount(ids);
        //Map<String, ParamsDTO> planLoadCaseMap = testPlanTestCaseMapper.testPlanLoadCaseCount(ids);
        //ArrayList<String> idList = new ArrayList<>(ids);
        iPage.getRecords().forEach(item -> {
            item.setCreateUser(userMapper.selectById(item.getCreator()).getName());
            item.setProjectName(projectMapper.selectById(item.getProjectId()).getName());
            item.setTestPlanTestCaseCount(planTestCaseCountMap.get(item.getId()) == null ? 0 : Integer.parseInt(planTestCaseCountMap.get(item.getId()).getValue() == null ? "0" : planTestCaseCountMap.get(item.getId()).getValue()));
            calcTestPlanRate(item);
        });
        result.put("total", iPage.getTotal());
        result.put("records", iPage.getRecords());

        return ResponseResult.success(result);
    }

    public void calcTestPlanRate(TestPlan testPlan) {
        testPlan.setTested(0);
        testPlan.setPassed(0);
        testPlan.setTotal(0);
        List<CountMapDTO> statusCountMap = testPlanTestCaseMapper.getExecResultMapByPlanId(testPlan.getId());
        Integer functionalExecTotal = 0;
        for (CountMapDTO item : statusCountMap) {
            functionalExecTotal += item.getValue();
            if (!StringUtils.equals(item.getKey(), TestPlanTestCaseStatus.Prepare.name())
                    && !StringUtils.equals(item.getKey(), TestPlanTestCaseStatus.Underway.name())) {
                testPlan.setTested(testPlan.getTested() + item.getValue());
                if (StringUtils.equals(item.getKey(), TestPlanTestCaseStatus.Pass.name())) {
                    testPlan.setPassed(testPlan.getPassed() + item.getValue());
                }
            }
        }
        // TODO: 2022/6/23 0023 api
        testPlan.setTotal(functionalExecTotal);
        testPlan.setPassRate(CommonUtils.getPercentWithDecimal(testPlan.getTested() == 0 ? 0 : testPlan.getPassed() * 1.0 / testPlan.getTotal()));
        testPlan.setTestRate(CommonUtils.getPercentWithDecimal(testPlan.getTotal() == 0 ? 0 : testPlan.getTested() * 1.0 / testPlan.getTotal()));
    }

    @Override
    public ResponseResult<List<TestPlan>> listTestAllPlan(TestPlan request) {
        String projectId = request.getProjectId();
        if (StringUtils.isBlank(projectId)) {
            return ResponseResult.success(new ArrayList<>());
        }
        LambdaQueryWrapper<TestPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestPlan::getProjectId, projectId);
        return ResponseResult.success(baseMapper.selectList(wrapper));
    }

    @Override
    public ResponseResult<TestPlan> getTestPlan(String testPlanId) {
        return ResponseResult.success(baseMapper.selectById(testPlanId));
    }

    @Override
    public ResponseResult<String> testPlanRelevance(PlanCaseRelevanceRequest request) {
        LinkedHashMap<String, String> userMap;
        LambdaQueryWrapper<TestCase> testCaseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        testCaseLambdaQueryWrapper.in(TestCase::getId, request.getIds());
        List<TestCase> testCaseList = testCaseMapper.selectList(testCaseLambdaQueryWrapper);
        userMap = testCaseList.stream()
                .collect(LinkedHashMap::new, (m, v) -> m.put(v.getId(), v.getMaintainer()), LinkedHashMap::putAll);

        List<String> testCaseIds = new ArrayList<>(userMap.keySet());
        if (testCaseIds.isEmpty()) {
            return ResponseResult.success("");
        }
        Collections.reverse(testCaseIds);
        long nextOrder = ServiceUtils.getNextOrder(request.getPlanId(), testPlanTestCaseMapper::getLastOrder);
        for (String caseId : testCaseIds) {
            TestPlanTestCase build = TestPlanTestCase.builder().createUser(request.getUserId())
                    .executor(Optional.ofNullable(userMap.get(caseId)).orElse(request.getUserId()))
                    .caseId(caseId).planId(request.getPlanId()).status(TestPlanStatus.Prepare.name())
                    .isDel(false).order(nextOrder).build();
            nextOrder += ServiceUtils.ORDER_STEP;
            testPlanTestCaseMapper.insert(build);
        }
        // 同步添加关联的接口和测试用例
        //caseTestRelevance(request, testCaseIds);

        TestPlan testPlan = baseMapper.selectById(request.getPlanId());
        if (StringUtils.equals(testPlan.getStatus(), TestPlanStatus.Prepare.name())
                || StringUtils.equals(testPlan.getStatus(), TestPlanStatus.Completed.name())) {
            testPlan.setStatus(TestPlanStatus.Underway.name());
            testPlan.setActualStartTime(LocalDate.now());
            baseMapper.updateById(testPlan);
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult<String> editTestPlanStatus(String planId) {
        TestPlan testPlan = new TestPlan();
        testPlan.setId(planId);
        String status = calcTestPlanStatus(planId);
        testPlan.setStatus(status);
        baseMapper.updateById(testPlan);
        return ResponseResult.success();
    }

    @Override
    public List<String> getPlanIdByProjectId(String projectId) {
        LambdaQueryWrapper<TestPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestPlan::getProjectId, projectId);
        List<TestPlan> testPlans = baseMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(testPlans)) {
            return new ArrayList<>();
        }
        return testPlans.stream().map(TestPlan::getId).collect(Collectors.toList());
    }

    @Override
    public int deleteTestPlan(String planId) {
        testPlanPrincipalService.deleteTestPlanPrincipalByPlanId(planId);
        testPlanFollowService.deleteTestPlanFollowByPlanId(planId);
        deleteTestCaseByPlanId(planId);
        return baseMapper.deleteById(planId);
    }

    private void deleteTestCaseByPlanId(String planId) {
        LambdaQueryWrapper<TestPlanTestCase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestPlanTestCase::getPlanId, planId);
        testPlanTestCaseMapper.delete(wrapper);
    }

    private String calcTestPlanStatus(String planId) {
        // test-plan-functional-case status
        List<String> funcStatusList = testPlanTestCaseMapper.getStatusByPlanId(planId);
        for (String funcStatus : funcStatusList) {
            if (StringUtils.equals(funcStatus, TestPlanTestCaseStatus.Prepare.name())
                    || StringUtils.equals(funcStatus, TestPlanTestCaseStatus.Underway.name())) {
                return TestPlanStatus.Underway.name();
            }
        }
        // todo test-plan-api-case status
        // todo test-plan-scenario-case status
        // todo test-plan-load-case status

        return TestPlanStatus.Completed.name();
    }

    private void caseTestRelevance(PlanCaseRelevanceRequest request, List<String> testCaseIds) {

    }

    private List<TestPlan> getTestPlanByName(TestPlan testPlan) {
        LambdaQueryWrapper<TestPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestPlan::getName, testPlan.getName()).eq(TestPlan::getProjectId, testPlan.getProjectId());
        return baseMapper.selectList(wrapper);
    }
}
