package io.square.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

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
@TableName("custom_field_template")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomFieldTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Custom field field template related id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * Custom field ID
     */
    @TableField("field_id")
    private String fieldId;

    /**
     * Field template ID
     */
    @TableField("template_id")
    private String templateId;

    /**
     * Use scene
     */
    @TableField("scene")
    private String scene;

    /**
     * Is required
     */
    @TableField("required")
    private Boolean required;

    /**
     * Item order
     */
    @TableField("order")
    private Integer order;

    @TableField("default_value")
    private String defaultValue;

    /**
     * Custom data
     */
    @TableField("custom_data")
    private String customData;

    /**
     * Save Table Header Key
     */
    @TableField("key")
    private String key;

    @TableField(exist = false)
    private String name;
    @TableField(exist = false)
    private String type;
    @TableField(exist = false)
    private String remark;
    @TableField(exist = false)
    private String options;
    @TableField(exist = false)
    private Boolean system;
}
