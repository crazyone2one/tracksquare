package io.square.service;

import io.square.entity.TestPlanFollow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-21
 */
public interface TestPlanFollowService extends IService<TestPlanFollow> {

    int insertTestPlanFollow(TestPlanFollow testPlanFollow);

    void deleteTestPlanFollowByPlanId(String planId);
}
