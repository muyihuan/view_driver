package com.github.case1.domain.feed.model.ugc;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 奖牌信息
 * @author lishipeng
 */
@Getter
@Setter
public class MultiImageContentInfo {

    /**
     * 作者的uid
     */
    private String sourceUid;

    /**
     * 图片列表
     */
    private List<ImageContentInfo> imageList;
}
