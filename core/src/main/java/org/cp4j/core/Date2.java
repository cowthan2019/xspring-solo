package org.cp4j.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Date2 {

    public static Date getDayStartTime(Date whichDay){
        Calendar c = Calendar.getInstance();
        c.setTime(whichDay);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public static Date getDayEndTime(Date whichDay){
        Calendar c = Calendar.getInstance();
        c.setTime(whichDay);
        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 19);
        return c.getTime();
    }

    public static Date addSeconds(Date from, int deltaSeconds){
        return new Date(from.getTime() + deltaSeconds * 1000);
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
    }

}
