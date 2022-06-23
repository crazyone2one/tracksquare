package io.square.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.square.common.ResponseResult;
import io.square.controller.request.QueryNodeRequest;
import io.square.entity.TestCaseNode;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-15
 */
public interface TestCaseNodeService extends IService<TestCaseNode> {

    /**
     * 查询项目对应的模块
     *
     * @param projectId projectId
     * @return io.square.common.ResponseResult<java.util.List < io.square.entity.TestCaseNode>>
     */
    ResponseResult<List<TestCaseNode>> getNodeTreeByProjectId(String projectId);

    /**
     * 判断当前项目下是否有默认模块，没有添加默认模块
     *
     * @param projectId projectId
     * @return io.square.entity.TestCaseNode
     */
    TestCaseNode getDefaultNode(String projectId);

    ResponseResult<String> addNode(TestCaseNode node);

    ResponseResult<TestCaseNode> editNode(TestCaseNode node);

    ResponseResult<Integer> deleteNode(List<String> nodeIds);

    ResponseResult<List<TestCaseNode>> getAllNodeByPlanId(QueryNodeRequest request);

    ResponseResult<List<TestCaseNode>> getAllNodeByProjectId(QueryNodeRequest request);

    /**
     * 根据plan id 查询对应的node信息
     *
     * @param planId plan id
     * @return io.square.common.ResponseResult<java.util.List < io.square.entity.TestCaseNode>>
     */
    ResponseResult<List<TestCaseNode>> getNodeByPlanId(String planId);
}
