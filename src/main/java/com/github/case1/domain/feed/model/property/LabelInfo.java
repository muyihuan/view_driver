package com.github.case1.domain.feed.model.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 词卡信息
 * @author yanghuan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
