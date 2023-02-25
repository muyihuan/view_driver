package com.github.viewdriver.case1.domain.state.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yanghuan
 */
@Getter
@Setter
public class StateModel {

    /**
     * 跳转地址
     */
    private String jumpUrl;

    /**
     * 用户当前状态描述 当前活跃/在游戏/看语音房
     */
    private String desc;

    /**
     * 类型 用于埋点
     * profile：个人页
     * live_room：语音房
     * game：游戏
     */
    private String type;

    /**
     * 游戏类型 用于埋点
     */
    private Integer gameTypeId;
}
