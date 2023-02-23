package com.github.case1.domain.tag.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yanghuan
 */
@Getter
@Setter
public class TagModel {

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
