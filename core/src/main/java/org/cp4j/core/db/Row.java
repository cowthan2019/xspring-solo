package org.cp4j.core.db;


import org.cp4j.core.AssocArray;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

public final class Row extends AssocArray {
    public int getInt(String key, int defaultValue){
        Object v = get(key);
        if(v == null) return defaultValue;
        if(v instanceof Integer) return (int) v;
        if(v instanceof String) {
            return Integer.parseInt((String)v);
        }else if(v instanceof BigInteger){
            return ((BigInteger) v).intValue();
        }if(v instanceof Long) {
            return Integer.parseInt(v + "");
        }
        return (Integer)v; //等着抛异常就行了，不要在这里catch
    }
    public Date getDate(String key, Date defaultValue){
        Object v = get(key);
        if(v == null) return defaultValue;
        if(v instanceof Timestamp){
            Timestamp t = (Timestamp) v;
            return new Date(t.getTime());
        }
        return (Date)v; //等着抛异常就行了，不要在这里catch
    }
    public double getDouble(String key, double defaultValue){
        Object v = get(key);
        if(v == null) return defaultValue;
        if(v instanceof BigDecimal){
            return ((BigDecimal)v).doubleValue();
        }
        return (Double) v; //等着抛异常就行了，不要在这里catch
    }
    public String getString(String key, String defaultValue){
        Object v = get(key);
        if(v == null) return defaultValue;
        return v.toString();
    }
    public Object getObject(String key, Object defaultValue){
        Object v = get(key);
        if(v == null) return defaultValue;
        return v;
    }
}
