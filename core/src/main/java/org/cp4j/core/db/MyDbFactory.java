package org.cp4j.core.db;


import org.cp4j.core.AssocArray;
import org.cp4j.core.CoreException;

import java.util.HashSet;
import java.util.Set;

public class MyDbFactory {

    private static AssocArray configs = AssocArray.array();
    private static AssocArray dbs = AssocArray.array();

    public static void addConfig(String name, AssocArray config){
        dbNames.add(name);
        configs.add(name, config);
    }

    public static AssocArray getConfigs(){
        return configs;
    }

    public static Set<String> getDbNames(){
        return dbNames;
    }

    public static Set<String> dbNames = new HashSet<>();

    public static MyDb getDb(String name){
        if(!configs.containsKey(name)){
            CoreException.raise(String.format("没有找到配置为%s的数据库配置", name));
        }
//        boolean hasInstance = ;
        if(!dbs.containsKey(name)){
            synchronized (MyDbFactory.configs){
                if(!dbs.containsKey(name)){
                    System.out.println("create MyDb--" + name);
                    dbs.put(name, new MyDb(configs.getObject(name, AssocArray.array())));
                }
            }
        }
        return dbs.getObject(name, null);
    }

}
