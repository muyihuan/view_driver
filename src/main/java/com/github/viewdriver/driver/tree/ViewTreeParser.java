package com.github.viewdriver.driver.tree;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.github.case2.view.ViewA;
import com.github.viewdriver.driver.exception.MetaDataIsNullException;
import com.github.viewdriver.driver.exception.NotViewException;
import com.github.viewdriver.driver.exception.ParamIsNullException;
import com.github.viewdriver.driver.metadata.ViewDriverMetaData;

import java.lang.reflect.Type;
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

    private static final Map<Class<?>, ViewTree> view_tree_cache = new ConcurrentHashMap<>();
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
        viewTree.rootNode = buildViewTreeNode(rootView, 0, null, false, right);

        view_tree_cache.put(rootView, viewTree);

        return viewTree;
    }

    /**
     * 构建视图树节点，通过注册的元数据进行推导.
     *
     * @param right 进入解析状态的.
     * @return 节点.
     */
    private ViewTreeNode buildViewTreeNode(Class<?> view, int type, ViewTreeNode parent, boolean isOneToN, Map<Class, ViewTreeNode> right) {
        if(view == null) {
            return null;
        }

        ViewTreeNode node = new ViewTreeNode();
        node.type = type;
        node.nodeClass = view;
        if(parent != null) {
            node.fromParentLine = new ViewTreeLine(parent, node, isOneToN);
        }

        // 进入解析状态
        right.put(view, node);

        List<ViewTreeNode> child_nodes = new ArrayList<>();
        Map<ViewTreeNode, Boolean> child_relations = new HashMap<>();
        List<BeanPropertyDefinition> properties = getAllProperties(view);
        for(BeanPropertyDefinition property : properties) {
            AnnotatedMethod getter = property.getGetter();
            Class<?> propertyType = getter.getAnnotated().getReturnType();
            boolean isArray = propertyType.isArray();
            boolean isCollection = propertyType.isAssignableFrom(Collection.class);
            if(isArray) {
                ArrayType arrayType = (ArrayType) jackson_helper.getTypeFactory().constructType(propertyType);
//                arrayType.getContentType().getClass();
                Class elementType = null;
                boolean isView = driverMeta.view_bind_model.containsKey(propertyType);
                if(isView) {
                    Class parent_model = driverMeta.view_bind_model.get(view);
                    Class child_model = driverMeta.view_bind_model.get(elementType);
                    if(child_model == null) {
                        ViewTreeNode childNode = buildViewTreeNode(propertyType, 0, node, true, right);
                        child_nodes.add(childNode);
                        child_relations.put(childNode, true);
                    }
                    else {
                        driverMeta.model_relation_by_outer_id.get(new ViewDriverMetaData.TwoModel(parent_model, child_model));
                    }
                }
                else {
                    ViewDriverMetaData.ViewAndGetter viewAndGetter = new ViewDriverMetaData.ViewAndGetter(view, getter.getName());
                    boolean isOuterAttribute = driverMeta.non_model_loader.containsKey(viewAndGetter);
                    if(isOuterAttribute) {
                        ViewTreeNode childNode = buildViewTreeNode(elementType, 1, node, true, right);
                        child_nodes.add(childNode);
                        child_relations.put(childNode, true);
                    }
                }
            }
            else if(isCollection) {

            }
            else {
                boolean isView = driverMeta.view_bind_model.containsKey(propertyType);
                if(isView) {
                    ViewTreeNode childNode = buildViewTreeNode(propertyType, 0, node, false, right);
                    child_nodes.add(childNode);
                    child_relations.put(childNode, false);
                }
                else {
                    ViewDriverMetaData.ViewAndGetter viewAndGetter = new ViewDriverMetaData.ViewAndGetter(view, getter.getName());
                    boolean isOuterAttribute = driverMeta.non_model_loader.containsKey(viewAndGetter);
                    if(isOuterAttribute) {
                        ViewTreeNode childNode = buildViewTreeNode(propertyType, 1, node, false, right);
                        child_nodes.add(childNode);
                        child_relations.put(childNode, false);
                    }
                }
            }
        }

        List<ViewTreeLine> toChildLines = new ArrayList<>();
        for(ViewTreeNode childNode : child_nodes) {
            toChildLines.add(new ViewTreeLine(parent, node, child_relations.get(childNode)));
        }
        node.toChildLines = toChildLines;

        return node;
    }

    /**
     * 获取该类的所有属性.
     * 1.排除 静态字段.
     * 2.排除 没有getter方法的字段.
     *
     * @return 属性列表.
     */
    private static List<BeanPropertyDefinition> getAllProperties(Class<?> classType) {
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

    public static void main(String[] args) {
        List<BeanPropertyDefinition> properties = getAllProperties(ViewA.class);
        for(BeanPropertyDefinition property : properties) {
            AnnotatedMethod getter = property.getGetter();
            Class<?> propertyType = getter.getAnnotated().getReturnType();
            JavaType javaType = jackson_helper.getTypeFactory().constructType(propertyType);
//            ()Ljava/util/List<Lcom/github/case2/view/ViewD;>;
            String cn = getter.getAnnotated().getGenericReturnType().getTypeName();
            boolean isArray = propertyType.isArray();
            if(isArray) {
                ArrayType arrayType = (ArrayType) jackson_helper.getTypeFactory().constructType(propertyType);
                String kk = arrayType.getContentType().getTypeName();
                assert kk == null;
            }
            assert javaType == null;
        }
    }
}
