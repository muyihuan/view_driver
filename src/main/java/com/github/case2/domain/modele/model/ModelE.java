package com.github.case2.domain.modele.model;

import lombok.Data;

/**
 * @author yanghuan
 */
@Data
public class ModelE {

    /**
     * ModelA的ID
     */
    private Long modelAId;

    /**
     * 基础属性 ea
     */
    private Object innerAttributeEa;

    /**
     * 基础属性 eb
     */
    private Object innerAttributeEb;
}
