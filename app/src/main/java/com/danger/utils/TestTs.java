package com.danger.utils;

import org.cp4j.core.IoUtils;
import org.cp4j.core.Lang;
import org.cp4j.core.MyHttp;
import org.cp4j.core.http.AyoResponse;

import java.io.File;

public class TestTs {

    public static void main(String[] args) {

        String dir = "/Users/cowthan/Documents/downloader/";
        String totalFile = "/Users/cowthan/Documents/downloader/total.mp4";
        int start = 524000;
        String url = "https://video1.91pandian.com/20191011/VHtZjDLD/500kb/hls/nZdzA4" + 524000 + ".ts";

        for (int i = 0; i < 10000; i++) {
            int index = start+i;
            url = "https://video1.91pandian.com/20191011/VHtZjDLD/500kb/hls/nZdzA4" + index + ".ts";
            System.out.println("下载：" + url);
            File outFile = new File(dir + "ts" + index + ".ts");
            AyoResponse r = MyHttp.getEmitter().download(url, outFile, null);
            if(r.failInfo != null) {
                System.out.println("下载失败：" + r.failInfo.code + "--" + r.failInfo.reason);
                break;
            }

            byte[] content = IoUtils.readBytes(IoUtils.fromFile(outFile));
            Lang.file_append_content(totalFile, content);
            outFile.delete();
        }

    }

}
