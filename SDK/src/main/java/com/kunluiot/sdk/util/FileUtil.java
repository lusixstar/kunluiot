package com.kunluiot.sdk.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 文件操作工具包
 */
public class FileUtil {

    /**
     * 拷贝文本文件
     */
    public static void insertTextFile(String src, String desc, String text, String math) throws IOException {

        try (FileInputStream fis = new FileInputStream(src);
             InputStreamReader reader = new InputStreamReader(fis);
             BufferedReader bufferedReader = new BufferedReader(reader);
             FileOutputStream fos = new FileOutputStream(desc + ".tmp");
             OutputStreamWriter writer = new OutputStreamWriter(fos);
             BufferedWriter bw = new BufferedWriter(writer)) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(math)) {
                    int index = line.indexOf(math);
                    String header = line.substring(0, index);
                    System.out.println(header);
                    String end = line.substring(index);
                    line = header + text + end;
                }
                bw.write(line);
                bw.flush();
            }
        }
        reNamePath(desc + ".tmp", desc);
    }

    /**
     * 重命名
     */
    public static boolean reNamePath(String oldName, String newName) {
        File f = new File(oldName);
        return f.renameTo(new File(newName));
    }

    public static String getStringFromAssets(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}