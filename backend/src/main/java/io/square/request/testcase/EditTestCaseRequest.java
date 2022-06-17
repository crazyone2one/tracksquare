package io.square.request.testcase;

import io.square.domain.TestCaseWithBlobs;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by 11's papa on 2022/6/16 0016
 * @version 1.0.0
 */
@Getter
@Setter
public class EditTestCaseRequest extends TestCaseWithBlobs {
    @Getter
    @Setter
    public static class OtherInfoConfig {
        //是否复制备注
        private boolean remark;
        //是否复制关联测试
        private boolean relateTest;
        //是否复制关联需求
        private boolean relateDemand;
        //是否复制关联缺陷
        private boolean relateIssue;
        //是否复制附件
        private boolean archive;
        //是否复制依赖
        private boolean dependency;

        public static OtherInfoConfig createDefault() {
            OtherInfoConfig o = new OtherInfoConfig();
            o.setArchive(true);
            o.setRemark(true);
            o.setRelateTest(true);
            o.setDependency(true);
            o.setRelateDemand(true);
            o.setRelateIssue(true);
            return o;
        }
    }

    private List<String> follows;
    private OtherInfoConfig otherInfoConfig;
    private String oldDataId;
    private List<String> fileIds = new ArrayList<>();
    private List<List<String>> selected = new ArrayList<>();
    // 是否处理附件文件
    private boolean handleAttachment = true;
}
