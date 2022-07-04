package io.square.service;

import io.square.common.ResponseResult;
import io.square.entity.IssueTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-27
 */
public interface IssueTemplateService extends IService<IssueTemplate> {

    ResponseResult<IssueTemplate> getTemplate(String projectId);
}
