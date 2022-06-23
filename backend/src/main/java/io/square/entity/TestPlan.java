package io.square.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2022-06-20
 */
@Getter
@Setter
@TableName("test_plan")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Test Plan ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * Workspace ID this plan belongs to
     */
    @TableField("workspace_id")
    private String workspaceId;

    /**
     * Test plan report
     */
    @TableField("report_id")
    private String reportId;

    /**
     * Plan name
     */
    @TableField("name")
    private String name;

    /**
     * Plan description
     */
    @TableField("description")
    private String description;

    /**
     * Plan status
     */
    @TableField("status")
    private String status;

    /**
     * Plan stage
     */
    @TableField("stage")
    private String stage;

    /**
     * Test case match rule
     */
    @TableField("test_case_match_rule")
    private String testCaseMatchRule;

    /**
     * Executor match rule)
     */
    @TableField("executor_match_rule")
    private String executorMatchRule;

    /**
     * Test plan tags (JSON format)
     */
    @TableField("tags")
    private String tags;

    /**
     * Create timestamp
     */
    @TableField("create_time")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate createTime;

    /**
     * Update timestamp
     */
    @TableField("update_time")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate updateTime;

    /**
     * Planned start time timestamp
     */
    @TableField("planned_start_time")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate plannedStartTime;

    /**
     * Planned end time timestamp
     */
    @TableField("planned_end_time")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate plannedEndTime;

    /**
     * Actual start time timestamp
     */
    @TableField("actual_start_time")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate actualStartTime;

    /**
     * Actual end time timestamp
     */
    @TableField("actual_end_time")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate actualEndTime;

    @TableField("creator")
    private String creator;
    @TableField(exist = false)
    private String userName;

    /**
     * 测试计划所属项目
     */
    @TableField("project_id")
    private String projectId;
    @TableField(exist = false)
    private String projectName;

    @TableField("execution_times")
    private Integer executionTimes;

    /**
     * 是否自定更新功能用例状态
     */
    @TableField("automatic_status_update")
    private Boolean automaticStatusUpdate;

    /**
     * 测试计划报告总结
     */
    @TableField("report_summary")
    private String reportSummary;

    /**
     * 测试计划报告配置
     */
    @TableField("report_config")
    private String reportConfig;

    /**
     * 是否允许重复添加用例
     */
    @TableField("repeat_case")
    private Boolean repeatCase;

    /**
     * request (JSON format)
     */
    @TableField("run_mode_config")
    private String runModeConfig;

    @TableField(exist = false)
    private List<String> projectIds;
    @TableField(exist = false)
    private List<String> principals;
    @TableField(exist = false)
    private List<String> follows;

    /**
     * 定时任务ID
     */
    @TableField(exist = false)
    private String scheduleId;
    /**
     * 定时任务是否开启
     */
    @TableField(exist = false)
    private boolean scheduleOpen;
    /**
     * 定时任务状态
     */
    @TableField(exist = false)
    private String scheduleStatus;
    /**
     * 定时任务规则
     */
    @TableField(exist = false)
    private String scheduleCorn;
    /**
     * 定时任务下一次执行时间
     */
    @TableField(exist = false)
    private Long scheduleExecuteTime;
    @TableField(exist = false)
    private Double passRate;
    @TableField(exist = false)
    private Double testRate;
    @TableField(exist = false)
    private Integer passed;
    @TableField(exist = false)
    private Integer tested;
    @TableField(exist = false)
    private Integer total;
    @TableField(exist = false)
    private String createUser;
    @TableField(exist = false)
    private Integer testPlanTestCaseCount;
    @TableField(exist = false)
    private Integer testPlanApiCaseCount;
    @TableField(exist = false)
    private Integer testPlanApiScenarioCount;
    @TableField(exist = false)
    private Integer testPlanLoadCaseCount;
}
