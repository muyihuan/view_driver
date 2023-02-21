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
import com.github.viewdriver.lambda.FieldGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 视图解析器.
 * .解析视图生成视图树.
 * .根据视图树反向生成视图.
 *
 * @author yanghuan
 */
public class ViewTreeParser {

    private static final Logger logger = LoggerFactory.getLogger(ViewTreeParser.class);
    private static final Map<Class<?>, ViewTree> view_tree_cache = new ConcurrentHashMap<>();
    private static final ObjectMapper jackson_helper = new ObjectMapper();
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
     *
     * @param rootView 视图.
     * @return 视图树.
     */
    public ViewTree generateViewTree(Class<?> rootView) throws MetaDataIsNullException, NotViewException {
        if(rootView == null) {
            throw new ParamIsNullException("view 参数为空");
        }

        if(driverMeta == null) {
            throw new MetaDataIsNullException();
        }

        if(!driverMeta.view_bind_model.containsKey(rootView)) {
            throw new NotViewException();
        }

        ViewTree viewTree = view_tree_cache.get(rootView);
        if(viewTree != null) {
            return viewTree;
        }

        viewTree = new ViewTree();

        Map<Class, ViewTreeNode> right = new HashMap<>();
        viewTree.rootNode = generateViewTreeNode(rootView, null,  0, null, false, right);

        view_tree_cache.put(rootView, viewTree);

        viewTree.draw_it();

        return viewTree;
    }

    /**
     * 构建视图树节点，通过注册的元数据进行推导.
     * todo 支持继承关系、基础属性、getter方法.
     *
     * @param nodeClass 节点的Class类型.
     * @param type 节点类型.
     * @param parent 父节点.
     * @param isOneToN 父节点与本节点的关系是否是1:n.
     * @param __right 进入解析状态列表.
     * @return 节点.
     */
    private ViewTreeNode generateViewTreeNode(Class<?> nodeClass, Method getter, int type, ViewTreeNode parent, boolean isOneToN, Map<Class, ViewTreeNode> __right) {
        if(nodeClass == null) {
            return null;
        }

        // 已经进入解析状态的不需要再构建，防止循环依赖.
        if(__right.get(nodeClass) != null) {
            return __right.get(nodeClass);
        }

        ViewTreeNode node = new ViewTreeNode();
        node.type = type;
        node.nodeClass = nodeClass;
        node.parentGetter = getter;
        if(parent != null) {
            node.fromParentLine = new ViewTreeLine(parent, node, isOneToN);
        }

        // 进入解析状态.
        __right.put(nodeClass, node);

        List<ViewTreeNode> child_nodes = new ArrayList<>();
        Map<ViewTreeNode, Boolean> child_relations = new HashMap<>();
        List<BeanPropertyDefinition> properties = getAllProperties(nodeClass);
        for(BeanPropertyDefinition property : properties) {
            Method _getter = property.getGetter().getAnnotated();
            Class<?> propertyType = _getter.getReturnType();
            boolean isArray = propertyType.isArray();
            boolean isCollection = Collection.class.isAssignableFrom(propertyType);
            if(isArray) {
                Class componentType = propertyType.getComponentType();
                boolean isView = driverMeta.view_bind_model.containsKey(componentType);
                if(isView) {
                    Class parent_model = driverMeta.view_bind_model.get(nodeClass);
                    Class child_model = driverMeta.view_bind_model.get(componentType);
                    if(child_model == null) {
                        ViewTreeNode childNode = generateViewTreeNode(componentType, _getter,  0, node, true, __right);
                        child_nodes.add(childNode);
                        child_relations.put(childNode, true);
                    }
                    else {
                        FieldGetter outer_id_getter = driverMeta.model_relation_by_outer_id.get(new ViewDriverMetaData.TwoModel(parent_model, child_model));
                        if(outer_id_getter != null) {
                            Class outer_id_getter_return_type = outer_id_getter.getReturnType();
                            boolean outerIdGetterIsArray = outer_id_getter_return_type.isArray();
                            boolean outerIdGetterIsCollection = Collection.class.isAssignableFrom(outer_id_getter_return_type);
                            if(outerIdGetterIsArray || outerIdGetterIsCollection) {
                                ViewTreeNode childNode = generateViewTreeNode(componentType, _getter, 0, node, false, __right);
                                child_nodes.add(childNode);
                                child_relations.put(childNode, false);
                            }
                            else {
                                logger.info("解析视图遇到无法处理情况，无法推算两者关系 父视图 = " + nodeClass.getName() + "，子视图 = " + componentType.getName());
                            }
                        }
                        else {
                            outer_id_getter = driverMeta.model_relation_by_outer_id.get(new ViewDriverMetaData.TwoModel(child_model, parent_model));
                            if(outer_id_getter != null) {
                                Class outer_id_getter_return_type = outer_id_getter.getReturnType();
                                boolean outerIdGetterIsArray = outer_id_getter_return_type.isArray();
                                boolean outerIdGetterIsCollection = Collection.class.isAssignableFrom(outer_id_getter_return_type);
                                if(outerIdGetterIsArray || outerIdGetterIsCollection) {
                                    logger.info("解析视图遇到无法处理情况，无法推算两者关系 父视图 = " + nodeClass.getName() + "，子视图 = " + componentType.getName());
                                }
                                else {
                                    ViewTreeNode childNode = generateViewTreeNode(componentType, _getter, 0, node, true, __right);
                                    child_nodes.add(childNode);
                                    child_relations.put(childNode, true);
                                }
                            }
                            else {
                                logger.info("解析视图遇到无法处理情况，无法推算两者关系 父视图 = " + nodeClass.getName() + "，子视图 = " + componentType.getName());
                            }
                        }
                    }
                }
                else {
                    ViewDriverMetaData.ViewAndGetter viewAndGetter = new ViewDriverMetaData.ViewAndGetter(nodeClass, _getter.getName());
                    boolean isOuterAttribute = driverMeta.non_model_loader.containsKey(viewAndGetter);
                    if(isOuterAttribute) {
                        ViewTreeNode childNode = generateViewTreeNode(componentType, _getter, 1, node, true, __right);
                        child_nodes.add(childNode);
                        child_relations.put(childNode, true);
                    }
                }
            }
            else if(isCollection) {
                Class elementType = Object.class;
                Type genericReturnType = _getter.getGenericReturnType();
                if(genericReturnType != null) {
                    String typeName = genericReturnType.getTypeName();
                    if(typeName != null && !"".equals(typeName)) {
                        typeName = typeName.substring(typeName.indexOf("<") + 1, typeName.indexOf(">"));
                        try {
                            elementType = Class.forName(typeName);
                        } catch (Exception e) {
                            logger.error("Class.forName error typeName = " + typeName);
                        }
                    }
                }

                boolean isView = driverMeta.view_bind_model.containsKey(elementType);
                if(isView) {
                    Class parent_model = driverMeta.view_bind_model.get(nodeClass);
                    Class child_model = driverMeta.view_bind_model.get(elementType);
                    if(child_model == null) {
                        ViewTreeNode childNode = generateViewTreeNode(elementType, _getter, 0, node, true, __right);
                        child_nodes.add(childNode);
                        child_relations.put(childNode, true);
                    }
                    else {
                        FieldGetter outer_id_getter = driverMeta.model_relation_by_outer_id.get(new ViewDriverMetaData.TwoModel(parent_model, child_model));
                        if(outer_id_getter != null) {
                            Class outer_id_getter_return_type = outer_id_getter.getReturnType();
                            boolean outerIdGetterIsArray = outer_id_getter_return_type.isArray();
                            boolean outerIdGetterIsCollection = Collection.class.isAssignableFrom(outer_id_getter_return_type);
                            if(outerIdGetterIsArray || outerIdGetterIsCollection) {
                                ViewTreeNode childNode = generateViewTreeNode(elementType, _getter, 0, node, false, __right);
                                child_nodes.add(childNode);
                                child_relations.put(childNode, false);
                            }
                            else {
                                logger.info("解析视图遇到无法处理情况，无法推算两者关系 父视图 = " + nodeClass.getName() + "，子视图 = " + elementType.getName());
                            }
                        }
                        else {
                            outer_id_getter = driverMeta.model_relation_by_outer_id.get(new ViewDriverMetaData.TwoModel(child_model, parent_model));
                            if(outer_id_getter != null) {
                                Class outer_id_getter_return_type = outer_id_getter.getReturnType();
                                boolean outerIdGetterIsArray = outer_id_getter_return_type.isArray();
                                boolean outerIdGetterIsCollection = Collection.class.isAssignableFrom(outer_id_getter_return_type);
                                if(outerIdGetterIsArray || outerIdGetterIsCollection) {
                                    logger.info("解析视图遇到无法处理情况，无法推算两者关系 父视图 = " + nodeClass.getName() + "，子视图 = " + elementType.getName());
                                }
                                else {
                                    ViewTreeNode childNode = generateViewTreeNode(elementType, _getter, 0, node, true, __right);
                                    child_nodes.add(childNode);
                                    child_relations.put(childNode, true);
                                }
                            }
                            else {
                                logger.info("解析视图遇到无法处理情况，无法推算两者关系 父视图 = " + nodeClass.getName() + "，子视图 = " + elementType.getName());
                            }
                        }
                    }
                }
                else {
                    ViewDriverMetaData.ViewAndGetter viewAndGetter = new ViewDriverMetaData.ViewAndGetter(nodeClass, _getter.getName());
                    boolean isOuterAttribute = driverMeta.non_model_loader.containsKey(viewAndGetter);
                    if(isOuterAttribute) {
                        ViewTreeNode childNode = generateViewTreeNode(elementType, _getter, 1, node, true, __right);
                        child_nodes.add(childNode);
                        child_relations.put(childNode, true);
                    }
                }
            }
            else {
                boolean isView = driverMeta.view_bind_model.containsKey(propertyType);
                if(isView) {
                    ViewTreeNode childNode = generateViewTreeNode(propertyType, _getter, 0, node, false, __right);
                    child_nodes.add(childNode);
                    child_relations.put(childNode, false);
                }
                else {
                    ViewDriverMetaData.ViewAndGetter viewAndGetter = new ViewDriverMetaData.ViewAndGetter(nodeClass, _getter.getName());
                    boolean isOuterAttribute = driverMeta.non_model_loader.containsKey(viewAndGetter);
                    if(isOuterAttribute) {
                        ViewTreeNode childNode = generateViewTreeNode(propertyType, _getter, 1, node, false, __right);
                        child_nodes.add(childNode);
                        child_relations.put(childNode, false);
                    }
                }
            }
        }

        boolean is_depend_self = false;
        List<ViewTreeLine> to_child_lines = new ArrayList<>();
        List<Method> depend_self_getters = new ArrayList<>();
        for (ViewTreeNode childNode : child_nodes) {
            to_child_lines.add(new ViewTreeLine(node, childNode, child_relations.get(childNode)));

            if (node == childNode) {
                is_depend_self = true;
                depend_self_getters.add(childNode.parentGetter);
            }
        }
        node.toChildLines = to_child_lines;
        node.isDependSelf = is_depend_self;

        return node;
    }

    /**
     * 获取该类的所有属性.
     * 1.排除 静态字段.
     * 2.排除 没有getter方法的字段.
     *
     * @return 属性列表.
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
