package com.wb.case1.view;

import lombok.Data;

/**
 * feed上logo信息
 * @author yanghuan
 */
@Data
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
