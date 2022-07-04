package io.square.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.square.entity.CustomFieldTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-27
 */
@Mapper
public interface CustomFieldTemplateMapper extends BaseMapper<CustomFieldTemplate> {

    List<CustomFieldTemplate> listAllByTemplateId(@Param("request") CustomFieldTemplate request);
}
