package io.square.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

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
@TableName("project_application")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("project_id")
    private String projectId;

    @TableField("type")
    private String type;

    @TableField("type_value")
    private String typeValue;


}
