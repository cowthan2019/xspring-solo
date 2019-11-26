package org.cp4j.core.utils;

import java.io.*;

public class LinuxUsageUtils {
    public static float getUsageCPU() {
        System.out.println("开始收集cpu使用率");
        float cpuUsage = 0;
        Process pro1,pro2;
        Runtime r = Runtime.getRuntime();
        try {
            String command = "cat /proc/stat";
            //第一次采集CPU时间
            long startTime = System.currentTimeMillis();
            pro1 = r.exec(command);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
            String line = null;
            long idleCpuTime1 = 0, totalCpuTime1 = 0;   //分别为系统启动后空闲的CPU时间和总的CPU时间
            while((line=in1.readLine()) != null){
                if(line.startsWith("cpu")){
                    line = line.trim();
                    System.out.println(line);
                    String[] temp = line.split("\\s+");
                    idleCpuTime1 = Long.parseLong(temp[4]);
                    for(String s : temp){
                        if(!s.equals("cpu")){
                            totalCpuTime1 += Long.parseLong(s);
                        }
                    }
                    System.out.println("IdleCpuTime: " + idleCpuTime1 + ", " + "TotalCpuTime" + totalCpuTime1);
                    break;
                }
            }
            in1.close();
            pro1.destroy();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                System.out.println("CpuUsage休眠时发生InterruptedException. " + e.getMessage());
                System.out.println(sw.toString());
            }
            //第二次采集CPU时间
            long endTime = System.currentTimeMillis();
            pro2 = r.exec(command);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
            long idleCpuTime2 = 0, totalCpuTime2 = 0;   //分别为系统启动后空闲的CPU时间和总的CPU时间
            while((line=in2.readLine()) != null){
                if(line.startsWith("cpu")){
                    line = line.trim();
                    System.out.println(line);
                    String[] temp = line.split("\\s+");
                    idleCpuTime2 = Long.parseLong(temp[4]);
                    for(String s : temp){
                        if(!s.equals("cpu")){
                            totalCpuTime2 += Long.parseLong(s);
                        }
                    }
                    System.out.println("IdleCpuTime: " + idleCpuTime2 + ", " + "TotalCpuTime" + totalCpuTime2);
                    break;
                }
            }
            if(idleCpuTime1 != 0 && totalCpuTime1 !=0 && idleCpuTime2 != 0 && totalCpuTime2 !=0){
                cpuUsage = 1 - (float)(idleCpuTime2 - idleCpuTime1)/(float)(totalCpuTime2 - totalCpuTime1);
                System.out.println("本节点CPU使用率为: " + cpuUsage);
            }
            in2.close();
            pro2.destroy();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            System.out.println("CpuUsage发生InstantiationException. " + e.getMessage());
            System.out.println(sw.toString());
        }
        return cpuUsage;
    }
    public static float getUsageMemory() {
        System.out.println("开始收集memory使用率");
        float memUsage = 0.0f;
        Process pro = null;
        Runtime r = Runtime.getRuntime();
        try {
            String command = "cat /proc/meminfo";
            pro = r.exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line = null;
            int count = 0;
            long totalMem = 0, freeMem = 0;
            while((line=in.readLine()) != null){
                System.out.println(line);
                String[] memInfo = line.split("\\s+");
                if(memInfo[0].startsWith("MemTotal")){
                    totalMem = Long.parseLong(memInfo[1]);
                }
                if(memInfo[0].startsWith("MemFree")){
                    freeMem = Long.parseLong(memInfo[1]);
                }
                memUsage = 1- (float)freeMem/(float)totalMem;
                System.out.println("本节点内存使用率为: " + memUsage);
                if(++count == 2){
                    break;
                }
            }
            in.close();
            pro.destroy();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            System.out.println("MemUsage发生InstantiationException. " + e.getMessage());
            System.out.println(sw.toString());
        }
        return memUsage;
    }

    public static float getUsageDisk() {
        System.out.println("开始收集磁盘IO使用率");
        float ioUsage = 0.0f;
        Process pro = null;
        Runtime r = Runtime.getRuntime();
        try {
            String command = "iostat -d -x";
            pro = r.exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line = null;
            int count =  0;
            while((line=in.readLine()) != null){
                if(++count >= 4){
//                  log.info(line);
                    String[] temp = line.split("\\s+");
                    if(temp.length > 1){
                        float util =  Float.parseFloat(temp[temp.length-1]);
                        ioUsage = (ioUsage>util)?ioUsage:util;
                    }
                }
            }
            if(ioUsage > 0){
                System.out.println("本节点磁盘IO使用率为: " + ioUsage);
                ioUsage /= 100;
            }
            in.close();
            pro.destroy();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            System.out.println("IoUsage发生InstantiationException. " + e.getMessage());
            System.out.println(sw.toString());
        }
        return ioUsage;
    }

    private final static float TotalBandwidth = 10;   //网口带宽,Mbps
    public static float getUsageNetwork() {
        try {
            return getUsageNetwork1();
        }catch (Exception e){
            return 0;
        }
    }
    private static float getUsageNetwork1() {
        System.out.println("开始收集网络带宽使用率");
        float netUsage = 0.0f;
        Process pro1,pro2;
        Runtime r = Runtime.getRuntime();
        try {
            String command = "cat /proc/net/dev";
            //第一次采集流量数据
            long startTime = System.currentTimeMillis();
            pro1 = r.exec(command);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
            String line = null;
            long inSize1 = 0, outSize1 = 0;
            while((line=in1.readLine()) != null){
                line = line.trim();
                if(line.startsWith("eth0")){
                    System.out.println(line);
                    String[] temp = line.split("\\s+");
                    inSize1 = Long.parseLong(temp[0].substring(5)); //Receive bytes,单位为Byte
                    outSize1 = Long.parseLong(temp[8]);             //Transmit bytes,单位为Byte
                    break;
                }
            }
            in1.close();
            pro1.destroy();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                System.out.println("NetUsage休眠时发生InterruptedException. " + e.getMessage());
                System.out.println(sw.toString());
            }
            //第二次采集流量数据
            long endTime = System.currentTimeMillis();
            pro2 = r.exec(command);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
            long inSize2 = 0 ,outSize2 = 0;
            while((line=in2.readLine()) != null){
                line = line.trim();
                if(line.startsWith("eth0")){
                    System.out.println(line);
                    String[] temp = line.split("\\s+");
                    inSize2 = Long.parseLong(temp[0].substring(5));
                    outSize2 = Long.parseLong(temp[8]);
                    break;
                }
            }
            if(inSize1 != 0 && outSize1 !=0 && inSize2 != 0 && outSize2 !=0){
                float interval = (float)(endTime - startTime)/1000;
                //网口传输速度,单位为bps
                float curRate = (float)(inSize2 - inSize1 + outSize2 - outSize1)*8/(1000000*interval);
                netUsage = curRate/TotalBandwidth;
                System.out.println("本节点网口速度为: " + curRate + "Mbps");
                System.out.println("本节点网络带宽使用率为: " + netUsage);
            }
            in2.close();
            pro2.destroy();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            System.out.println("NetUsage发生InstantiationException. " + e.getMessage());
            System.out.println(sw.toString());
        }
        return netUsage;
    }

}
