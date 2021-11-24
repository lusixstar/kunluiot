package com.kunluiot.sdk.util;


import com.kunluiot.sdk.util.log.KunLuLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {

    public static boolean unzipFile(String zipPath) {
        try {

            String tempFileName = new File(zipPath).getParent();
            // 创建解压目标目录
            File file = new File(tempFileName);
            // 如果目标目录不存在，则创建
            if (!file.exists()) {
                file.mkdirs();
            }
            // 打开压缩文件
            InputStream inputStream = new FileInputStream(zipPath);

            ZipInputStream zipInputStream = new ZipInputStream(inputStream);

            // 读取一个进入点
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            // 使用1Mbuffer
            byte[] buffer = new byte[1024 * 1024];
            // 解压时字节计数
            int count = 0;
            // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
            while (zipEntry != null) {
//                Log.e("whh0927", "解压文件 入口 1： " + zipEntry);
                if (!zipEntry.isDirectory()) {  //如果是一个文件
                    // 如果是文件
                    String fileName = zipEntry.getName();
//
//                    Log.e("whh0927", "解压文件 的名字： " + fileName);
                    file = new File(tempFileName + File.separator + fileName);  //放到新的解压的文件路径
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    KunLuLog.INSTANCE.e("new file path = " + file);
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while ((count = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, count);
                    }
                    fileOutputStream.close();

                }

                // 定位到下一个文件入口
                zipEntry = zipInputStream.getNextEntry();
//                Log.e("whh0927", "解压文件 入口 2： " + zipEntry);
            }
            zipInputStream.close();
            new File(zipPath).delete(); //删除原文件
            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

}
