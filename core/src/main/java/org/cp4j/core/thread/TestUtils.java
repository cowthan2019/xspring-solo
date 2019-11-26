package org.cp4j.core.thread;

import java.util.Random;

public class TestUtils {

    public static void sleep(long miliis){
        try {
            Thread.sleep(miliis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static final Random random = new Random(System.currentTimeMillis());

    public static int random(int includeMin, int includeMax){
        if(includeMin >= includeMax) throw new RuntimeException("非法范围");
        return random.nextInt(includeMax - includeMin + 1) + includeMin;
    }

    public static void test(String[] args){
        for (int i = 0; i < 100; i++) {
            System.out.println(random(5, 10));
        }
    }
}
