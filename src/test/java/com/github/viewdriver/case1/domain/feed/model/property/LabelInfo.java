package com.github.viewdriver.case1.domain.feed.model.property;

import lombok.Getter;
import lombok.Setter;

/**
 * 词卡信息
 * @author yanghuan
 */
@Getter
@Setter
public class LabelInfo {

    /**
     * 同类签id
     */
    private String labelId;

    /**
     * 同类签名称
     */
    private String labelName;
}
