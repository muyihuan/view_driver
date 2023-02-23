package com.github.case1.domain.feed.model.ugc;

import lombok.Getter;
import lombok.Setter;

/**
 * 奖牌信息
 * @author edz
 */
@Getter
@Setter
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
