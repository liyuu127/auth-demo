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
}
