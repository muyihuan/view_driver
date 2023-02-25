package com.github.viewdriver.case1.view;

import lombok.Getter;
import lombok.Setter;

/**
 * feed上logo信息
 * @author yanghuan
 */
@Getter
@Setter
public class LogoInfoView {

    /**
     * logo 图片地址
     */
    private String logo;

    /**
     * 统一跳转
     */
    private String jumpUrl;

    /**
     * 业务区分
     */
    private String type;
}
