package cn.liyu.auth.utils;

import java.util.ArrayList;
import java.util.List;

import static cn.liyu.auth.constant.AuthConstant.NODE_PATH_SEP;

public class NodeUtils {

    /**
     * 获取path中的node集合
     *
     * @param path
     * @return
     */
    public static List<Integer> getPathNode(String path) {
        int i = 1, n = path.length();
        ArrayList<Integer> list = new ArrayList<>();

        while (i < n) {
            int indexOf = path.indexOf(NODE_PATH_SEP, i);
            if (indexOf > 0) {
                list.add(Integer.valueOf(path.substring(i, indexOf)));
            } else {
                break;
            }
            i = indexOf + 1;
        }
        return list;
    }

    /**
     * 修改子节点路径
     *
     * @param path    当前父节点路径
     * @param prePath 之前父节点路径
     * @param cPath   当前子节点path
     * @return
     */
    public static String getChildrenPath(String path, String prePath, String cPath) {
        String children = cPath;
        int i = Math.max(prePath.length() - 1, 0);
        int j = path.length() == 0 ? 0 : path.length() - 1;
        children = path.substring(0, j) + children.substring(i);
        return children;
    }


    /**
     * 拼接当前节点和当前节点的path
     *
     * @param parentPath 父节点path
     * @param parentId   父节点id
     * @return
     */
    public static String getPathFromParent(String parentPath, Integer parentId) {
        String path;
        if (parentPath.endsWith(NODE_PATH_SEP)) {
            path = parentPath + parentId + NODE_PATH_SEP;
        } else {
            path = NODE_PATH_SEP + parentId + NODE_PATH_SEP;
        }
        return path;
    }
}
