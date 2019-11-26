package com.danger.utils;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;

/**
 * 依赖于ffmpeg
 */
public class MediaUtils {
    private static final Logger log = LoggerFactory.getLogger(MediaUtils.class);

    /**
     * 获取时长
     *
     * @param file 音频/视频文件
     * @return
     */
    public static MediaVideoInfo getVideoMetadata(File file) {
        long duration = 0;
        try {
            Encoder encoder = new Encoder();
            MultimediaInfo info = encoder.getInfo(file);

            MediaVideoInfo videoInfo = new MediaVideoInfo();
            videoInfo.setDuration(info.getDuration());
            videoInfo.setFormat(info.getFormat());
            videoInfo.setW(info.getVideo().getSize().getWidth());
            videoInfo.setH(info.getVideo().getSize().getHeight());

            videoInfo.setSize(readFileSize(file));
            return videoInfo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Long readFileSize(File source) {
        FileChannel fc= null;
        Long size = 0L;
        try {
            @SuppressWarnings("resource")
            FileInputStream fis = new FileInputStream(source);
            fc= fis.getChannel();
            BigDecimal fileSize = new BigDecimal(fc.size());
            size = Long.parseLong(fileSize.toString());
            if (size > 0) {
                size = Long.parseLong(fileSize.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null!=fc){
                try{
                    fc.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return size;
    }

    /**
     * 获取路径
     *
     * @return
     * @throws Exception
     */
    private static String getFilePath() throws Exception {
        //获取当前文件的根路径
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if (!path.exists()) path = new File("");

        //盘符路径
        StringBuilder codeUrl = new StringBuilder();
        codeUrl.append(path.getAbsolutePath()).append("/static/video/");
        File file = new File(codeUrl.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        return codeUrl.toString();
    }
}
