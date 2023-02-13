package com.wb.case2.domain.modelf.model;

import lombok.Getter;

/**
 * @author yanghuan
 */
@Getter
public class ModelF {

    /**
     * ModelB的ID
     */
    private Long modelBId;

    /**
     * 基础属性 fa
     */
    private Object innerAttributeFa;

    /**
     * 基础属性 fb
     */
    private Object innerAttributeFb;
}
