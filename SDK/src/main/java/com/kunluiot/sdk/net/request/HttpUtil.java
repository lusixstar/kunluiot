package com.kunluiot.sdk.net.request;

import android.text.TextUtils;

import com.kunluiot.sdk.net.log.KunLuLog;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

public class HttpUtil {

    public static String get(String requestUrl, Map<String, Object> header) throws IOException {
        URL url = new URL(requestUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        if (header != null) {
            for (String key : header.keySet()) {
                conn.setRequestProperty(key, header.get(key).toString());
            }
        }
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        conn.connect();
        int code = conn.getResponseCode();
        StringBuffer sb = new StringBuffer();
        if (code == 200) {
            InputStream is = conn.getInputStream();
            byte[] bytes = new byte[1024];
            int readed = -1;

            while ((readed = is.read(bytes)) != -1) {
                String line = new String(bytes, 0, readed);
                sb.append(line);
            }
        } else {
            InputStream is = conn.getErrorStream();
            byte[] bytes = new byte[1024];
            int readed = -1;

            while ((readed = is.read(bytes)) != -1) {
                String line = new String(bytes, 0, readed);
                if (!TextUtils.isEmpty(line)) {
                    sb.append(line);
                }
            }
        }
        KunLuLog.d("resp ===== " + sb.toString());
        return sb.toString();
    }

    public static String get(String requestUrl) throws IOException {
        return get(requestUrl, null);
    }

    public static String post(String reqUrl, String json) throws IOException {
        return post(reqUrl, json, null);
    }

    public static String post(String reqUrl, String json, Map<String, Object> header) throws IOException {
        return post(reqUrl, json, header, "POST");
    }

    public static String post(String reqUrl, String json, Map<String, Object> header, String method) throws IOException {
        StringBuffer sb = new StringBuffer();
        URL url = new URL(reqUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestProperty("User-agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setConnectTimeout(50000);
        conn.setReadTimeout(50000);
        if (header != null) {
            for (String key : header.keySet()) {
                conn.setRequestProperty(key, header.get(key).toString());
            }
        }
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod(method);
        conn.connect();
        if (!TextUtils.isEmpty(json)) {
            OutputStream os = conn.getOutputStream();
            byte[] bytes = json.getBytes();
            os.write(bytes);
            os.flush();
            os.close();
        }
        int code = conn.getResponseCode();
        System.out.println("code === " + code);
        if (code == 200) {
            InputStream ins = conn.getInputStream();
            byte[] readedBytes = new byte[1024];
            int readed = -1;
            while ((readed = ins.read(readedBytes)) != -1) {

                String line = new String(readedBytes, 0, readed);
                if (!TextUtils.isEmpty(line)) {
                    sb.append(line);
                }
            }
        } else {
            InputStream is = conn.getErrorStream();
            if (is != null) {
                byte[] bytes = new byte[1024];
                int readed = -1;

                while ((readed = is.read(bytes)) != -1) {
                    String line = new String(bytes, 0, readed);
                    if (!TextUtils.isEmpty(line)) {
                        sb.append(line);
                    }
                }
            }
        }
        return sb.toString();
    }

    public static String uploadFile(String filePath, Map<String, Object> header) throws IOException {
        return uploadFile(filePath, null, header);
    }

    public static String uploadFile(String filePath, String reqUrl, Map<String, Object> header) throws IOException {
        if (TextUtils.isEmpty(reqUrl)) {
            reqUrl = ReqApi.KHA_WEB_BASE_URL + UserApi.KHA_API_UPDATE_PHOTO;
        }
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型
        File file = new File(filePath);
        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10 * 1000);
        conn.setConnectTimeout(10 * 1000);
        conn.setDoInput(true);  //允许输入流
        conn.setDoOutput(true); //允许输出流
        conn.setUseCaches(false);  //不允许使用缓存
        conn.setRequestMethod("POST");  //请求方式
        conn.setRequestProperty("Charset", "UTF-8");  //设置编码
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
        if (header != null) {
            for (String key : header.keySet()) {
                conn.setRequestProperty(key, header.get(key).toString());
            }
        }
        if (file != null) {
            /**
             * 当文件不为空，把文件包装并且上传
             */
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            StringBuffer sb = new StringBuffer();
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINE_END);
            /**
             * 这里重点注意：
             * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
             * filename是文件的名字，包含后缀名的   比如:abc.png
             */
            sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + LINE_END);
            sb.append("Content-Type: image/jpg" + LINE_END);
            sb.append(LINE_END);
            dos.write(sb.toString().getBytes());
            InputStream is = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                dos.write(bytes, 0, len);
            }
            is.close();
            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            /**
             * 获取响应码  200=成功
             * 当响应成功，获取响应的流
             */
            int res = conn.getResponseCode();
            KunLuLog.e("response code:" + res);
            KunLuLog.e("request success");
            int code = conn.getResponseCode();
            System.out.println("code === " + code);
            sb = new StringBuffer();
            if (code == 200 || code == 201) {
                InputStream ins = conn.getInputStream();
                byte[] readedBytes = new byte[1024];
                int readed = -1;
                while ((readed = ins.read(readedBytes)) != -1) {
                    String line = new String(readedBytes, 0, readed);
                    if (!TextUtils.isEmpty(line)) {
                        sb.append(line);
                    }
                }

            } else {
                InputStream ins = conn.getErrorStream();
                if (ins != null) {
                    byte[] readedBytes = new byte[1024];
                    int readed = -1;
                    while ((readed = ins.read(readedBytes)) != -1) {
                        String line = new String(readedBytes, 0, readed);

                        if (!TextUtils.isEmpty(line)) {
                            sb.append(line);
                        }
                    }
                }
            }
            result = sb.toString();
        }
        return result;
    }
}
