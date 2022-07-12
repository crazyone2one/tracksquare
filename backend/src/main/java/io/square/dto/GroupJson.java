package io.square.dto;

import lombok.Data;

import java.util.List;

/**
 * @author by 11's papa on 2022/7/12 0012
 * @version 1.0.0
 */
@Data
public class GroupJson {
    private List<GroupResource> resource;
    private List<GroupPermission> permissions;
}
