package com.github.case1.domain.feed.model.ugc;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

/**
 * 转发UGC
 * @author yanghuan
 */
@Getter
@Setter
public class RepostUgcInfo {

    /**
     * 左转发链feedId
     */
    private LinkedList<Long> leftRepostChain;

    /**
     * 右转发链feedId
     */
    private LinkedList<Long> rightRepostChain;

    /**
     * 本feed的语音信息
     */
    private AudioContentInfo audio;

    /**
     * 本feed的图片信息
     */
    private ImageContentInfo image;
}
