package com.github.viewdriver.case1.view;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangqixiao
 * @createtime 2020-12-23 11:43
 * @description feed流推荐信息
 */
@Getter
@Setter
public class RecommendView {

    /**
     * 推荐标识文案
     */
    private String recommendTitle;

    /**
     * 推荐统一跳转地址
     */
    private String recommendUnifyJump;

    /**
     * 推荐夜间图标地址
     */
    private String recommendNightLogo;

    /**
     * 推荐日间图标地址
     */
    private String recommendDayLogo;

}
