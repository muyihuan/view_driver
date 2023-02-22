package com.github.case2.domain.modela.model;

import lombok.Data;

import java.util.List;

/**
 * @author yanghuan
 */
@Data
public class ModelA {

    /**
     * id
     */
    private Long id;

    /**
     * 基础属性 aa
     */
    private ObjectInfo innerAttributeAa;

    /**
     * 有依赖关系的 ModelA
     */
    private ModelA dependentModelA;

    /**
     * ModelB的ID
     */
    private Long modelBId;

    /**
     * ModelD的ID集合
     */
    private List<Long> modelDIdList;
}
