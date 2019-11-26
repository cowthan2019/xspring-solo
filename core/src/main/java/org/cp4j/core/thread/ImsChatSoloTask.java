package org.cp4j.core.thread;


import org.cp4j.core.JsonUtils;

public class ImsChatSoloTask extends SoloTask{
    public ImsChatSoloTask(DataProcessCenter dataProcessCenter) {
        super(dataProcessCenter);
    }

    @Override
    public void run() {
        try {
            /**
             * 这里可以根据业务模式请求一系列接口，注意模拟真正的线上场景
             */

            String result = "";
            getDataProcessCenter().addResult(System.currentTimeMillis(), JsonUtils.toJson(result), null);

        }catch (Exception e){
            e.printStackTrace();
            getDataProcessCenter().addResult(System.currentTimeMillis(), null, e);
        }

    }

}
