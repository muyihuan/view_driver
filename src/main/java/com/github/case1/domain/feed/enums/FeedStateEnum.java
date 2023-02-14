package com.github.case1.domain.feed.enums;

import lombok.Getter;

/**
 * @author liudongyue
 * @version 1.0.0
 * @Description feed 状态
 * @createTime 2020年12月29日 15:32:00
 */
@Getter
public enum FeedStateEnum {

    NORMAL(0, "正常状态"),
    DEL(1, "删除状态"),

    /**
     * 新的feed模型已经不存在这两种状态， after 2021-12-20
     */
    @Deprecated
    SELF_VIEW(2, "自见状态"),
    @Deprecated
    IN_AUDIT(3, "审核中状态"),
    ;

    private final int status;
    private final String desc;

    FeedStateEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static FeedStateEnum getByCode(int status) {
        for (FeedStateEnum value : values()) {
            if (value.getStatus() == status) {
                return value;
            }
        }

        throw new RuntimeException("FeedStateEnum can't find : " + status);
    }
}
