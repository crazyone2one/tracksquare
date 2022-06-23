package io.square.mapper;

import io.square.entity.TestPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-20
 */
@Mapper
public interface TestPlanMapper extends BaseMapper<TestPlan> {

}
