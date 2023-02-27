package com.github.case2.domain.modela.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author yanghuan
 */
@Getter
@Setter
public class ModelA {

    /**
     * id
     */
    private Long id;

    /**
     * 基础属性 aa
     */
    private ObjectA innerAttributeAa;

    /**
     * 有依赖关系的 ModelA
     */
    private Long sourceModelAId;

    /**
     * ModelB的ID
     */
    private Long modelBId;

    /**
     * ModelG的id在更深的层级
     */
    private ObjectB objectB;

    /**
     * ModelD的ID集合
     */
    private List<Long> modelDIdList;
}
