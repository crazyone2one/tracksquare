package io.square.service;

import io.square.entity.TestPlanPrincipal;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-21
 */
public interface TestPlanPrincipalService extends IService<TestPlanPrincipal> {

    void deleteTestPlanPrincipalByPlanId(String planId);

    int insertTestPlanPrincipal(TestPlanPrincipal planPrincipal);
}
