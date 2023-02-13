package com.wb.case1.domain.comment.model;

import com.wb.case1.domain.feed.model.ugc.ImageContentInfo;
import lombok.Data;

/**
 * @author yanghuan
 */
@Data
public class CommentModel {

    /**
     * 评论ID
     */
    private long id;

    /**
     * 宿主ID
     */
    private Long topicId;

    /**
     * 宿主类型
     */
    private Integer topicType;

    /**
     * 作者相关信息
     */
    private String uid;

    /**
     * 文本内容
     */
    private String content;

    /**
     * 单图信息
     */
    private ImageContentInfo image;

    /**
     * 被回复的评论信息
     */
    private CommentModel repliedComment;
}
