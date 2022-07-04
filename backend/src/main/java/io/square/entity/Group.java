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
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-28
 */
@Getter
@Setter
@TableName("`group`")
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    /**
     * 是否是系统用户组
     */
    @TableField("system")
    private Boolean system;

    /**
     * 所属类型
     */
    @TableField("type")
    private String type;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate createTime;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate updateTime;

    /**
     * 创建人(操作人）
     */
    @TableField("creator")
    private String creator;

    /**
     * 应用范围
     */
    @TableField("scope_id")
    private String scopeId;

    @TableField(exist = false)
    private List<String> types = new ArrayList<>();
    @TableField(exist = false)
    private List<String> scopes = new ArrayList<>();
    @TableField(exist = false)
    private Boolean global;
    @TableField(exist = false)
    private String projectId;
    @TableField(exist = false)
    private String userGroupId;

    @TableField(exist = false)
    private String currentUserId;
    @TableField(exist = false)
    private long page;
    @TableField(exist = false)
    private long limit;
    @TableField(exist = false)
    private long memberSize;
}
