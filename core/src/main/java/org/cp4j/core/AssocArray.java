package org.cp4j.core;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class AssocArray extends HashMap<String, Object> {

    public static AssocArray array(){
        return new AssocArray();
    }

    public AssocArray add(String k, Object v){
        put(k, v);
        return this;
    }

    public Object getData(String k, Object defaultValue){
        if(containsKey(k)){
            return get(k);
        }
        return defaultValue;
    }

    public <T> T getObject(String k, T defaultValue){
        if(containsKey(k)){
            return (T)get(k);
        }
        return defaultValue;
    }

    public int getInt(String k, int defaultValue){
        Object v = get(k);
        if(v == null) return defaultValue;
        return Lang.toInt(v.toString());
    }
    public long getLong(String k, long defaultValue){
        Object v = get(k);
        if(v == null) return defaultValue;
        return Lang.toLong(v.toString());
    }

    public String getString(String k, String defaultValue){
        Object v = get(k);
        if(v == null) return defaultValue;
        return v.toString();
    }

    public void incrementInt(String key, int num){
        int current = 0;
        if(containsKey(key)){
            current = (int) get(key);
        }
        put(key, current + num);
    }
    public void incrementLong(String key, long num){
        long current = 0;
        if(containsKey(key) && get(key) != null){
            current = (long) get(key);
        }
        put(key, current + num);
    }

    public void each(Callback callback){
        if(!isEmpty()){
            forEach(new BiConsumer<String, Object>() {
                @Override
                public void accept(String s, Object o) {
                    callback.onEntry(s, o);
                }
            });
        }
    }

    public static interface Callback{
        void onEntry(String key, Object value);
    }
}
