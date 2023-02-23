package com.github.case1.view.ugc;

import lombok.Getter;
import lombok.Setter;

/**
 * 音频信息
 */
@Getter
@Setter
public class AudioView {

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
}
