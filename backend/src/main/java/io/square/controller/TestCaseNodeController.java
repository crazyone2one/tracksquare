package io.square.controller;

import io.square.common.ResponseResult;
import io.square.entity.TestCaseNode;
import io.square.service.TestCaseNodeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-15
 */
@RestController
@RequestMapping("/case/node")
public class TestCaseNodeController {
    @Resource
    TestCaseNodeService service;

    @GetMapping("/list/{projectId}")
    public ResponseResult<List<TestCaseNode>> getNodeByProjectId(@PathVariable String projectId) {
        return service.getNodeTreeByProjectId(projectId);
    }

    @PostMapping("/add")
    public ResponseResult<String> addNode(@RequestBody TestCaseNode node) {
        return service.addNode(node);
    }

    @PostMapping("/edit")
    public ResponseResult<TestCaseNode> editNode(@RequestBody TestCaseNode node) {
        return service.editNode(node);
    }

    @PostMapping("/delete")
    public ResponseResult<Integer> deleteNode(@RequestBody List<String> nodeIds) {
        return service.deleteNode(nodeIds);
    }
}
