package io.square.dto;

import io.square.entity.Project;
import io.square.entity.Workspace;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by 11's papa on 2022/6/28 0028
 * @version 1.0.0
 */
@Data
public class WorkspaceResource {
    private List<Workspace> workspaces = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();
}
