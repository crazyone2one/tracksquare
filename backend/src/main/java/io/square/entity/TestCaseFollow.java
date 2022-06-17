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
 * @since 2022-06-17
 */
@Getter
@Setter
@TableName("test_case_follow")
public class TestCaseFollow implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("case_id")
    private String caseId;

    @TableField("follow_id")
    private String followId;


}
