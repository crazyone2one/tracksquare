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
 * @since 2022-06-15
 */
@Getter
@Setter
@TableName("test_case_node")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TestCaseNode  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Test case node ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * Project ID this node belongs to
     */
    @TableField("project_id")
    private String projectId;

    /**
     * Node name
     */
    @TableField("name")
    private String name;

    /**
     * Parent node ID
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * Node level
     */
    @TableField("level")
    private Integer level;

    /**
     * Update timestamp
     */
    @TableField("update_time")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate updateTime;

    /**
     * create time
     */
    @TableField("create_time")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate createTime;

    @TableField("pos")
    private Double pos;

    @TableField("create_user")
    private String createUser;

    @TableField(exist = false)
    private Integer caseNum;

    @TableField(exist = false)
    private List<String> nodeIds;

    @TableField(exist = false)
    private String label;

    @TableField(exist = false)
    private List<TestCaseNode> children;

}
