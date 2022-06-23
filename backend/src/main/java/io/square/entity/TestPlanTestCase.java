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
 * 测试计划-测试用例关联
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-22
 */
@Getter
@Setter
@TableName("test_plan_test_case")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestPlanTestCase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * Plan ID relation to
     */
    @TableField("plan_id")
    private String planId;

    /**
     * Case ID relation to
     */
    @TableField("case_id")
    private String caseId;

    /**
     * Test report ID relation to
     */
    @TableField("report_id")
    private String reportId;

    /**
     * Test case executor
     */
    @TableField("executor")
    private String executor;

    /**
     * Test case status
     */
    @TableField("status")
    private String status;

    /**
     * Test case result
     */
    @TableField("results")
    private String results;

    /**
     * Test case result issues
     */
    @TableField("issues")
    private String issues;

    /**
     * Test case remark
     */
    @TableField("remark")
    private String remark;

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

    @TableField("actual_result")
    private String actualResult;

    @TableField("create_user")
    private String createUser;

    @TableField("issues_count")
    private Integer issuesCount;

    /**
     * 自定义排序，间隔5000
     */
    @TableField("`order`")
    private Long order;

    /**
     * 关联的用例是否放入回收站
     */
    @TableField("is_del")
    private Boolean isDel;

}
