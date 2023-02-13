package com.wb.case1.domain.feed.model.ugc;

import lombok.Data;

/**
 * 奖牌信息
 * @author edz
 */
@Data
public class VideoContentInfo {

    /**
     * 用户url
     */
    private String uid;

    /**
     * 图片url
     */
    private String imageUrl;

    /**
     * 播放url
     */
    private String playUrl;

    /**
     * 视频宽度
     */
    private int width;

    /**
     * 视频高度
     */
    private int height;

    /**
     * 视频长度，单位表
     */
    private int videoLength;
}
