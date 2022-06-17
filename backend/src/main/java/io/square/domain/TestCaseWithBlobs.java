package io.square.domain;

import io.square.entity.TestCase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author by 11's papa on 2022/6/16 0016
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TestCaseWithBlobs extends TestCase implements Serializable {
    private String prerequisite;

    private String remark;

    private String steps;

    private String stepDescription;

    private String expectedResult;

    private String customFields;

    private static final long serialVersionUID = 1L;
}
