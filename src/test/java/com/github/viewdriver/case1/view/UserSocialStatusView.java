package com.github.viewdriver.case1.view;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户社交状态信息
 * @author yanghuan
 */
@Getter
@Setter
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
