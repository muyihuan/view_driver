package com.wb.case1.view;

import lombok.Data;

/**
 * feed背景图信息
 * @author yanghuan
 */
@Data
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
