package com.github.case1.domain.feed.enums;

import lombok.Getter;

/**
 * @author liudongyue
 * @version 1.0.0
 * @Description feed 可见性
 * @createTime 2020年12月29日 15:32:00
 */
@Getter
public enum FeedPrivilegeEnum {

    FRIEND_VIEW(0, "仅好友可见"),
    SQUARE_VIEW(1, "全部可见"),
    STRANGER_VIEW(2, "仅陌生人可见"),
    SELF_VIEW(3, "仅自己可见")
    ;

    private final int code;
    private final String desc;

    FeedPrivilegeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static FeedPrivilegeEnum getByCode(int code) {
        for (FeedPrivilegeEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }

        throw new RuntimeException("FeedPrivilegeEnum can't find : " + code);
    }
}
