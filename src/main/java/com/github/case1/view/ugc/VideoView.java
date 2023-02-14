package com.github.case1.view.ugc;

import lombok.Data;

/**
 * @author yanghuan
 */
@Data
public class VideoView {

    /**
     * 播放url
     */
    private String playUrl;

    /**
     * 视频长度，单位表
     */
    private int videoLength;
}
