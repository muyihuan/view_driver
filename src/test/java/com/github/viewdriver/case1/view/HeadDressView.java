package com.github.viewdriver.case1.view;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户头像装扮信息
 */
@Getter
@Setter
public class HeadDressView {

    /**
     * 头像装扮url地址
     */
    private String dressUrl;

    /**
     * 头像装扮排序
     */
    private Integer dressSort;

    /**
     * 头像装扮url类型 0：图片 1：svga动效
     */
    private Integer urlType;

    /**
     * 头像装扮类型 0："普通"  1：亲密度 2：守护 3：会员 4：avatar帽子
     */
    private Integer dressType;
}
