package io.square.dto;

import lombok.Data;

/**
 * @author by 11's papa on 2022/7/12 0012
 * @version 1.0.0
 */
@Data
public class GroupPermission {
    private String id;
    private String name;
    private String resourceId;
    private Boolean checked = false;
    private Boolean license = false;
}
