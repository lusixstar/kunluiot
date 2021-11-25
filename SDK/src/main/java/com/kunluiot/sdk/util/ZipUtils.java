package com.kunluiot.sdk.util;


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
            File file = new File(tempFileName);
            if (!file.exists()) {
                file.mkdirs();
            }
            InputStream inputStream = new FileInputStream(zipPath);
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            byte[] buffer = new byte[1024 * 1024];
            int count = 0;
            while (zipEntry != null) {
                if (!zipEntry.isDirectory()) {
                    String fileName = zipEntry.getName();
                    file = new File(tempFileName + File.separator + fileName);  //放到新的解压的文件路径
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while ((count = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, count);
                    }
                    fileOutputStream.close();
                }
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.close();
            new File(zipPath).delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
