package com.github.case2.domain.modele.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yanghuan
 */
@Getter
@Setter
public class ModelE {

    /**
     * ModelA的ID
     */
    private Long modelAId;

    /**
     * 基础属性 ea
     */
    private String innerAttributeEa;

    /**
     * 基础属性 eb
     */
    private String innerAttributeEb;
}
