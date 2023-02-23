package com.github.case1.domain.feed.model.ugc;

import lombok.Getter;
import lombok.Setter;

/**
 * 链接信息
 * @author edz
 */
@Getter
@Setter
public class LinkContentInfo {

    /**
     * 用户id
     */
    private String uid;

    /**
     * 链接的标题
     */
    private String title;

    /**
     * 链接的内容描述
     */
    private String desc;

    /**
     * 链接内容的图片
     */
    private String icon;

    /**
     * 链接的跳转地址
     */
    private String url;

    /**
     * 扩展信息，json
     */
    private String optInfo;
}
