package io.square.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.square.common.ResponseResult;
import io.square.common.constants.TestCaseReviewStatus;
import io.square.controller.request.QueryTestCaseRequest;
import io.square.entity.*;
import io.square.mapper.ProjectMapper;
import io.square.mapper.TestCaseFollowMapper;
import io.square.mapper.TestCaseMapper;
import io.square.mapper.TestCaseNodeMapper;
import io.square.service.TestCaseService;
import io.square.service.UserService;
import io.square.utils.DateUtils;
import io.square.utils.ServiceUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TestCaseServiceImpl extends ServiceImpl<TestCaseMapper, TestCase> implements TestCaseService {

    @Resource
    ProjectMapper projectMapper;
    @Resource
    TestCaseNodeMapper testCaseNodeMapper;
    @Resource
    TestCaseFollowMapper testCaseFollowMapper;
    @Resource
    UserService userService;

    @Override
    public ResponseResult<Map<String, Object>> listTestCase(QueryTestCaseRequest request, long page, long limit) {
        //initRequest(request, true);
        LambdaQueryWrapper<TestCase> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(request.getName()),TestCase::getName, request.getName());
        IPage<TestCase> iPage = baseMapper.selectPage(new Page<>(page, limit), wrapper);
        buildUserInfo(iPage.getRecords());
        buildProjectInfoWithoutProject(iPage.getRecords());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", iPage.getTotal());
        result.put("records", iPage.getRecords());
        return ResponseResult.success(result);
    }

    private void buildProjectInfoWithoutProject(List<TestCase> testCases) {
        testCases.forEach(c->{
            c.setProjectName(projectMapper.selectById(c.getProjectId()).getName());
        });
    }

    private void buildUserInfo(List<TestCase> testCases) {
        List<String> userIds = new ArrayList();
        userIds.addAll(testCases.stream().map(TestCase::getCreateUser).collect(Collectors.toList()));
        userIds.addAll(testCases.stream().map(TestCase::getDeleteUserId).collect(Collectors.toList()));
        userIds.addAll(testCases.stream().map(TestCase::getMaintainer).collect(Collectors.toList()));
        if (CollectionUtils.isNotEmpty(userIds)) {
            Map<String, User> userMap = userService.queryNameByIds(userIds);
            HashMap<String, String> nameMap = new HashMap<>();
            userMap.forEach((k, v) -> {
                nameMap.put(k, v.getName());
            });
            testCases.forEach(caseResult->{
                caseResult.setCreateName(nameMap.get(caseResult.getCreateUser()));
                caseResult.setDeleteUserId(nameMap.get(caseResult.getDeleteUserId()));
                caseResult.setMaintainerName(nameMap.get(caseResult.getMaintainer()));
            });
        }
    }

    @Override
    public ResponseResult<TestCase> addTestCase(TestCase request) {
        request.setId(request.getId());
        checkTestCaseExist(request);
        //checkTestCustomNum(request);
        request.setNum(getNextNum(request.getProjectId()));
        if (StringUtils.isBlank(request.getCustomNum())) {
            request.setCustomNum(request.getNum().toString());
        }
        request.setReviewStatus(TestCaseReviewStatus.Prepare.name());
        request.setStatus(TestCaseReviewStatus.Prepare.name());
        request.setDemandId(request.getDemandId());
        request.setDemandName(request.getDemandName());
        setNode(request);
        request.setOrder(ServiceUtils.getNextOrder(request.getProjectId(), baseMapper::getLastOrder));
        if (StringUtils.isAllBlank(request.getRefId(), request.getVersionId())) {
            request.setRefId(request.getId());
        } else if (StringUtils.isBlank(request.getRefId()) && StringUtils.isNotBlank(request.getVersionId())) {
            //从版本选择直接创建
            request.setRefId(request.getId());
        }
        request.setLatest(true);
        baseMapper.insert(request);
        saveFollows(request.getId(), request.getFollows());
        return ResponseResult.success(request);
    }

    @Override
    public ResponseResult<TestCase> getTestCase(String testCaseId) {
        TestCase testCase = baseMapper.selectById(testCaseId);
        testCase.setProjectName(projectMapper.selectById(testCase.getProjectId()).getName());
        return ResponseResult.success(testCase);
    }

    @Override
    public ResponseResult<TestCase> saveCase(TestCase request, List<MultipartFile> fileList) {
        return addTestCase(request);
    }

    @Override
    public ResponseResult<TestCase> saveCase(TestCase request) {
        baseMapper.updateById(request);
        return ResponseResult.success(request);
    }

    @Override
    public ResponseResult<Map<String, Object>> getTestCaseRelateList(long page, long limit, QueryTestCaseRequest request) {
        LambdaQueryWrapper<TestCase> wrapper = new LambdaQueryWrapper<>();
        wrapper.notInSql(TestCase::getId, "select case_id from test_plan_test_case where plan_id ='" + request.getPlanId() + "'");
        IPage<TestCase> iPage = baseMapper.getTestCaseByNotInPlan(new Page<>(page, limit), wrapper);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", iPage.getTotal());
        result.put("records", iPage.getRecords());
        return ResponseResult.success(result);
    }

    private void saveFollows(String caseId, List<String> follows) {
        testCaseFollowMapper.delByCaseId(caseId);
        if (CollectionUtils.isNotEmpty(follows)) {
            follows.forEach(f -> {
                TestCaseFollow follow = new TestCaseFollow();
                follow.setCaseId(caseId);
                follow.setFollowId(f);
                testCaseFollowMapper.insert(follow);
            });
        }
    }

    private void setNode(TestCase testCase) {
        if (StringUtils.isEmpty(testCase.getNodeId()) || "default-module".equals(testCase.getNodeId())) {
            LambdaQueryWrapper<TestCaseNode> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TestCaseNode::getProjectId, testCase.getProjectId()).eq(TestCaseNode::getName, "未规划用例");
            List<TestCaseNode> nodes = testCaseNodeMapper.selectList(wrapper);
            if (CollectionUtils.isNotEmpty(nodes)) {
                testCase.setNodeId(nodes.get(0).getId());
                testCase.setNodePath("/" + nodes.get(0).getName());
            }
        }
    }

    private void checkTestCustomNum(TestCase testCase) {
        if (StringUtils.isNotBlank(testCase.getCustomNum())) {
            String projectId = testCase.getProjectId();
            Project project = projectMapper.selectById(projectId);
        }
    }

    private int getNextNum(String projectId) {
        LambdaQueryWrapper<TestCase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestCase::getProjectId, projectId).orderByDesc(TestCase::getNum).last("limit 1");
        TestCase testCase = baseMapper.getMaxNumByProjectId(wrapper);
        if (testCase == null || testCase.getNum() == null) {
            return 100001;
        } else {
            return Optional.of(testCase.getNum() + 1).orElse(100001);
        }
    }

    private TestCase checkTestCaseExist(TestCase testCase) {
        if (Objects.nonNull(testCase)) {
            String nodePath = testCase.getNodePath();
            if (!nodePath.startsWith("/")) {
                nodePath = "/" + nodePath;
            }
            if (nodePath.endsWith("/")) {
                nodePath = nodePath.substring(0, nodePath.length() - 1);
            }
            LambdaQueryWrapper<TestCase> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TestCase::getNodePath, nodePath)
                    .eq(TestCase::getName, testCase.getName())
                    .eq(TestCase::getProjectId, testCase.getProjectId())
                    .eq(TestCase::getType, testCase.getType())
                    .ne(TestCase::getStatus, "Trash");
            if (StringUtils.isNotBlank(testCase.getPriority())) {
                wrapper.eq(TestCase::getPriority, testCase.getPriority());
            }
            if (StringUtils.isNotBlank(testCase.getTestId())) {
                wrapper.eq(TestCase::getTestId, testCase.getTestId());
            }
            if (StringUtils.isNotBlank(testCase.getId())) {
                wrapper.ne(TestCase::getId, testCase.getId());
            }
            List<TestCase> caseList = baseMapper.selectList(wrapper);
            if (CollectionUtils.isNotEmpty(caseList)) {
                String caseRemark = testCase.getRemark() == null ? "" : testCase.getRemark();
                String caseSteps = testCase.getSteps() == null ? "" : testCase.getSteps();
                String casePrerequisite = testCase.getPrerequisite() == null ? "" : testCase.getPrerequisite();
                for (TestCase tc : caseList) {
                    String steps = tc.getSteps() == null ? "" : tc.getSteps();
                    String remark = tc.getRemark() == null ? "" : tc.getRemark();
                    String prerequisite = tc.getPrerequisite() == null ? "" : tc.getPrerequisite();
                    if (StringUtils.equals(steps, caseSteps) && StringUtils.equals(remark, caseRemark) && StringUtils.equals(prerequisite, casePrerequisite)) {
                        //MSException.throwException(Translator.get("test_case_already_exists"));
                        return tc;
                    }
                }
            }
        }
        return null;
    }

    private void initRequest(QueryTestCaseRequest request, boolean checkThisWeekData) {
        Map<String, Date> weekFirstTimeAndLastTime = DateUtils.getWeedFirstTimeAndLastTime(new Date());
        Date weekFirstTime = weekFirstTimeAndLastTime.get("firstTime");
        if (request.isSelectThisWeedData()) {
            if (weekFirstTime != null) {
                request.setCreateTime(weekFirstTime.getTime());
            }
        }
        if (request.isSelectThisWeedRelevanceData()) {
            if (weekFirstTime != null) {
                request.setRelevanceCreateTime(weekFirstTime.getTime());
            }
        }
    }


}
