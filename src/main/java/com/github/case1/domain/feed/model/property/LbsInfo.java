package com.github.case1.domain.feed.model.property;

import lombok.Getter;
import lombok.Setter;

/**
 * 坐标信息
 * @author yanghuan
 */
@Getter
@Setter
public class LbsInfo {

    /**
     * 经度
     */
    private Double lo;

    /**
     * 纬度
     */
    private Double la;
}
