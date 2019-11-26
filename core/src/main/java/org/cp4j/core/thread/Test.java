package org.cp4j.core.thread;

import org.cp4j.core.IoUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Test {
    public static void test(String[] args){
        DataProcessCenter dataProcessCenter = new MyDataCenter();
        SoloTask soloTask = new ImsChatSoloTask(dataProcessCenter);
        MultiThread multiThread = new MultiThread(70);
        multiThread.start(new RepeatSoloTaskProvider() {
            @Override
            public RepeatSoloTask createTask() {
                return new RepeatSoloTask(soloTask, 2000);
            }
        }, 70);
    }


    public static class MyDataCenter extends DataProcessCenter{

        private Set<String> threads = new HashSet<>();
        private List<String> error = new ArrayList<>();

        @Override
        public void processData(Data data) {
            String path = "/Users/cowthan/Desktop/log/log.txt";
            if(data.isOut){
                String info = "";
                if(data.error == null){
                    //{"sleep":880,"cost":880,"thread":"http-nio-9020-exec-21","client_cost":1023}
//                    AssocArray result = JsonUtils.getBean(data.data, AssocArray.class);
//                System.out.println(data.data);
//                    String thread = result.getString("thread", "");
//                    long client_cost = result.getLong("client_cost", 0L);
//                    threads.add(thread);
//                    info = thread;

                    IoUtils.file_append_content(path, data.data + "\n");
                }else{
//                    error.add(data.error.getMessage());
//                    info = data.error.getMessage();
                    IoUtils.file_append_content(path, data.error.getMessage() + "\n");
                }
//                System.out.println(info + "---线程数量：" + threads.size() + "， 错误数量：" + error.size());
            }
        }
    }
}
