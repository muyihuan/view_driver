package com.github.case1.domain.feed.enums;

import lombok.Getter;

/**
 * @author liudongyue
 * @version 1.0.0
 * @Description 展示类型
 * @createTime 2021年05月10日 14:22:00
 */
@Getter
public enum FeedContentTypeEnum {

    /**
     * feed的内容类型
     */
    TEXT(0, "纯文本类型"),
    IMAGE(1, "单图类型"),
    PERSON(2, "花神"),
    LOTTERY(3, "猜猜类型"),
    RCMDFRD_MULTI(4, "未知"),
    LINK(5, "链接类型"),
    PAINT_PLAY(6, "旧画猜类型"),
    VIDEO_PLAY(7, "视频类型"),
    PAINT_PLAY_NEW(8, "新版画猜类型"),
    MEDAL(9, "称号类型"),
    IMAGE_MULTI(10, "多图类型"),
    FORWARD(11, "转发类型"),
    AUDIO(12, "语音类型"),
    VIDEO_PLAY_NEW(18, "新视频类型"),

    ;

    /**
     * type
     */
    private final int type;

    /**
     * 描述
     */
    private final String desc;

    FeedContentTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static FeedContentTypeEnum getByCode(int type) {
        for (FeedContentTypeEnum value : FeedContentTypeEnum.values()) {
            if (value.getType() == type) {
                return value;
            }
        }

        throw new RuntimeException("FeedContentType can't find : " + type);
    }
}
