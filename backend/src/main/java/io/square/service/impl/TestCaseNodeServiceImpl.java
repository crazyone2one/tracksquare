package io.square.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.square.common.ResponseResult;
import io.square.common.constants.TestCaseConstants;
import io.square.controller.request.QueryNodeRequest;
import io.square.controller.request.QueryTestCaseRequest;
import io.square.entity.Project;
import io.square.entity.TestCase;
import io.square.entity.TestCaseNode;
import io.square.entity.TestPlan;
import io.square.exception.BizException;
import io.square.mapper.ProjectMapper;
import io.square.mapper.TestCaseMapper;
import io.square.mapper.TestCaseNodeMapper;
import io.square.mapper.TestPlanMapper;
import io.square.service.TestCaseNodeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;

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
public class TestCaseNodeServiceImpl extends ServiceImpl<TestCaseNodeMapper, TestCaseNode> implements TestCaseNodeService {
    @Resource
    TestCaseMapper testCaseMapper;
    @Resource
    TestPlanMapper testPlanMapper;
    @Resource
    ProjectMapper projectMapper;

    @Override
    public ResponseResult<List<TestCaseNode>> getNodeTreeByProjectId(String projectId) {
        QueryTestCaseRequest request = new QueryTestCaseRequest();
        List<TestCaseNode> testCaseNodes = getNodeTreeByProjectId(projectId, request);
        return ResponseResult.success(testCaseNodes);
    }

    private List<TestCaseNode> getNodeTreeByProjectId(String projectId, QueryTestCaseRequest request) {
        getDefaultNode(projectId);
        LambdaQueryWrapper<TestCaseNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestCaseNode::getProjectId, projectId).orderByAsc(TestCaseNode::getPos);
        List<TestCaseNode> testCaseNodes = baseMapper.selectList(wrapper);

        request.setProjectId(projectId);


        List<String> allModuleIdList = new ArrayList<>();
        testCaseNodes.forEach(node -> {
            List<String> moduleIds = new ArrayList<>();
            nodeList(testCaseNodes, node.getId(), moduleIds);
            moduleIds.add(node.getId());
            for (String moduleId : moduleIds) {
                if (!allModuleIdList.contains(moduleId)) {
                    allModuleIdList.add(moduleId);
                }
            }
        });
        request.setModuleIds(allModuleIdList);

        LambdaQueryWrapper<TestCase> caseWrapper = new LambdaQueryWrapper<>();
        caseWrapper.eq(TestCase::getProjectId, projectId).in(TestCase::getNodeId, allModuleIdList).groupBy(TestCase::getNodeId);
        List<Map<String, Object>> moduleCountList = testCaseMapper.moduleCountByCollection(caseWrapper);
        Map<String, Long> moduleCountMap = parseModuleCountList(moduleCountList);
        testCaseNodes.forEach(node -> {
            List<String> moduleIds = new ArrayList<>();
            nodeList(testCaseNodes, node.getId(), moduleIds);
            moduleIds.add(node.getId());
            int countNum = 0;
            for (String moduleId : moduleIds) {
                if (moduleCountMap.containsKey(moduleId)) {
                    countNum += moduleCountMap.get(moduleId);
                }
            }
            node.setCaseNum(countNum);
        });
        return getNodeTrees(testCaseNodes);
    }
    private List<TestCaseNode> getNodeTrees(List<TestCaseNode> nodes) {
        List<TestCaseNode> nodeTreeList = new ArrayList<>();
        Map<Integer, List<TestCaseNode>> nodeLevelMap = new HashMap<>();
        nodes.forEach(node -> {
            Integer level = node.getLevel();
            if (nodeLevelMap.containsKey(level)) {
                nodeLevelMap.get(level).add(node);
            } else {
                List<TestCaseNode> testCaseNodes = new ArrayList<>();
                testCaseNodes.add(node);
                nodeLevelMap.put(node.getLevel(), testCaseNodes);
            }
        });
        List<TestCaseNode> rootNodes = Optional.ofNullable(nodeLevelMap.get(1)).orElse(new ArrayList<>());
        rootNodes.forEach(rootNode -> {
            nodeTreeList.add(buildNodeTree(nodeLevelMap, rootNode));
        });
        return nodeTreeList;
    }

    private TestCaseNode buildNodeTree(Map<Integer, List<TestCaseNode>> nodeLevelMap, TestCaseNode rootNode) {
        rootNode.setLabel(rootNode.getName());
        List<TestCaseNode> lowerNodes = nodeLevelMap.get(rootNode.getLevel() + 1);if (lowerNodes == null) {
            return rootNode;
        }
        List<TestCaseNode> children = Optional.ofNullable(rootNode.getChildren()).orElse(new ArrayList<>());
        lowerNodes.forEach(node -> {
            if (node.getParentId() != null && node.getParentId().equals(rootNode.getId())) {
                children.add(buildNodeTree(nodeLevelMap, node));
                rootNode.setChildren(children);
            }
        });
        return rootNode;
    }

    @Override
    public TestCaseNode getDefaultNode(String projectId) {
        LambdaQueryWrapper<TestCaseNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestCaseNode::getProjectId, projectId).eq(TestCaseNode::getName, "未规划用例").isNull(TestCaseNode::getParentId);
        List<TestCaseNode> list = baseMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            TestCaseNode build = TestCaseNode.builder().name("未规划用例").projectId(projectId)
                    .pos(1.0).level(1).createTime(LocalDate.now()).updateTime(LocalDate.now()).build();
            baseMapper.insert(build);
            build.setCaseNum(0);
            return build;
        } else {
            return list.get(0);
        }
    }

    @Override
    public ResponseResult<String> addNode(TestCaseNode node) {
        validateNode(node);
        TestCaseNode build = TestCaseNode.builder().name(node.getName()).projectId(node.getProjectId()).parentId(node.getParentId()).level(node.getLevel())
                .createTime(LocalDate.now()).updateTime(LocalDate.now()).build();
        double pos = getNextLevelPos(node.getProjectId(), node.getLevel(), node.getParentId());
        build.setPos(pos);
        baseMapper.insert(build);
        return ResponseResult.success(build.getId());
    }

    @Override
    public ResponseResult<TestCaseNode> editNode(TestCaseNode node) {
        checkTestCaseNodeExist(node);
        if (CollectionUtils.isNotEmpty(node.getNodeIds())) {
            LambdaQueryWrapper<TestCase> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(TestCase::getNodeId, node.getNodeIds());
            List<TestCase> testCases = testCaseMapper.getForNodeEdit(queryWrapper);
            testCases.forEach(testCase -> {
                StringBuilder path = new StringBuilder(testCase.getNodePath());
                List<String> pathLists = Arrays.asList(path.toString().split("/"));
                pathLists.set(node.getLevel(), node.getName());
                path.delete(0, path.length());
                for (int i = 1; i < pathLists.size(); i++) {
                    path.append("/").append(pathLists.get(i));
                }
                testCase.setNodePath(path.toString());
            });
            batchUpdateTestCase(testCases);
        }
        node.setUpdateTime(LocalDate.now());
        baseMapper.updateById(node);
        return ResponseResult.success(node);
    }

    @Override
    public ResponseResult<Integer> deleteNode(List<String> nodeIds) {
        if (org.springframework.util.CollectionUtils.isEmpty(nodeIds)) {
            return ResponseResult.success(1);
        }
        // 删除模块下关联的测试用例
        List<String> testCaseIdList = selectCaseIdByNodeIds(nodeIds);
        if (CollectionUtils.isNotEmpty(testCaseIdList)) {
            testCaseMapper.deleteBatchIds(testCaseIdList);
        }
        // 删除模块数据
        int batchIds = baseMapper.deleteBatchIds(nodeIds);
        return ResponseResult.success(batchIds);
    }

    @Override
    public ResponseResult<List<TestCaseNode>> getAllNodeByPlanId(QueryNodeRequest request) {
        String planId = request.getTestPlanId();
        TestPlan testPlan = testPlanMapper.selectById(planId);
        if (Objects.isNull(testPlan)) {
            return ResponseResult.success(new ArrayList<>());
        }
        return getAllNodeByProjectId(request);
    }

    @Override
    public ResponseResult<List<TestCaseNode>> getAllNodeByProjectId(QueryNodeRequest request) {
        return getNodeTreeByProjectId(request.getProjectId() );
    }

    @Override
    public ResponseResult<List<TestCaseNode>> getNodeByPlanId(String planId) {
        List<TestCase> testCases = testCaseMapper.getTestCaseWithNodeInfo(planId);
        Map<String, List<String>> projectNodeMap = getProjectNodeMap(testCases);
        List<TestCaseNode> list = getNodeTreeWithPruningTree(projectNodeMap);
        return ResponseResult.success(list);
    }

    private List<TestCaseNode> getNodeTreeWithPruningTree(Map<String, List<String>> projectNodeMap) {
        List<TestCaseNode> list = new ArrayList<>();
        projectNodeMap.forEach((k,v)->{
            Project project = projectMapper.selectById(k);
            if (Objects.nonNull(project)) {
                String name = project.getName();
                List<TestCaseNode> testCaseNodes = getNodeTreeWithPruningTree(k, v);
                TestCaseNode testCaseNode = new TestCaseNode();
                testCaseNode.setProjectId(project.getId());
                testCaseNode.setName(name);
                testCaseNode.setLabel(name);
                testCaseNode.setChildren(testCaseNodes);
                if (CollectionUtils.isNotEmpty(testCaseNodes)) {
                    list.add(testCaseNode);
                }
            }
        });
        return list;
    }

    private List<TestCaseNode> getNodeTreeWithPruningTree(String projectId, List<String> pruningTreeIds) {
        List<TestCaseNode> testCaseNodes = baseMapper.getNodeTreeByProjectId(projectId);
        List<TestCaseNode> nodeTrees= getNodeTrees(testCaseNodes);
        nodeTrees.removeIf(rootNode -> pruningTree(rootNode, pruningTreeIds));
        return nodeTrees;
    }

    private boolean pruningTree(TestCaseNode rootNode, List<String> nodeIds) {
        List<TestCaseNode> children = rootNode.getChildren();
        if (Objects.isNull(children) || children.isEmpty()) {
            if (!nodeIds.contains(rootNode.getId())) {
                return true;
            }
        }
        if (Objects.nonNull(children)) {
            children.removeIf(next -> pruningTree(next, nodeIds));
            return children.isEmpty() && !nodeIds.contains(rootNode.getId());
        }
        return false;
    }

    private Map<String, List<String>> getProjectNodeMap(List<TestCase> testCases) {
        Map<String, List<String>> projectNodeMap = new HashMap<>();
        for (TestCase testCase : testCases) {
            List<String> nodeIds = Optional.ofNullable(projectNodeMap.get(testCase.getProjectId())).orElse(new ArrayList<>());
            nodeIds.add(testCase.getNodeId());
            projectNodeMap.put(testCase.getProjectId(), nodeIds);
        }
        return projectNodeMap;
    }

    private List<String> selectCaseIdByNodeIds(List<String> nodeIds) {
        if (CollectionUtils.isEmpty(nodeIds)) {
            return new ArrayList<>();
        } else {
            LambdaQueryWrapper<TestCase> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(TestCase::getNodeId, nodeIds);
            return testCaseMapper.selectIdsByNodeIds(wrapper);
        }
    }
    private void batchUpdateTestCase(List<TestCase> testCases) {
        testCases.forEach(testCase -> testCaseMapper.updateById(testCase));
    }

    private void validateNode(TestCaseNode node) {
        if (node.getLevel() > TestCaseConstants.MAX_NODE_DEPTH) {
            throw new RuntimeException("模块树最大深度为" + TestCaseConstants.MAX_NODE_DEPTH + "层");
        }
        checkTestCaseNodeExist(node);
    }

    private void checkTestCaseNodeExist(TestCaseNode node) {
        if (Objects.nonNull(node.getName())) {
            LambdaQueryWrapper<TestCaseNode> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TestCaseNode::getName, node.getName()).eq(TestCaseNode::getProjectId, node.getProjectId());
            if (StringUtils.isNotBlank(node.getParentId())) {
                wrapper.eq(TestCaseNode::getParentId, node.getParentId());
            } else {
                wrapper.eq(TestCaseNode::getLevel, node.getLevel());
            }
            if (StringUtils.isNotBlank(node.getId())) {
                wrapper.ne(TestCaseNode::getId, node.getId());
            }
            if (baseMapper.selectCount(wrapper) > 0) {
                BizException.throwException("同层级下已存在该模块名称");
            }
        }
    }

    public static List<String> nodeList(List<TestCaseNode> testCaseNodes, String pid, List<String> list) {
        testCaseNodes.forEach(node -> {
            if (StringUtils.equals(node.getParentId(), pid)) {
                list.add(node.getId());
                nodeList(testCaseNodes, node.getId(), list);
            }
        });
        return list;
    }

    private Map<String, Long> parseModuleCountList(List<Map<String, Object>> moduleCountList) {
        Map<String, Long> returnMap = new HashMap<>();
        for (Map<String, Object> map : moduleCountList) {
            Object moduleIdObj = map.get("moduleId");
            Object countNumObj = map.get("countNum");
            if (moduleIdObj != null && countNumObj != null) {
                String moduleId = String.valueOf(moduleIdObj);
                try {
                    Long countNumInteger = (long) countNumObj;
                    returnMap.put(moduleId, countNumInteger);
                } catch (Exception ignored) {
                }
            }
        }
        return returnMap;
    }

    private double getNextLevelPos(String projectId, int level, String parentId) {
        List<TestCaseNode> list = getPos(projectId, level, parentId, "desc");
        if (!org.springframework.util.CollectionUtils.isEmpty(list) && list.get(0) != null && list.get(0).getPos() != null) {
            return list.get(0).getPos() + 65536;
        } else {
            return 65536;
        }
    }

    private List<TestCaseNode> getPos(String projectId, int level, String parentId, String order) {
        LambdaQueryWrapper<TestCaseNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestCaseNode::getProjectId, projectId).eq(TestCaseNode::getLevel, level);
        if (level != 1 && StringUtils.isNotBlank(parentId)) {
            wrapper.eq(TestCaseNode::getParentId, parentId);
        }
        if (StringUtils.equals(order, "asc")) {
            wrapper.orderByAsc(TestCaseNode::getPos);
        } else {
            wrapper.orderByDesc(TestCaseNode::getPos);
        }
        return baseMapper.selectList(wrapper);
    }
}
