package org.cp4j.core;

import java.util.*;

public class ListDiff {

    /**
     * 获取curlist有，但prelist没有的
     * 或者prelist有，curlist没有的
     */
    public static <T> List<T> diff(List<T> prelist, List<T> curlist) {
        List<T> diff = new ArrayList<T>();

        Map<T, Integer> map = new HashMap<>(curlist.size());
        for (T stu : curlist) {
            map.put(stu, 1);
        }
        for (T stu : prelist) {
            if (map.get(stu) != null) {
                map.put(stu, 2);
                continue;
            }
            diff.add(stu);
        }
        for (Map.Entry<T, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                diff.add(entry.getKey());
            }
        }
        return diff;
    }

    /**
     * 获取curlist有，但prelist没有的
     */
    public static <T> List<T> diffRight(List<T> prelist, List<T> curlist) {
        List<T> diff = new ArrayList<T>();

        Set<T> set = new HashSet<>(curlist.size());
        for (T stu : curlist) {
            set.add(stu);
        }
        for (T stu : prelist) {
            if(!set.contains(stu)) {
                diff.add(stu);
            }
        }
        return diff;
    }

    /**
     * 获取prelist有，但curlist没有的
     */
    public static <T> List<T> diffLeft(List<T> prelist, List<T> curlist) {
        return diffRight(curlist, prelist);
    }
}
