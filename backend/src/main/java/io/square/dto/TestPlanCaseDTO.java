package io.square.dto;

import io.square.domain.TestCaseWithBlobs;
import lombok.Data;

/**
 * @author by 11's papa on 2022/6/24 0024
 * @version 1.0.0
 */
@Data
public class TestPlanCaseDTO extends TestCaseWithBlobs {
    private String executor;
    private String executorName;
    private String results;
    private String planId;
    private String planName;
    private String caseId;
    private String issues;
    private String reportId;
    private String model;
    private String projectName;
    private String actualResult;
    private String maintainerName;
    private Boolean isCustomNum;
    private int issuesCount;
    private String versionName;
}
