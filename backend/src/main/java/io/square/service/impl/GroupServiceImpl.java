package io.square.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.square.common.ResponseResult;
import io.square.common.constants.UserGroupType;
import io.square.entity.Group;
import io.square.entity.UserGroup;
import io.square.exception.BizException;
import io.square.mapper.GroupMapper;
import io.square.service.GroupService;
import io.square.utils.SessionUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

    @Resource
    SessionUtils sessionUtils;

    private static final String GLOBAL = "global";
    private static final Map<String, List<String>> map = new HashMap<String, List<String>>(4) {{
        put(UserGroupType.SYSTEM, Arrays.asList(UserGroupType.SYSTEM, UserGroupType.WORKSPACE, UserGroupType.PROJECT));
        put(UserGroupType.WORKSPACE, Arrays.asList(UserGroupType.WORKSPACE, UserGroupType.PROJECT));
        put(UserGroupType.PROJECT, Collections.singletonList(UserGroupType.PROJECT));
    }};
    @Override
    public ResponseResult<Group> addGroup(Group group) {
        checkGroupExist(group);
        group.setSystem(false);
        if (group.getGlobal()) {
            group.setScopeId(GLOBAL);
        }
        baseMapper.insert(group);
        return ResponseResult.success(group);
    }

    @Override
    public ResponseResult<String> editGroup(Group group) {
        return null;
    }

    @Override
    public ResponseResult<Map<String, Object>> getGroupList(Group group, long page, long limit) {
        List<UserGroup> userGroup = baseMapper.getUserGroup(group.getCurrentUserId(), group.getProjectId());
        List<String> groupTypeList = userGroup.stream().map(UserGroup::getType).distinct().collect(Collectors.toList());
        group.setPage(page);
        group.setLimit(limit);
        IPage<Group> groups = getGroups(groupTypeList, group);
        Map<String, Object> result = new LinkedHashMap<>();
        assert groups != null;
        result.put("total", groups.getTotal());
        result.put("records", groups.getRecords());
        return ResponseResult.success(result);
    }

    private IPage<Group> getGroups(List<String> groupTypeList, Group group) {
        if (groupTypeList.contains(UserGroupType.SYSTEM)) {
            return getUserGroup(UserGroupType.SYSTEM, group);
        }
        if (groupTypeList.contains(UserGroupType.WORKSPACE)) {
            return getUserGroup(UserGroupType.WORKSPACE, group);
        }

        if (groupTypeList.contains(UserGroupType.PROJECT)) {
            return getUserGroup(UserGroupType.PROJECT, group);
        }
        return null;
    }

    private IPage<Group> getUserGroup(String groupType, Group request) {
        List<String> types;
        String workspaceId = sessionUtils.getCurrentWorkspaceId();
        List<String> scopes = Arrays.asList(GLOBAL, workspaceId);
        if (StringUtils.equals(groupType, UserGroupType.SYSTEM)) {
            scopes = new ArrayList<>();
        }
        types = map.get(groupType);
        request.setTypes(types);
        request.setScopes(scopes);
        Page<Group> page = new Page<>(request.getPage(), request.getLimit());
        return baseMapper.getGroupList(page, request);
    }

    private void checkGroupExist(Group group) {
        String name = group.getName();
        String id = group.getId();
        LambdaQueryWrapper<Group> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Group::getName, name);
        wrapper.ne(StringUtils.isNotBlank(id), Group::getId, id);
        if (CollectionUtils.isNotEmpty(baseMapper.selectList(wrapper))) {
            BizException.throwException("用户组名称已存在！");
        }
    }
}
