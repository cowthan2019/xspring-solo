package org.cp4j.core.time;

public class Clock {
    private long startTime = 0;
    private long cost;
    public void start(){
        startTime = System.currentTimeMillis();
        cost = 0;
    }

    public long stop(){
        cost = System.currentTimeMillis() - startTime;
        return cost;
    }

    public long getCost(){
        return cost;
    }
}
