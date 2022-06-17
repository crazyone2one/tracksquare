package io.square.mapper;

import io.square.entity.TestCaseFollow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-17
 */
@Mapper
public interface TestCaseFollowMapper extends BaseMapper<TestCaseFollow> {
@Select("delete from test_case_follow where case_id=#{caseId}")
    void delByCaseId(String caseId);
}
