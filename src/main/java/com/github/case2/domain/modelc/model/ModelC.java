package com.github.case2.domain.modelc.model;

import lombok.Data;

/**
 * @author yanghuan
 */
@Data
public class ModelC {

    /**
     * id
     */
    private Long id;

    /**
     * ModelA的ID
     */
    private Long modelAId;

    /**
     * ModelB的ID
     */
    private Long modelBId;

    /**
     * 有依赖关系的 ModelC
     */
    private ModelC dependentModelC;
}
