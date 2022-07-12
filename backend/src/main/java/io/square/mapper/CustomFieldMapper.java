package io.square.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.square.controller.request.QueryCustomFieldRequest;
import io.square.entity.CustomField;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface CustomFieldMapper extends BaseMapper<CustomField> {

    IPage<CustomField> searchByCondition(Page<CustomField> page, @Param("request") QueryCustomFieldRequest request);

    List<CustomField> list(@Param("request") QueryCustomFieldRequest request);
}
