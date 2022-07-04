package io.square.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.square.entity.TestPlanFollow;
import io.square.mapper.TestPlanFollowMapper;
import io.square.service.TestPlanFollowService;
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
public class TestPlanFollowServiceImpl extends ServiceImpl<TestPlanFollowMapper, TestPlanFollow> implements TestPlanFollowService {

    @Override
    public int insertTestPlanFollow(TestPlanFollow testPlanFollow) {
        return baseMapper.insert(testPlanFollow);
    }

    @Override
    public void deleteTestPlanFollowByPlanId(String planId) {
        LambdaQueryWrapper<TestPlanFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestPlanFollow::getTestPlanId, planId);
        baseMapper.delete(wrapper);
    }
}
