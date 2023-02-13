package com.wb.case1.domain.feed.model.ugc;

import lombok.Data;

/**
 * 奖牌信息
 * @author lishipeng
 */
@Data
public class PaintContentInfo {

    /**
     * 用户id
     */
    private String uid;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userIcon;

    /**
     * 画猜的封面图
     */
    private String imgIcon;

    /**
     * 画猜的时长
     */
    private int drawTime;

    /**
     * 画猜的题目id
     */
    private int answerId;

    /**
     * 画猜的答案
     */
    private String answer;

    /**
     * 答案提示，例："生活用品"
     */
    private String hint;
}
