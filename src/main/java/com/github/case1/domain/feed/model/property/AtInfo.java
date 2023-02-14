package com.github.case1.domain.feed.model.property;

import lombok.Data;

/**
 * 艾特信息
 * @author edz
 */
@Data
public class AtInfo {

    /**
     * at的用户id
     */
    private String uid;

    /**
     * at的用户名
     */
    private String userName;

    /**
     * 颜色，亲密度
     */
    private String atColor;

    /**
     * 亲密度关系类型
     */
    private int relationType;

    public AtInfo() {
    }

    public AtInfo(String uid, String userName) {
        this.uid = uid;
        this.userName = userName;
    }
}
