package org.cp4j.core.thread;

public class RepeatSoloTask implements Runnable {

    private SoloTask task;

    private int repeatCount = 1;

    public RepeatSoloTask(SoloTask task, int repeatCount) {
        this.task = task;
        this.repeatCount = repeatCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < repeatCount; i++) {
            task.run();
        }
    }
}
