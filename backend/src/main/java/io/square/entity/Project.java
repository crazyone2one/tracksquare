package io.square.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 *
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-14
 */
@Getter
@Setter
@TableName("project")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Project ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * Workspace ID this project belongs to
     */
    @TableField("workspace_id")
    private String workspaceId;

    /**
     * Project name
     */
    @TableField("name")
    private String name;

    /**
     * Project description
     */
    @TableField("description")
    private String description;

    /**
     * Create timestamp
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate createTime;

    /**
     * Update timestamp
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate updateTime;

    @TableField("tapd_id")
    private String tapdId;

    @TableField("jira_key")
    private String jiraKey;

    @TableField("zentao_id")
    private String zentaoId;

    @TableField("azure_devops_id")
    private String azureDevopsId;

    /**
     * Relate test case template id
     */
    @TableField("case_template_id")
    private String caseTemplateId;

    /**
     * Relate test issue template id
     */
    @TableField("issue_template_id")
    private String issueTemplateId;

    @TableField("create_user")
    private String createUser;

    @TableField("system_id")
    private String systemId;

    /**
     * azure 过滤需求的 parent workItem ID
     */
    @TableField("azure_filter_id")
    private String azureFilterId;

    /**
     * 项目使用哪个平台的模板
     */
    @TableField("platform")
    private String platform;

    /**
     * 是否使用第三方平台缺陷模板
     */
    @TableField("third_part_template")
    private Boolean thirdPartTemplate;

    @TableField("version_enable")
    private Boolean versionEnable;

    @TableField("issue_config")
    private String issueConfig;


}
