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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("custom_field")
@Data
public class CustomField implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * Custom field name
     */
    @TableField("name")
    private String name;

    /**
     * Custom field use scene
     */
    @TableField("scene")
    private String scene;

    /**
     * Custom field type
     */
    @TableField("type")
    private String type;

    /**
     * Custom field remark
     */
    @TableField("remark")
    private String remark;

    /**
     * Test resource pool status
     */
    @TableField("options")
    private String options;

    /**
     * Is system custom field
     */
    @TableField("system")
    private Boolean system;

    /**
     * Is global custom field
     */
    @TableField("global")
    private Boolean global;

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

    @TableField("create_user")
    private String createUser;

    @TableField("project_id")
    private String projectId;


}
