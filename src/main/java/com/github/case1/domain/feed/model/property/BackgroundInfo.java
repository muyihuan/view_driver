package com.github.case1.domain.feed.model.property;

import lombok.Getter;
import lombok.Setter;

/**
 * feed背景信息
 * @author yanghuan
 */
@Getter
@Setter
public class BackgroundInfo {

    /**
     * 动态图
     */
    private Long backgroundImgId;

    /**
     * 静态图
     */
    private String staticImgUrl;

    /**
     * 动态图
     */
    private String dynamicImgUrl;
}
