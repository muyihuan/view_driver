package com.github.viewdriver.case1.domain.feed.model.ugc;

import lombok.Getter;
import lombok.Setter;

/**
 * 音频信息
 * @author wangsch
 */
@Getter
@Setter
public class AudioContentInfo {

    /**
     * 用户uid
     */
    private String uid;
    /**
     * url地址
     */
    private String audioUrl;
    /**
     * 长度，秒
     */
    private Integer audioLength;
    /**
     * 文件大小，字节
     */
    private Integer audioSize;

    /**
     * 语音气泡id
     * */
    private String audioPropId;

    /**
     * 是否是流文件
     */
    private Integer isStreamFile;

    /**
     * 语音描述
     */
    private String audioDesc;

    /**
     * 语音标题
     */
    private String audioTitle;

    /**
     * 语音封面
     */
    private String audioCover;
}
