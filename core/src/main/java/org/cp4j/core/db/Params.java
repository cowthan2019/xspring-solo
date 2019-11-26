package org.cp4j.core.db;

import java.util.HashMap;

public final class Params extends HashMap<String, Object> {

    private Params(){}

    public static Params obtain(){
        return new Params();
    }

    public Params add(String key, Object v){
        put(key, v);
        return this;
    }
}
