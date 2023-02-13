package com.wb.case1.domain.feed.enums;

import lombok.Getter;

/**
 * @author liudongyue
 * @version 1.0.0
 * @Description feed 来源的类型
 * @createTime 2021年05月10日 14:22:00
 */
@Getter
public enum FeedSourceTypeEnum {

    /**
     *
     */
    SIGNATURE(0, " 文字，签名触发"),
    IMAGE(1, " 单图"),
    CHAMPION(2, " 花神"),
    RCMDFRD(3, ""),
    RICH(4, " 旧喊话"),
    STATUS(5, " 文字"),
    GAMBLING_KING(6, ""),
    LOTTERY(7, ""),
    RCMDFRD_MULTI(8, ""),
    LINK(9, " 链接"),
    PAINT_PLAY(10, " 画猜"),
    RICH_NEW(11, " 喊话"),
    VIDEO_PLAY(12, " 视频"),
    PAINT_PLAY_NEW(13, " 新画猜"),
    MEDAL(14, " 徽章"),
    IMAGE_MULTI(15, "多图feed"),
    REPOST(16, "分享(转发)"),
    FEED(17, "feed"),
    AUDIO(18, "语音feed"),
    COMMENT(19, "评论"),
    RED_PACKET(20, " 红包"),
    RECBAR(21, "推荐条"),
    RECTAG(22, "推荐tag"),
    LIVING_ROOM_AUDIO(23, "语音房专辑feed"),
    VIDEO_PLAY_NEW(24, "新视频feed类型"),
    ;

    /**
     * code
     */
    private final int code;

    /**
     * 描述
     */
    private final String desc;

    FeedSourceTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static FeedSourceTypeEnum getByCode(int code) {
        for (FeedSourceTypeEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }

        throw new RuntimeException("FeedSourceType can't find : " + code);
    }
}
