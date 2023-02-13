package com.wb.case1.domain.member.model;

import lombok.Data;

/**
 * @author yanghuan
 */
@Data
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
