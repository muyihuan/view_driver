package com.github.case1.view;

import lombok.Getter;
import lombok.Setter;

/**
 * feed背景图信息
 * @author yanghuan
 */
@Getter
@Setter
public class BackgroundView {

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
