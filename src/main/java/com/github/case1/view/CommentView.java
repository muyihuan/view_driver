package com.github.case1.view;

import com.github.case1.view.ugc.ImageView;
import lombok.Data;

/**
 * 评论
 */
@Data
public class CommentView {

    /**
     * 评论ID
     */
    private long id;

    /**
     * 文本内容
     */
    private String content;

    /**
     * 作者相关信息  todo 需要通过comment的uid属性获取
     */
    private UserInfoView authorInfo;

    /**
     * 单图信息
     */
    private ImageView image;

    /**
     * 被回复的评论信息  todo 需要通过comment的repliedCommentId属性获取
     */
    private CommentView repliedComment;
}