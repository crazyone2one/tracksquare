package io.square.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-21
 */
@Getter
@Setter
@TableName("test_plan_principal")
public class TestPlanPrincipal implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("test_plan_id")
    private String testPlanId;

    @TableField("principal_id")
    private String principalId;


}
