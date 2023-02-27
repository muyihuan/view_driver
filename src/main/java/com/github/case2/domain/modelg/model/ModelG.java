package com.github.case2.domain.modelg.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yanghuan
 */
@Getter
@Setter
public class ModelG {

    /**
     * ModelG的ID
     */
    private Long id;

    /**
     * 基础属性 ga
     */
    private String innerAttributeGa;

    /**
     * 基础属性 gb
     */
    private String innerAttributeGb;
}
