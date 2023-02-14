package com.wb.zeus.tools.viewdriver.builder;

import com.wb.zeus.tools.viewdriver.lambda.FieldGetter;

import java.util.HashMap;
import java.util.Map;

/**
 * 绑定主键id和外键id对应的model.
 *
 * @author yanghuan
 */
public class IdBinder<M> {

    Map<FieldGetter<M, Object>, Class<?>> idBind = new HashMap<>();

    /**
     * 绑定主键id和外键id对应的model.
     * @param idGetter 主键或者外键id的get方法.
     * @param bindModel 主键或者外键id对应model.
     * @return OuterIdBinder
     */
    public IdBinder<M> bind(FieldGetter<M, Object> idGetter, Class<?> bindModel) {
        idBind.put(idGetter, bindModel);
        return this;
    }
}
