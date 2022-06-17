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
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-15
 */
@Getter
@Setter
@TableName("test_case")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestCase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Test case ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * Node ID this case belongs to
     */
    @TableField("node_id")
    private String nodeId;

    @TableField("test_id")
    private String testId;

    /**
     * Node path this case belongs to
     */
    @TableField("node_path")
    private String nodePath;

    /**
     * Project ID this test belongs to
     */
    @TableField("project_id")
    private String projectId;
    @TableField(exist = false)
    private String projectName;

    /**
     * Test case name
     */
    @TableField("name")
    private String name;

    /**
     * Test case type
     */
    @TableField("type")
    private String type;

    /**
     * Test case maintainer
     */
    @TableField("maintainer")
    private String maintainer;
    @TableField(exist = false)
    private String maintainerName;

    /**
     * Test case priority
     */
    @TableField("priority")
    private String priority;

    /**
     * Test case method type
     */
    @TableField("method")
    private String method;

    /**
     * Test case prerequisite condition
     */
    @TableField("prerequisite")
    private String prerequisite;

    /**
     * Test case remark
     */
    @TableField("remark")
    private String remark;

    /**
     * Test case steps (JSON format)
     */
    @TableField("steps")
    private String steps;

    /**
     * Create timestamp
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate createTime;

    /**
     * Update timestamp
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate updateTime;

    /**
     * Import test case sort
     */
    @TableField("sort")
    private Integer sort;

    /**
     * Manually controlled growth identifier
     */
    @TableField("num")
    private Integer num;

    @TableField("other_test_name")
    private String otherTestName;

    @TableField("review_status")
    private String reviewStatus;

    @TableField("tags")
    private String tags;

    @TableField("demand_id")
    private String demandId;

    @TableField("demand_name")
    private String demandName;

    @TableField("status")
    private String status;

    @TableField("step_description")
    private String stepDescription;

    @TableField("expected_result")
    private String expectedResult;

    /**
     * CustomField
     */
    @TableField("custom_fields")
    private String customFields;

    /**
     * Test case step model
     */
    @TableField("step_model")
    private String stepModel;

    /**
     * custom num
     */
    @TableField("custom_num")
    private String customNum;

    @TableField("create_user")
    private String createUser;
    @TableField(exist = false)
    private String createName;

    @TableField("original_status")
    private String originalStatus;

    /**
     * Delete timestamp
     */
    @TableField("delete_time")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate deleteTime;

    /**
     * Delete user id
     */
    @TableField("delete_user_id")
    private String deleteUserId;

    /**
     * 自定义排序，间隔5000
     */
    @TableField("`order`")
    private Long order;

    /**
     * 是否是公共用例
     */
    @TableField("case_public")
    private Boolean casePublic;

    /**
     * 版本ID
     */
    @TableField("version_id")
    private String versionId;

    /**
     * 指向初始版本ID
     */
    @TableField("ref_id")
    private String refId;

    /**
     * 是否为最新版本 0:否，1:是
     */
    @TableField("latest")
    private Boolean latest;


    @TableField(exist = false)
    private List<String> follows;

}
