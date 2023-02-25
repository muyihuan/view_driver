package com.github.viewdriver.case1.domain.feed.model.property;

import lombok.Getter;
import lombok.Setter;

/**
 * 艾特信息
 * @author edz
 */
@Getter
@Setter
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
