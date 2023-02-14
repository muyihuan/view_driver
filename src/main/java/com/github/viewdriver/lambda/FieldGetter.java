package com.github.viewdriver.lambda;

import lombok.SneakyThrows;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * getter方法的lambda表达式，可以获取表达式对应的类、方法、属性等.
 *
 * @author yanghuan
 */
public interface FieldGetter<T, R> extends Function<T, R>, Serializable {

    /**
     * 获取getter对应的类名称.
     * @return 类的全路径.
     */
    @SneakyThrows
    default String getClassName() {
        return getSerializedLambda().getImplClass().replace('/', '.');
    }

    /**
     * 获取getter对应的方法名称.
     * @return 方法名称.
     */
    @SneakyThrows
    default String getMethodName() {
        return getSerializedLambda().getImplMethodName();
    }

    /**
     * 获取getter对应的属性名称.
     * @return 属性名称.
     */
    @SneakyThrows
    default String getFieldName() {
        String fieldName;

        String methodName = getMethodName();
        if (methodName.startsWith("get")) {
            fieldName = methodName.substring(3);
        }
        else {
            fieldName = methodName;
        }
        return fieldName.toLowerCase();
    }

    /**
     * 获取lambda表达式的元信息.
     * @return 表达式的相关信息.
     */
    @SneakyThrows
    default SerializedLambda getSerializedLambda() {
        Method method = getClass().getDeclaredMethod("writeReplace");
        method.setAccessible(true);
        return (SerializedLambda) method.invoke(this);
    }
}
