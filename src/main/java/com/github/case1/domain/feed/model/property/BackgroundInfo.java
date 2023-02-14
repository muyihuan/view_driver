package com.github.case1.domain.feed.model.property;

import lombok.Data;

/**
 * feed背景信息
 * @author yanghuan
 */
@Data
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
