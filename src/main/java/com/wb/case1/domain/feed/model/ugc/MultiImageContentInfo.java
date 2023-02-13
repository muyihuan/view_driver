package com.wb.case1.domain.feed.model.ugc;

import lombok.Data;

import java.util.List;

/**
 * 奖牌信息
 * @author lishipeng
 */
@Data
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
