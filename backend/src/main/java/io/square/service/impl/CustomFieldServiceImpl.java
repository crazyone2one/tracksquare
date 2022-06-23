package io.square.service.impl;

import io.square.entity.CustomField;
import io.square.mapper.CustomFieldMapper;
import io.square.service.CustomFieldService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-20
 */
@Service
public class CustomFieldServiceImpl extends ServiceImpl<CustomFieldMapper, CustomField> implements CustomFieldService {

}
