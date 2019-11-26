package org.cp4j.core.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThread {

    private int threadCount = 1;
    private ExecutorService pool;
    public MultiThread(int threadCount) {
        this.threadCount = threadCount;
        pool = Executors.newFixedThreadPool(threadCount);
    }

    public void start(RepeatSoloTaskProvider provider, int concurrencyCount){
        for (int i = 0; i < concurrencyCount; i++) {
            pool.submit(provider.createTask());
        }

        pool.shutdown();

        /**
         * 线程数量：4070
         * 线程数量：4071
         * Exception in thread "main" java.lang.OutOfMemoryError: unable to create new native thread
         * 	at java.lang.Thread.start0(Native Method)
         * 	at java.lang.Thread.start(Thread.java:717)
         * 	at java.util.concurrent.ThreadPoolExecutor.addWorker(ThreadPoolExecutor.java:957)
         * 	at java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1367)
         * 	at java.util.concurrent.AbstractExecutorService.submit(AbstractExecutorService.java:112)
         * 	at com.danger.testHttp.MultiThread.start(MultiThread.java:17)
         * 	at com.danger.testHttp.MultiThreadHttpTest.main(MultiThreadHttpTest.java:15)
         * Java HotSpot(TM) 64-Bit Server VM warning: Exception java.lang.OutOfMemoryError occurred dispatching signal SIGINT to handler- the VM may need to be forcibly terminated
         * 线程数量：4071
         * 线程数量：4071
         * 线程数量：4071
         */
    }

}
