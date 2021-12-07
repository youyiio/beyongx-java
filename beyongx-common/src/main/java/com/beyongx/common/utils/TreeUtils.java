package com.beyongx.common.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.beyongx.common.vo.Node;

/**
 * 树形结构解析
 */
public class TreeUtils {
    
    /**
     * 解析树型结构
     * 
     * @param nodeList 数据节点集合
     * @return 解析完成后的树状结果
     **/
    public static <T extends Node> List<T> parse(List<T> nodeList) {
        List<T> resultList = new ArrayList<>(64);
        
        //找出所有的页节点（没有child)
        List<T> leafList = nodeList.stream().filter(val -> !hasChild(nodeList, val.getId())).collect(Collectors.toList());
        leafList.forEach(val -> {
                    reverseRecursion(val, nodeList, resultList);
                });

        Set<T> set = new HashSet<>(32);
        set.addAll(resultList);

        resultList.clear();
        set.stream().forEach(val -> {
            resultList.add(val);
            recursion(val, nodeList);
        });

        List<T> sortedList = resultList.stream().sorted(Comparator.comparing(T::getSort)).collect(Collectors.toList());
        resultList.clear();

        return sortedList;
    }

    /**
     * 解析已知父节点的子树集合
     * 
     * @param nodeList 数据节点集合
     * @return 解析完成后的树状结果
     **/
    public static <T extends Node> List<T> parse(Integer pid, List<T> nodeList) {
        
        List<T> parentList = nodeList.stream().filter(vo -> vo.getPid().equals(pid)).collect(Collectors.toList());

        parentList.stream().forEach(vo -> {
            TreeUtils.recursion(vo, nodeList);
        });

        List<T> sortedList = parentList.stream().sorted(Comparator.comparing(T::getSort)).collect(Collectors.toList());
        parentList.clear();

        return sortedList;
    }

    /**
     * 倒序递归检索所有父节点,由下往上直到根节点(父辈集合)
     * 
     * @param node       末尾叶子节点
     * @param nodeList   数据节点集合
     * @param resultList 根节点集合
     * @return void
     **/
    private static <T extends Node> void reverseRecursion(T node, List<T> nodeList, List<T> resultList) {
        List<T> parentNodeList = nodeList.stream().filter(val -> val.getId().equals(node.getPid()))
                .collect(Collectors.toList());
        if (parentNodeList.size() != 0) {
            T parentNode = parentNodeList.get(0);
            reverseRecursion(parentNode, nodeList, resultList);
        } else {
            resultList.add(node);
        }
    }

    /**
     * 递归检索所有子节点，并构建树结构;
     * 
     * @param node     根节点
     * @param nodeList 数据节点集合
     * @return void
     **/
    private static <T extends Node> void recursion(T node, List<T> nodeList) {
        List<T> childNodeList = nodeList.stream().filter(val -> val.getPid().equals(node.getId()))
                .collect(Collectors.toList());
        if (childNodeList.size() != 0) {
            node.setChildren((List<Node>)childNodeList);
            childNodeList.stream().forEach(val -> {
                recursion(val, nodeList);
            });
        }
    }

    /**
     * 是否还存在父级元素,找出末尾叶子节点
     * 
     * @param node   叶子节点
     * @param menuId 节点标记
     * @return boolean true 存在, false 不存在
     **/
    private static <T extends Node> boolean hasChild(List<T> node, Object id) {
        return node.stream().anyMatch(val -> val.getPid().equals(id));
    }

}
