package com.github.viewdriver.case1.domain.feed.model.ugc;

import lombok.Getter;
import lombok.Setter;

/**
 * 文本内容模型
 * @author yanghuan
 */
@Getter
@Setter
public class TextContentInfo {

    /**
     * 用户id
     */
    private String uid;

    /**
     * 状态内容
     */
    private String content;

    /**
     * 状态类型
     */
    private Integer type;
}
