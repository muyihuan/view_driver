package com.github.views.domain.modela.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yanghuan
 */
@Getter
@Setter
public class ObjectA {

    /**
     * 属性 oa
     */
    private String oa;

    /**
     * 属性 ob
     */
    private String ob;

    public boolean equals(Object obj) {
       if(!(obj instanceof ObjectA)) {
           return false;
       }

        ObjectA objectA = ((ObjectA) obj);
       return objectA.oa.equals(this.oa) && objectA.ob.equals(this.ob);
    }
}
