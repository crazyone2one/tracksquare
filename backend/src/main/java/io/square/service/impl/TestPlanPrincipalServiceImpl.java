package io.square.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.square.entity.TestPlanPrincipal;
import io.square.mapper.TestPlanPrincipalMapper;
import io.square.service.TestPlanPrincipalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-21
 */
@Service
public class TestPlanPrincipalServiceImpl extends ServiceImpl<TestPlanPrincipalMapper, TestPlanPrincipal> implements TestPlanPrincipalService {

    @Override
    public void deleteTestPlanPrincipalByPlanId(String planId) {
        LambdaQueryWrapper<TestPlanPrincipal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestPlanPrincipal::getTestPlanId, planId);
        baseMapper.delete(wrapper);
    }

    @Override
    public int insertTestPlanPrincipal(TestPlanPrincipal planPrincipal) {
        return baseMapper.insert(planPrincipal);
    }
}
