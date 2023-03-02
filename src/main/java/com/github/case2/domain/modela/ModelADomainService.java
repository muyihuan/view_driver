package com.github.case2.domain.modela;

import com.github.case2.domain.modela.model.ModelA;
import com.github.case2.domain.modela.model.ObjectA;
import com.github.case2.domain.modela.model.ObjectB;

import java.util.*;

/**
 * modelA 领域.
 *
 * @author yanghuan
 */
public class ModelADomainService {

    private static Map<Long, ModelA> modelA_db = new HashMap<>();
    static {
        ModelA modelA = new ModelA();
        modelA.setId(1L);
        ObjectA objectInfo = new ObjectA();
        objectInfo.setOa("内部Object类型的Oa属性-1");
        objectInfo.setOb("内部Object类型的Ob属性-1");
        modelA.setInnerAttributeAa(objectInfo);
        modelA.setSourceModelAId(2L);
        modelA.setModelBId(1L);
        modelA.setModelDIdList(Arrays.asList(1L, 2L, 3L));
        ObjectB objectB = new ObjectB();
        objectB.setModelGId(1L);
        modelA.setObjectB(objectB);
        modelA_db.put(1L, modelA);

        ModelA modelA2 = new ModelA();
        modelA2.setId(2L);
        ObjectA objectInfo2 = new ObjectA();
        objectInfo2.setOa("内部Object类型的Oa属性-2");
        objectInfo2.setOb("内部Object类型的Ob属性-2");
        modelA2.setInnerAttributeAa(objectInfo2);
        modelA2.setSourceModelAId(null);
        modelA2.setModelBId(null);
        modelA2.setModelDIdList(Collections.singletonList(2L));
        modelA_db.put(2L, modelA2);
    }

    public Map<Long, ModelA> batchGetModelAs(List<Long> ids) {
        if(ids == null || ids.size() == 0) {
            return null;
        }

        Map<Long, ModelA> modelMap = new HashMap<>();
        ids.forEach(id -> {
            modelMap.put(id, modelA_db.get(id));
        });
        return modelMap;
    }

    public Map<Long, ObjectA> batchGetOuterObject(List<Long> ids) {
        if(ids == null || ids.size() == 0) {
            return null;
        }

        Map<Long, ObjectA> objectInfoMap = new HashMap<>();
        ObjectA objectInfo = new ObjectA();
        objectInfo.setOa("外部属性Oa-1");
        objectInfo.setOb("外部属性Ob-1");
        objectInfoMap.put(1L, objectInfo);
        return objectInfoMap;
    }
}
