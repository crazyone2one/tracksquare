package io.square.dto;

import io.square.domain.TestCaseWithBlobs;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author by 11's papa on 2022/6/16 0016
 * @version 1.0.0
 */
@Setter
@Getter
public class TestCaseDTO extends TestCaseWithBlobs {
    private String maintainerName;
    private String apiName;
    private String performName;
    private String lastResultId;
    private String projectName;
    private String createName;
    private String lastExecuteResult;
    private String versionName;
    private List<String> caseTags;
}
