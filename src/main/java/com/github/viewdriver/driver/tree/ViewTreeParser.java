package com.github.viewdriver.driver.tree;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.github.viewdriver.driver.exception.MetaDataIsNullException;
import com.github.viewdriver.driver.exception.NotViewException;
import com.github.viewdriver.driver.exception.ParamIsNullException;
import com.github.viewdriver.driver.metadata.ViewDriverMetaData;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 视图解析器.
 * .解析视图生成视图树
 * .根据视图树反向生成视图
 *
 * @author yanghuan
 */
public class ViewTreeParser {

    /**
     * 本地缓存解析过的视图.
     */
    private static final Map<Class<?>, ViewTree> view_tree_cache = new ConcurrentHashMap<>();

    /**
     * 使用jackson去处理类解析.
     */
    private final static ObjectMapper jackson_helper = new ObjectMapper();

    private final ViewDriverMetaData driverMeta;

    /**
     * Create a new instance.
     *
     * @param driverMeta 视图驱动元数据.
     */
    public ViewTreeParser(ViewDriverMetaData driverMeta) {
        this.driverMeta = driverMeta;
    }

    /**
     * 解析视图并生成视图树.
     * @param view 视图.
     * @return 视图树.
     */
    public ViewTree generateViewTree(Class<?> view) throws MetaDataIsNullException, NotViewException {
        if(view == null) {
            throw new ParamIsNullException("view 参数为空");
        }

        if(driverMeta == null) {
            throw new MetaDataIsNullException();
        }

        if(!driverMeta.view_bind_model.containsKey(view)) {
            throw new NotViewException();
        }

        ViewTree viewTree = view_tree_cache.get(view);
        if(viewTree != null) {
            return viewTree;
        }

        // 未解析的站左边.
        List<Class> left = new ArrayList<>();
        // 解析完的站右边.
        Map<Class, ViewTreeNode> right = new HashMap<>();

        left.add(view);

        // 解析开始
        while(left.size() > 0) {
        }

        viewTree = new ViewTree();
        view_tree_cache.put(view, viewTree);
        return viewTree;
    }

    /**
     * 获取该类的所有属性.
     * 1.排除 静态字段.
     * 2.排除 没有getter方法的字段.
     * @return 属性列表
     */
    private List<BeanPropertyDefinition> getAllProperties(Class<?> classType) {
        JavaType javaType = jackson_helper.getTypeFactory().constructType(classType);
        if(!(javaType instanceof SimpleType)) {
            throw new RuntimeException("不支持该种类型的视图 => " + classType.getName());
        }

        BeanDescription b = jackson_helper.getSerializationConfig().introspect(javaType);
        List<BeanPropertyDefinition> propertyDefinitions = b.findProperties();
        if(propertyDefinitions == null || propertyDefinitions.size() == 0) {
            return Collections.emptyList();
        }

        return propertyDefinitions.stream().filter(BeanPropertyDefinition::hasGetter).collect(Collectors.toList());
    }
}
