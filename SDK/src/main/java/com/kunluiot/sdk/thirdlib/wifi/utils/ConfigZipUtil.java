package com.kunluiot.sdk.thirdlib.wifi.utils;

import android.text.TextUtils;
import android.util.Log;

import com.kunluiot.sdk.thirdlib.zip4j.core.ZipFile;
import com.kunluiot.sdk.thirdlib.zip4j.model.ZipParameters;
import com.kunluiot.sdk.thirdlib.zip4j.util.Zip4jConstants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Mike on 2018/8/3.
 * Author:
 * Description:
 */
public class ConfigZipUtil {

    private static final String TAG = ConfigZipUtil.class.getSimpleName();

    public static File zip(String from, String to, String password) {
        if (from == null || to == null) {
            return null;
        }
        return share(from, to, password);
    }

    private static File share(String f, String t, String password) {
        try {
            File from = new File(f);
            File to = new File(t);
            if (to.exists()) {
                to.delete();
            }
            zipFile(new File[]{from}, to, password);
            if (to.exists()) {
                Log.d(TAG, "Zip success: " + to.toString());
                return to;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Zip fail");
        return null;
    }

    private static void zipFile(File[] files, File toFile, String password) throws IOException {
        if (files == null || files.length == 0) {
            throw new IOException("File not exist");
        }

        if (toFile.exists()) {
            if (!toFile.delete()) {
                throw new IOException("File not exist");
            }
        }

        ZipParameters parameters = new ZipParameters();
        // 压缩方式
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        // 压缩级别
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        if (!TextUtils.isEmpty(password)) {
            parameters.setEncryptFiles(true);
            // 加密方式
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
            parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
            parameters.setPassword(password.toCharArray());
        }

        ArrayList<File> fileList = new ArrayList<>();
        for (File file : files) {
            if (file.exists()) {
                fileList.add(file);
            }
        }
        try {
            ZipFile zipFile = new ZipFile(toFile);
            zipFile.addFiles(fileList, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
