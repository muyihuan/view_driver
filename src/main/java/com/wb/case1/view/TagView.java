package com.wb.case1.view;

import lombok.Data;

/**
 * 话题信息
 */
@Data
public class TagView {

    /**
     * tag id
     */
    private long id;

    /**
     * tag name
     */
    private String name;

    /**
     * 0：未关注，1：已关注
     */
    private int followFlag;

    /**
     * 角标icon的url
     */
    private String markerUrl;

    /**
     * 是否是主话题
     */
    private Integer isMasterTag;
}
