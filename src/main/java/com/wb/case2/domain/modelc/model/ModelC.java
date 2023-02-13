package com.wb.case2.domain.modelc.model;

import lombok.Getter;

/**
 * @author yanghuan
 */
@Getter
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
