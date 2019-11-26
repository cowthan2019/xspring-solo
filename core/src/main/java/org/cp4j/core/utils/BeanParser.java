package org.cp4j.core.utils;

import org.cp4j.core.AssocArray;
import org.cp4j.core.JsonUtils;
import org.cp4j.core.Lang;
import org.cp4j.core.utils.test.TestModel;
import org.cp4j.core.utils.test.TestModel3;

import java.lang.reflect.Field;
import java.util.*;

public class BeanParser {

    public static final int NAME_MODE_LINE = 1;
    public static final int NAME_MODE_CAMEL = 2;
    public static final int NAME_MODE_DONT_CHANGE = 3;

    /**
     * 把obj的属性转换到一个Map里
     *
     * @param obj
     * @param nameForceToLineMode 放到Map里的key是否强制转成下划线模式
     * @param exclude             obj里哪些属性不需要转换
     * @return
     */
    public static AssocArray toMap(Object obj, boolean nameForceToLineMode, Set<String> exclude) {
        return toMap(obj, nameForceToLineMode, exclude, "");
    }

    public static AssocArray toMap(Object obj, boolean nameForceToLineMode) {
        return toMap(obj, nameForceToLineMode, null, "");
    }

    private static AssocArray toMap(Object obj, boolean nameForceToLineMode, Set<String> exclude, String parentField) {
        if (obj == null) return null;
        if (exclude == null) exclude = new HashSet<>();
        AssocArray row = AssocArray.array();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < Lang.count(fields); i++) {
            Field f = fields[i];
            f.setAccessible(true);
            String fieldName = f.getName();
            String key = nameForceToLineMode ? NameUtils.humpToLine2(fieldName) : fieldName;
            Class<?> fieldClass = f.getType();
            String fullFieldName = (Lang.isEmpty(parentField) ? "" : parentField + ".") + fieldName;

            if (exclude.contains(fullFieldName)) continue;

            try {
                Object value = f.get(obj);
                if (isPrimitive(fieldClass) || value == null) {
                    row.add(key, value);
                } else {
                    System.out.println(fullFieldName + " -- " + fieldClass.getSimpleName());
                    row.add(key, toMap(value, nameForceToLineMode, exclude, fullFieldName));
                }

            } catch (IllegalAccessException e) {
                throw new BeanParseException(e);
            }

        }
        return row;
    }

    private static AssocArray toMap2(Object obj, boolean nameForceToLineMode, Set<String> include){
        return toMap2(obj, nameForceToLineMode, include, "");
    }

    public static AssocArray newMap(Map<String, Object> map, int nameMode){
        if(map == null) return null;
        AssocArray r = AssocArray.array();
        for (String k: map.keySet()){
            String newKey = "";
            if(nameMode == NAME_MODE_DONT_CHANGE) newKey = k;
            if(nameMode == NAME_MODE_LINE) newKey = NameUtils.humpToLine2(k);
            if(nameMode == NAME_MODE_CAMEL) newKey = NameUtils.lineToHump(k);
            r.add(newKey, map.get(k));
        }
        return r;
    }

    private static AssocArray toMap2(Object obj, boolean nameForceToLineMode, Set<String> include, String parentField) {
        if (obj == null) return null;
        if (include == null) include = new HashSet<>();
        AssocArray row = AssocArray.array();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < Lang.count(fields); i++) {
            Field f = fields[i];
            f.setAccessible(true);
            String fieldName = f.getName();
            String key = nameForceToLineMode ? NameUtils.humpToLine2(fieldName) : fieldName;
            Class<?> fieldClass = f.getType();
            String fullFieldName = (Lang.isEmpty(parentField) ? "" : parentField + ".") + fieldName;

            if (!include.contains(fullFieldName)) continue;

            try {
                Object value = f.get(obj);
                if (isPrimitive(fieldClass) || value == null) {
                    row.add(key, value);
                } else {
                    System.out.println(fullFieldName + " -- " + fieldClass.getSimpleName());
                    row.add(key, toMap(value, nameForceToLineMode, include, fullFieldName));
                }

            } catch (IllegalAccessException e) {
                throw new BeanParseException(e);
            }

        }
        return row;
    }

    public static <T> T toObject(Class<T> clazz, Map<String, Object> from, Set<String> exclude) {
        return toObject(clazz, from, exclude, "");
    }
    public static <T> T map_to_object(Class<T> clazz, Map<String, Object> from, Set<String> exclude) {
        return toObject(clazz, from, exclude, "");
    }

    public static <T> T toObject(Class<T> clazz, Map<String, Object> from) {
        return toObject(clazz, from, null, "");
    }

    private static <T> T toObject(Class<T> clazz, Map<String, Object> from, Set<String> exclude, String parentField) {
        if (from == null) return null;
        if (exclude == null) exclude = new HashSet<>();
        T obj = null;

        try {
            obj = clazz.newInstance();
        } catch (Exception e) {
            throw new BeanParseException(e);
        }

        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < Lang.count(fields); i++) {
            Field f = fields[i];
            f.setAccessible(true);
            String fieldName = f.getName();
            String key1 = fieldName;
            String key2 = NameUtils.humpToLine2(fieldName);
            Class<?> fieldClass = f.getType();

            String fullFieldName = (Lang.isEmpty(parentField) ? "" : parentField + ".") + fieldName;
            if (exclude.contains(fullFieldName)) continue;

            try {
                Object value = from.get(key2);
                if (value == null) value = from.get(key1);

                if (value == null || isPrimitive(value.getClass())) {
                    if(value != null
                            && (value.getClass() == Long.class || value.getClass() == long.class)
                            && (f.getType() == Integer.class || f.getType() == int.class)){
                        Long v = (Long) value;
                        int v2 = Math.toIntExact(v);
                        f.set(obj, v2);
                    }else{
                        if(value != null){
                            f.set(obj, value);
                        }else{
                            // 从map里取出null就算了，保持默认值
                        }
                    }
                } else {
                    System.out.println(fullFieldName + "--" + value.getClass().getSimpleName());
                    if (value instanceof Map) {
                        Map row2 = (Map) value;
                        AssocArray row22 = AssocArray.array();
                        if (row2.size() == 0) {
                            f.set(obj, null);
                        } else {
                            for (Object k : row2.keySet()) {
                                row22.add(k.toString(), row2.get(k));
                            }
                            System.out.println(JsonUtils.toJsonPretty(row22));
                            f.set(obj, toObject(fieldClass, row22, exclude, fullFieldName));
                        }

                    } else {
                        f.set(obj, value);
                    }

                }

            } catch (IllegalAccessException e) {
                throw new BeanParseException(e);
            }

        }
        return obj;
    }

    public static <T> T newObject(Class<T> clazzDest, Object from, Set<String> excludeFromDest) {
        return newObject(clazzDest, from, excludeFromDest, "");
    }

    public static <T> T obj_to_obj(Class<T> clazzDest, Object from, Set<String> excludeFromDest){
        return newObject(clazzDest, from, excludeFromDest, "");
    }
    public static <T> List<T> obj_to_obj(Class<T> clazzDest, List<?> from, Set<String> excludeFromDest){
        List<T> list = new ArrayList<>();

        for (int i = 0; i < Lang.count(from); i++) {
            list.add(obj_to_obj(clazzDest, from.get(i), excludeFromDest));
        }

        return list;
    }
    public static <T> List<T> map_to_obj(Class<T> clazzDest, List<Map<String, Object>> from, Set<String> excludeFromDest){
        List<T> list = new ArrayList<>();

        for (int i = 0; i < Lang.count(from); i++) {
            list.add(map_to_object(clazzDest, from.get(i), excludeFromDest));
        }

        return list;
    }

    private static <T> T newObject(Class<T> clazzDest, Object from, Set<String> excludeFromDest, String parentField) {
        if (from == null) return null;
        if (excludeFromDest == null) excludeFromDest = new HashSet<>();
        T obj = null;

        try {
            obj = clazzDest.newInstance();
        } catch (Exception e) {
            throw new BeanParseException(e);
        }

        Field[] fields = clazzDest.getDeclaredFields();
        for (int i = 0; i < Lang.count(fields); i++) {
            Field fDest = fields[i];
            fDest.setAccessible(true);
            String fieldName = fDest.getName();
            String key1 = fieldName;
            String key2 = NameUtils.humpToLine2(fieldName);
            Class<?> fieldClass = fDest.getType();

            String fullFieldName = (Lang.isEmpty(parentField) ? "" : parentField + ".") + fieldName;
            if (excludeFromDest.contains(fullFieldName)) continue;

            try {

                Field fSrc = null;

                try {
                    fSrc = from.getClass().getDeclaredField(key1);
                } catch (Exception e) {
                    //ignore
                }

                if (fSrc == null) {
                    try {
                        fSrc = from.getClass().getDeclaredField(key2);
                    } catch (Exception e) {
                        //ignore
                    }
                }

                if (fSrc == null) {
//                    System.out.println(from.getClass().getSimpleName() + "--找不到field：" + fullFieldName);
                    continue;
                }
                fSrc.setAccessible(true);
                Object value = fSrc.get(from);

                // 浅复制
//                fDest.set(obj, value);

                // 深复制
                if (value == null || isPrimitive(fSrc.getType())) {

                    if(value != null && (value.getClass() == Long.class || value.getClass() == long.class)
                            && (fDest.getType() == Integer.class || fDest.getType() == int.class)){
                        Long v = (Long) value;
                        int v2 = Math.toIntExact(v);
                        fDest.set(obj, v2);
                    }else{
                        fDest.set(obj, value);
                    }

//                    fDest.set(obj, value);
                } else {
                    if (value instanceof Map) {
                        Map row2 = (Map) value;
                        AssocArray row22 = AssocArray.array();
                        if (row2.size() == 0) {
                            fDest.set(obj, null);
                        } else {
                            for (Object k : row2.keySet()) {
                                row22.add(k.toString(), row2.get(k));
                            }
                            fDest.set(obj, toObject(fieldClass, row22, null, ""));
                        }

                    } else {
                        fDest.set(obj, newObject(fieldClass, value, excludeFromDest, fullFieldName));
                    }

                }

            } catch (Exception e) {
                throw new BeanParseException(e);
            }

        }
        return obj;
    }


    public static boolean isPrimitive(Class<?> clazz) {
        if (clazz == Byte.class || clazz == byte.class) return true;
        if (clazz == Character.class || clazz == char.class) return true;
        if (clazz == Short.class || clazz == short.class) return true;
        if (clazz == Integer.class || clazz == int.class) return true;
        if (clazz == Long.class || clazz == long.class) return true;
        if (clazz == Boolean.class || clazz == boolean.class) return true;
        if (clazz == Float.class || clazz == float.class) return true;
        if (clazz == Double.class || clazz == double.class) return true;

        if (clazz == String.class) return true;
        if (clazz == Date.class) return true;

        return false;
    }

    public static void test(String[] args) {
        TestModel data = new TestModel();
        AssocArray row = toMap(data, true, Lang.newHashSet("ignore", "data.ignore"));
        String json = JsonUtils.toJsonPretty(row);
        System.out.println(json);

        data = toObject(TestModel.class, row, Lang.newHashSet("ignore", "data.ignore"));
        System.out.println(JsonUtils.toJsonPretty(data));

        TestModel3 data3 = newObject(TestModel3.class, data, null);
        System.out.println(JsonUtils.toJsonPretty(data3));
    }

}
