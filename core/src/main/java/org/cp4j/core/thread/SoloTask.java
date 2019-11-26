package org.cp4j.core.thread;

/**
 * 单线程下的请求
 */
public abstract class SoloTask implements Runnable{

    private DataProcessCenter dataProcessCenter;

    public DataProcessCenter getDataProcessCenter() {
        return dataProcessCenter;
    }

    public SoloTask(DataProcessCenter dataProcessCenter) {
        this.dataProcessCenter = dataProcessCenter;
    }
}
