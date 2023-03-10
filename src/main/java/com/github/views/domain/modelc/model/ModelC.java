package com.github.views.domain.modelc.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yanghuan
 */
@Getter
@Setter
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
    private Long sourceModelCId;
}
