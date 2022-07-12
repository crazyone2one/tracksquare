package io.square.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-27
 */
@Getter
@Setter
@TableName("test_case_template")
public class TestCaseTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * Field template name
     */
    @TableField("`name`")
    private String name;

    /**
     * Field template type
     */
    @TableField("type")
    private String type;

    /**
     * Field template description
     */
    @TableField("description")
    private String description;

    /**
     * Test Case Name
     */
    @TableField("case_name")
    private String caseName;

    /**
     * Is system field template
     */
    @TableField("system")
    private Boolean system;

    /**
     * Is global template
     */
    @TableField("`global`")
    private Boolean global;

    /**
     * Test case prerequisite condition
     */
    @TableField("prerequisite")
    private String prerequisite;

    /**
     * Test case steps desc
     */
    @TableField("step_description")
    private String stepDescription;

    /**
     * Test case expected result
     */
    @TableField("expected_result")
    private String expectedResult;

    /**
     * Test case actual result
     */
    @TableField("actual_result")
    private String actualResult;

    /**
     * Create timestamp
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate createTime;

    /**
     * Update timestamp
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate updateTime;

    /**
     * Step model
     */
    @TableField("step_model")
    private String stepModel;

    /**
     * Test case step
     */
    @TableField("steps")
    private String steps;

    @TableField("create_user")
    private String createUser;

    @TableField("project_id")
    private String projectId;

    @TableField(exist = false)
    private List<CustomFieldTemplate> customFields;
}
