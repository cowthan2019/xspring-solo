package org.cp4j.core;

import java.util.ArrayList;

public class ListArray extends ArrayList<Object> {

    public static ListArray array(){
        return new ListArray();
    }

    public ListArray addOne(Object o){
        add(o);
        return this;
    }

}
