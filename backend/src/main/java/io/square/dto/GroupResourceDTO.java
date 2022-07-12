package io.square.dto;

import io.square.entity.Group;
import io.square.entity.UserGroupPermission;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author by 11's papa on 2022/7/12 0012
 * @version 1.0.0
 */
@Data
public class GroupResourceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private GroupResource resource;
    private List<GroupPermission> permissions;
    private String type;

    private Group group;
    private List<UserGroupPermission> userGroupPermissions;
}
