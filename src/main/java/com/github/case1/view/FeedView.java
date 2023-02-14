package com.github.case1.view;

import com.github.case1.domain.feed.model.property.AtInfo;
import com.github.case1.view.ugc.AudioView;
import com.github.case1.view.ugc.ImageView;
import com.github.case1.view.ugc.VideoView;
import lombok.Data;

import java.util.List;

/**
 * @author yanghuan
 */
@Data
public class FeedView {

    /**
     * feed的Id
     */
    private long id;

    /**
     * 客户端识别的feed类型，可能在版本兼容的情况下改变这个值
     */
    private int feedType;

    /**
     * 文字内容
     */
    private String title;

    /**
     * 作者相关信息 todo 需要通过feed的uid属性获取
     */
    private UserInfoView author;

    /**
     * 图片信息 todo 需要通过feed的MultiImageContentModel属性获取
     */
    private List<ImageView> imageList;

    /**
     * 语音信息 todo 需要通过feed的AudioContentModel属性获取
     */
    private AudioView audio;

    /**
     * 视频信息 todo 需要通过feed的VideoContentModel属性获取
     */
    private VideoView video;

    /**
     * 话题信息 todo 需要通过feed的tagIds属性获取
     */
    private List<TagView> tagList;

    /**
     * 转发根feed todo 需要通过feed的repost属性获取
     */
    private FeedView rootFeed;

    /**
     * @信息 todo 需要通过feed的tagIds属性获取
     */
    private List<AtInfo> atInfos;

    /**
     * 评论列表 todo 需要通过feed的id属性获取(需要过滤自见的评论)
     */
    private List<CommentView> comments;

    /**
     * 评论数量 todo 需要通过feed的id属性获取(需要过滤自见的评论)
     */
    private Integer commentCount;

    /**
     * feed背景图信息 todo 需要通过feed的extraInfo属性获取
     */
    private BackgroundView backgroundImgInfo;

    /**
     * 推荐标识相关 todo 需要通过feed的置顶状态等获取
     */
    private RecommendView recommendInfo;

    /**
     * 用户状态引流位信息 todo 需要通过feed的uid获取
     */
    private UserStatusDrainageView userStatusDrainageInfo;

    /**
     * feed的logo todo 需要通过feed的uid的获取
     */
    private List<String> logoList;

    /**
     * feed的作者直播入口 todo 需要通过feed的uid的获取
     */
    private LiveRoomView liveRoomInfo;
}
