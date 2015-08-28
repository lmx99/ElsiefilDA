package com.boshu.utils;

import java.io.File;

/**
 * Created by amou on 27/8/2015.
 */
public class FileUtils {
    public static void createDirectory(String path) throws Exception {

        try {
            // 获得文件对象
            File f = new File(path);
            if (!f.exists()) {
                // 如果路径不存在,则创建
                f.mkdirs();
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
