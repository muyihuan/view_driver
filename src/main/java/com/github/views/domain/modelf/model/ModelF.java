package com.github.views.domain.modelf.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yanghuan
 */
@Getter
@Setter
public class ModelF {

    /**
     * ModelB的ID
     */
    private Long modelBId;

    /**
     * 基础属性 fa
     */
    private String innerAttributeFa;

    /**
     * 基础属性 fb
     */
    private String innerAttributeFb;
}
