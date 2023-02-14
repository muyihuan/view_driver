package com.github.viewdriver.builder;

import com.github.viewdriver.lambda.FieldGetter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 绑定view和model的属性的对应关系.
 *
 * @author yanghuan
 */
public class FieldBinder<V, M> {

    Map<FieldGetter, FieldGetter> fieldGetterBind = new HashMap<>();
    Map<FieldGetter, Function> fieldDecorator = new HashMap<>();

    /**
     * 属性映射.
     * @param viewFieldGetter View属性get方法.
     * @param modelFieldGetter 对应Model属性get方法.
     * @return FieldBinder
     */
    public <O> FieldBinder<V, M> bind(FieldGetter<V, O> viewFieldGetter, FieldGetter<M, O> modelFieldGetter) {
        fieldGetterBind.put(viewFieldGetter, modelFieldGetter);
        return this;
    }

    /**
     * 属性映射.
     * @param viewFieldGetter View属性get方法.
     * @param modelFieldGetter 对应Model属性get方法.
     * @param decorator 转化器 => 对Model属性get方法获取的数据进行类型转换、脱敏等转化处理.
     * @return FieldBinder
     */
    public <O, I> FieldBinder<V, M> bind(FieldGetter<V, O> viewFieldGetter, FieldGetter<M, I> modelFieldGetter, Function<I, O> decorator) {
        fieldGetterBind.put(viewFieldGetter, modelFieldGetter);
        fieldDecorator.put(viewFieldGetter, decorator);
        return this;
    }
}
