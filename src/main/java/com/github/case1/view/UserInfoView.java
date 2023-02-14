package com.github.case1.view;

import lombok.Data;

/**
 * 作者相关信息
 * @author yanghuan
 */
@Data
public class UserInfoView {

    /**
     * 用户ID
     */
    private String uid;

    /**
     * 作者的用户名
     */
    private String userName;

    /**
     * 作者头像
     */
    private String userIcon;

    /**
     * 作者头像
     */
    private String gender;

    /**
     * 会员等级
     */
    private Integer memberLevel;

    /**
     * 用户社交状态信息
     */
    private UserSocialStatusView userSocialStatusInfo;

    /**
     * 装扮信息
     */
    private HeadDressView headDress;
}
