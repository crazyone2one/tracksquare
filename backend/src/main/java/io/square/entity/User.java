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
 * @since 2022-06-13
 */
@Getter
@Setter
@TableName("user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * User ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("nick_name")
    private String nickName;

    /**
     * User name
     */
    @TableField("name")
    private String name;

    /**
     * E-Mail address
     */
    @TableField("email")
    private String email;

    @TableField("password")
    private String password;

    /**
     * User status
     */
    @TableField("status")
    private String status;

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

    @TableField("language")
    private String language;

    @TableField("last_workspace_id")
    private String lastWorkspaceId;

    /**
     * Phone number of user
     */
    @TableField("phone")
    private String phone;

    @TableField("source")
    private String source;

    @TableField("last_project_id")
    private String lastProjectId;

    @TableField("create_user")
    private String createUser;

    /**
     *  其他平台对接信息
     */
    @TableField("platform_info")
    private String platformInfo;

    @TableField("selenium_server")
    private String seleniumServer;

    @TableField("token")
    private String token;
}
