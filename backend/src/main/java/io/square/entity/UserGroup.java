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
@TableName("user_group")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("user_id")
    private String userId;

    @TableField("group_id")
    private String groupId;

    @TableField("source_id")
    private String sourceId;

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

    @TableField(exist = false)
    private String name;
    /**
     * 用户组所属类型
     */
    @TableField(exist = false)
    private String type;
}
