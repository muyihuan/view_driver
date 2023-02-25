package com.github.viewdriver.case1.domain.feed.model.ugc;

import com.github.viewdriver.case1.domain.feed.model.FeedModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 转发更全面的信息
 * @author yanghuan
 */
@Getter
@Setter
public class RepostInfo {

    /**
     * 根feed不可以是转发类型feed
     */
    private FeedModel rootFeedSource;

    /**
     * 转发原始信息
     */
    private RepostUgcInfo repostUgcContent;
}
