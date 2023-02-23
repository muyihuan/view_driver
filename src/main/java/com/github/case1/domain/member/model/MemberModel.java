package com.github.case1.domain.member.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yanghuan
 */
@Getter
@Setter
public class MemberModel {

    /**
     * uid
     */
    private String uid;

    /**
     * 会员等级
     */
    private Integer memberLevel;
}
