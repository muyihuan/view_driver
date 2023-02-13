package com.wb.case1.view;

import lombok.Data;

/**
 * 用户社交状态信息
 * @author yanghuan
 */
@Data
public class UserSocialStatusView {

    /**
     * 用户ID
     */
    private String uid;

    /**
     * 状态图标
     */
    private String stateIcon;
}
