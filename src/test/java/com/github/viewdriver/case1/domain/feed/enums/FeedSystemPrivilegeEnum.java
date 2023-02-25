package com.github.viewdriver.case1.domain.feed.enums;

import lombok.Getter;

/**
 * feed的系统可见范围，是系统侧设置的可见范围，优先级大于用户设置的可见范围
 * @author yanghuan
 */
@Getter
public enum FeedSystemPrivilegeEnum {

    /**
     * 默认
     */
    DEF(0, "默认-系统未设置可见范围"),

    /**
     * 这个是feed不允许对外开放
     */
    FEED_SELF_VIEW(1, "feed只允许自己可见"),

    /**
     * 这个feed的ugc内容不允许对外开放，但是feed是可以的，只不过展示的ugc内容可能得做一些处理
     */
    UGC_SELF_VIEW(2, "ugc内容只允许自己可见"),

    ;

    private final int code;
    private final String desc;

    FeedSystemPrivilegeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
