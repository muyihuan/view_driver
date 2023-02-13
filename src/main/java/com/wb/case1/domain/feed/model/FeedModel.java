package com.wb.case1.domain.feed.model;

import com.wb.case1.domain.feed.enums.*;
import com.wb.case1.domain.feed.model.property.*;
import com.wb.case1.domain.feed.model.ugc.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Chenlei
 */
@Getter
@AllArgsConstructor
public class FeedModel {

    /**
     * feed的id
     */
    private Long feedId;

    /**
     * feed的作者
     */
    private String uid;

    /**
     * feed的内容类型
     */
    private FeedContentTypeEnum contentType;

    /**
     * feed的状态
     */
    private FeedStateEnum state;

    /**
     * 可见范围，是用户侧设置的可见范围，优先级小于系统可见范围
     */
    private FeedPrivilegeEnum privilege;

    /**
     * 系统可见范围，是系统侧设置的可见范围，优先级大于用户设置的可见范围
     */
    private FeedSystemPrivilegeEnum systemPrivilege;

    /**
     * feed对应的资源id
     */
    private Long sourceId;

    /**
     * feed对应的资源类型
     */
    private FeedSourceTypeEnum sourceType;

    /**
     * feed的中文字内容
     */
    private String textContent;

    /**
     * feed的创建时间
     */
    private Date feedCreateTime;

    /**
     * feed的坐标信息信息（经纬度）
     */
    private LbsInfo lbsInfo;

    /**
     * 是否是匿名feed，0：不是、1：是匿名feed
     */
    private Integer anonymousFeedType;

    /**
     * feed礼物面板配置
     */
    private Integer giftStrategyId;

    /**
     * 文字内容信息
     */
    private TextContentInfo text;

    /**
     * 话题信息
     */
    private List<Long> tagIds;

    /**
     * 艾特信息
     */
    private List<AtInfo> atInfoList;

    /**
     * 同类签信息
     */
    private LabelInfo labelInfo;

    /**
     * 词卡信息
     */
    private CardInfo cardInfo;

    /**
     * 场景标识别
     */
    private Integer sceneType;

    /**
     * 场景参数
     */
    private Map sceneParam;

    /**
     * 多图信息
     */
    private MultiImageContentInfo multiImage;

    /**
     * 网页链接信息
     */
    private LinkContentInfo webLink;

    /**
     * 画猜信息
     */
    private PaintContentInfo paint;

    /**
     * 视频feed
     */
    private VideoContentInfo video;

    /**
     * 转发信息
     */
    private RepostInfo repost;

    /**
     * 语音feed
     */
    private AudioContentInfo audio;

    /**
     * feed扩展
     */
    private ExtraInfo extraInfo;
}
