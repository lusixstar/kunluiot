package com.kunluiot.sdk.util;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class StringUtils {

    private static final String TAG = StringUtils.class.getSimpleName();

    /**
     * 空字符串
     */
    public static final String STR_EMPTY = "";

    /**
     * 判断字符串是否相等
     *
     * @return
     */
    public static boolean equals(String str1, String str2) {
        return str1.equals(str2);
    }

    /**
     * 判断字符串是否相等, 忽略大小写
     *
     * @return
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1.equalsIgnoreCase(str2);
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    /**
     * 判断字符串是否为null,或者""、{}、[]
     *
     * @param str
     * @return
     */
    public static boolean isEmpty2(String str) {
        return str == null || "".equals(str.trim()) || "{}".equals(str) || "[]".equals(str);
    }

    /**
     * 字符串转stream
     *
     * @param str
     * @return
     */
    public static InputStream StringToInputStream(String str) {
        if (StringUtils.isEmpty(str))
            return null;
        ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
        return stream;
    }

    /**
     * URLEncode
     *
     * @param paramString
     * @return
     */
    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {

            return "";
        }

        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
            Log.e("toURLEncoded", "toURLEncoded error:" + paramString, localException);
        }

        return "";
    }

    /**
     * toURLDecodered
     *
     * @param paramString
     * @return
     */
    public static String toURLDecodered(String paramString) {
        if (paramString == null || paramString.equals("")) {

            return "";
        }

        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLDecoder.decode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
            Log.e("toURLDecodered", "toURLDecodered error:" + paramString, localException);
        }

        return "";
    }

    /**
     * URL编码转换
     *
     * @param src
     * @return
     */
    public static String escape(String src) {
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);
        for (i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
                tmp.append(j);
            else if (j < 256) {
                tmp.append("%");
                if (j < 16)
                    tmp.append("0");
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

    /**
     * URL解码转换
     *
     * @param src
     * @return
     */
    public static String unescape(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }

    /**
     * 字符串替换
     *
     * @param line
     * @param oldString
     * @param newString
     * @return
     */
    public static final String replace(String line, String oldString, String newString) {
        if (line == null) {
            return null;
        }
        int i = 0;
        if ((i = line.indexOf(oldString, i)) >= 0) {
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = line.indexOf(oldString, i)) > 0) {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }

    /**
     * 根据body内容生成完整的HTML内容
     *
     * @param bodyContent html 内容
     * @return
     */
    public static String genHtml(String bodyContent) {
        // 没有图片时, 直接不显示, 避免重复加载
        return genHtml(bodyContent, "UTF-8").replace("src=\"\"", "src=\"没路径不显示图片\"");
    }

    /**
     * 根据body内容生成完整的HTML内容
     *
     * @param bodyContent html 内容
     * @param encode      编码格式, 默认为utf-8
     * @return
     */
    public static String genHtml(String bodyContent, String encode) {
        if (encode == null || "".equals(encode.trim()))
            encode = "utf-8";
        // 添加CSS样式, 使得所有的图片都铺满整个屏幕
        String css = "<style type=\"text/css\"> img {" + "width:100%;" + "height:auto;" + "}" + "body {" + "margin-right:15px;" + "margin-left:15px;" + "margin-top:15px;" + "font-size:45px;" + "}" + "</style>";

        StringBuffer sb = new StringBuffer();
        sb.append("<html xmlns=http://www.w3.org/1999/xhtml>\n");
        sb.append("<head>\n");
        sb.append("<meta http-equiv='Content-Type' content='text/html; charset=" + encode + "' />\n");
        sb.append(css);
        sb.append("<title>查看消息</title>\n");
        sb.append("</head>\n");
        sb.append("\n");
        sb.append("<body>\n");
        sb.append(bodyContent);
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

    public static String removeHtml(String htmlStr) {
        String result = "";
        boolean flag = true;
        if (htmlStr == null || "".equals(htmlStr.trim())) {
            return "";
        }

        htmlStr = htmlStr.replace("\"", ""); // 去掉引号

        char[] a = htmlStr.toCharArray();
        int length = a.length;
        for (int i = 0; i < length; i++) {
            if (a[i] == '<') {
                flag = false;
                continue;
            }
            if (a[i] == '>') {
                flag = true;
                continue;
            }
            if (flag == true) {
                result += a[i];
            }
        }
        return result.toString();
    }

    /**
     * Java 使用正则表达式过滤HTML中标签
     *
     * @param inputString
     * @return
     */
    public static String html2Text(String inputString) {
        if (StringUtils.isEmpty(inputString)) {
            return "";
        }
        String htmlStr = inputString;
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;

        Pattern p_html1;
        Matcher m_html1;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            // }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            // }
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            String regEx_html1 = "<[^>]+";
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签

            p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
            m_html1 = p_html1.matcher(htmlStr);
            htmlStr = m_html1.replaceAll(""); // 过滤html标签

            textStr = htmlStr;

            // 替换&amp;nbsp;
            textStr = textStr.replaceAll("&amp;", "").replaceAll("&nbsp;", "").replaceAll("nbsp;", "");

        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }

        return textStr;// 返回文本字符串
    }

    /************************************* Base64 编解码 *********************************************/
    private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    /**
     * Base64 encode the given data
     */
    public static String encode(byte[] data) {
        int start = 0;
        int len = data.length;
        StringBuffer buf = new StringBuffer(data.length * 3 / 2);

        int end = len - 3;
        int i = start;
        int n = 0;

        while (i <= end) {
            int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 0x0ff) << 8) | (((int) data[i + 2]) & 0x0ff);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append(legalChars[d & 63]);

            i += 3;

            if (n++ >= 14) {
                n = 0;
                buf.append(" ");
            }
        }

        if (i == start + len - 2) {
            int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 255) << 8);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append("=");
        } else if (i == start + len - 1) {
            int d = (((int) data[i]) & 0x0ff) << 16;

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append("==");
        }

        return buf.toString();
    }

    /**
     * Decodes the given Base64 encoded String to a new byte array. The byte
     * array holding the decoded data is returned.
     */
    public static byte[] decode(String s) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            decode(s, bos);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        byte[] decodedBytes = bos.toByteArray();
        try {
            bos.close();
            bos = null;
        } catch (IOException ex) {
            System.err.println("Error while decoding BASE64: " + ex.toString());
        }
        return decodedBytes;
    }

    private static void decode(String s, OutputStream os) throws IOException {
        int i = 0;

        int len = s.length();

        while (true) {
            while (i < len && s.charAt(i) <= ' ')
                i++;

            if (i == len)
                break;

            int tri = (decode(s.charAt(i)) << 18) + (decode(s.charAt(i + 1)) << 12) + (decode(s.charAt(i + 2)) << 6) + (decode(s.charAt(i + 3)));

            os.write((tri >> 16) & 255);
            if (s.charAt(i + 2) == '=')
                break;
            os.write((tri >> 8) & 255);
            if (s.charAt(i + 3) == '=')
                break;
            os.write(tri & 255);

            i += 4;
        }
    }

    private static int decode(char c) {
        if (c >= 'A' && c <= 'Z')
            return ((int) c) - 65;
        else if (c >= 'a' && c <= 'z')
            return ((int) c) - 97 + 26;
        else if (c >= '0' && c <= '9')
            return ((int) c) - 48 + 26 + 26;
        else
            switch (c) {
                case '+':
                    return 62;
                case '/':
                    return 63;
                case '=':
                    return 0;
                default:
                    throw new RuntimeException("unexpected code: " + c);
            }
    }

    /************************************* 压缩、解压 ****************************************/

    /**
     * 压缩方法
     *
     * @param str
     * @return
     */
    public static byte[] compress(String str) {
        if (str == null)
            return null;
        byte[] compressed;
        ByteArrayOutputStream out = null;
        ZipOutputStream zout = null;
        try {
            out = new ByteArrayOutputStream();
            zout = new ZipOutputStream(out);
            zout.putNextEntry(new ZipEntry("0"));
            zout.write(str.getBytes());
            zout.closeEntry();
            compressed = out.toByteArray();
        } catch (IOException e) {
            compressed = null;
        } finally {
            if (zout != null) {
                try {
                    zout.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return compressed;
    }

    /**
     * @param compressed
     * @return
     */
    public static final String decompress(byte[] compressed) {

        if (compressed == null)
            return null;

        ByteArrayOutputStream out = null;
        ByteArrayInputStream in = null;
        ZipInputStream zin = null;
        String decompressed;

        try {

            out = new ByteArrayOutputStream();
            in = new ByteArrayInputStream(compressed);
            zin = new ZipInputStream(in);
            byte[] buffer = new byte[1024];
            int offset = -1;

            while ((offset = zin.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString();
        } catch (IOException e) {
            decompressed = null;
        } finally {
            if (zin != null) {
                try {
                    zin.close();
                } catch (IOException e) {
                }
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }

            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }

        return decompressed;
    }

    /**
     * 验证合法字符
     *
     * @param character
     * @return
     */
    public static boolean checkCharacter(String character) {
        boolean flag = false;
        try { // 添加 密码特殊字符(@#$%^&*)匹配
            String check = "[a-zA-Z0-9~!@#$%^&*()_+=\\\\<>]{6,18}";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(character);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证url
     *
     * @param url
     * @return
     */
    public static boolean checkURL(String url) {
        if (StringUtils.isEmpty(url))
            return false;
        boolean flag = false;
        try { // 添加 密码特殊字符(@#$%^&*)匹配
            String check = "(http|https|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(url);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证合法字符
     *
     * @param character
     * @return
     */
    public static boolean checkCharacterPwd(String character) {
        if (character.isEmpty())
            return true;
        boolean flag = false;
        try { // 添加 密码特殊字符(@#$%^&*)匹配
            String check = "[0-9a-zA-Z~!@#$%^&*()_+=\\\\<>]{0,18}";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(character);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证QQ 号码
     *
     * @param character
     * @return
     */
    public static boolean checkQQ(String character) {
        boolean flag = false;
        try {
            String check = "[1-9][0-9]{3,14}";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(character);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证身份证
     *
     * @param character
     * @return
     */
    public static boolean checkIDCard(String character) {
        boolean flag15 = false;
        try {
            String check = "" + "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(character);
            flag15 = matcher.matches();
        } catch (Exception e) {
            flag15 = false;
        }
        boolean flag18 = false;
        try {
            String check = "" + "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(character);
            flag18 = matcher.matches();
        } catch (Exception e) {
            flag18 = false;
        }
        return (flag15 || flag18);
    }

    /**
     * 验证验证码字符
     *
     * @param character
     * @return
     */
    public static boolean checkNumber(String character) {
        boolean flag = false;
        try {
            String check = "[0-9_]{6,6}";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(character);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证银行卡
     *
     * @param number 银行卡
     * @return 校验结果
     */
    public static boolean checkCreditID(String number) {
        boolean flag = false;
        int sumOdd = 0;
        int sumEven = 0;
        int length = number.length();
        int[] wei = new int[length];
        for (int i = 0; i < length / 2; i++) {
            sumOdd += wei[2 * i];
            if ((wei[2 * i + 1] * 2) > 9)
                wei[2 * i + 1] = wei[2 * i + 1] * 2 - 9;
            else
                wei[2 * i + 1] *= 2;
            sumEven += wei[2 * i + 1];
        }
        if ((sumOdd + sumEven) % 10 == 0)
            flag = true;
        else
            flag = false;
        return flag;
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 检查是否符合手机号码格式
     *
     * @param mobileNumber
     * @return
     */
    public static boolean checkMobileNumber(String mobileNumber) {
        boolean flag = false;
        try {
            // Note: 2018-12-15 号需求, 不过滤手机号
            // Pattern regex = Pattern.compile("^1[34578]\\d{9}$");
            Pattern regex = Pattern.compile("^1\\d{10}$");
            //            Pattern regex = Pattern
            //                    .compile("^("
            //                            + "(13[0-9])"
            //                            + "|(15[^4,\\D])"
            //                            + "|(18[0-9])"
            //                            + "|(17[0-9]"
            //                            + "|(14[0-9])"
            //                            + "))"
            //                            + "\\d{8}$");
            Matcher matcher = regex.matcher(mobileNumber);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 检查是否符合邮编
     *
     * @param zipString
     * @return
     */
    public static boolean isZipNO(String zipString) {
        if (zipString == null)
            return false;
        String str = "^[1-9][0-9]{5}$";
        return Pattern.compile(str).matcher(zipString).matches();
    }

    public static String format(double value, String chart) {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern(chart);
        return df.format(value);
    }

    public static String getVolume(String src) {
        if (!src.contains(".")) {
            return src;
        } else {
            String tmp = src.substring(src.indexOf(".") + 1);
            int i = Integer.parseInt(tmp);
            System.out.println(i);
            if (i == 0) {
                return src.substring(0, src.indexOf("."));
            } else {
                return src;
            }
        }
    }

    /**
     * Unicode转String方法
     *
     * @param str
     * @return
     */
    public static String UnicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    /**
     * 字体大小动态设置方法
     *
     * @param string,start,end,TextSize
     * @return
     */
    static SpannableString setMyTextType(String string, int start, int end, int TextSize) {
        SpannableString builder = new SpannableString(string);
        builder.setSpan(new AbsoluteSizeSpan(TextSize), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return builder;
    }

    //MD5加密
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /*    *//**
     * 正则表达式 先截取淘口令, 其次是提取关键词, 否则返回全部
     *
     * @param link
     * @return
     *//*
    public static String getTaoURL(String link) {
        boolean isTaoUrl = false;
        if (StringUtils.isEmpty(link)) {
            SPUtils.put(HContant.Key4SP.IS_TAO_URL, isTaoUrl); // 保存是否是淘口令, 或者URL
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
*//*        // 提取淘口令
        Pattern pattern = Pattern.compile("[€￥《》(){}()<>【】%@][a-zA-Z0-9]{11}[€￥《》()<>【】%@]");
//        Pattern pattern = Pattern.compile("￥.*￥");
        Matcher matcher = pattern.matcher(link);
        if (matcher.find()) {
            isTaoUrl = true;
            stringBuffer.append(matcher.group().trim());
        }*//*
     *//*        // 提取关键词  （.*）
        Pattern regexUrl = Pattern.compile("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
        Matcher matcher1 = regexUrl.matcher(link);
        if (matcher1.find()) {
            isTaoUrl = true;
            String keyStr = matcher1.group().trim();
            stringBuffer.append(keyStr).toString();
        }*//*
        SPUtils.put(HContant.Key4SP.IS_TAO_URL, isTaoUrl); // 保存是否是淘口令, 或者URL
        return isTaoUrl ? stringBuffer.toString() : "";
    }*/

    /**
     * 正则表达式 提取关键词, 否则返回全部
     *
     * @param link
     * @return
     */
    public static String getURL(String link) {
        if (StringUtils.isEmpty(link)) {
            return "";
        }
        // 提取关键词  （.*）
        Pattern regexUrl = Pattern.compile("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
        Matcher matcher1 = regexUrl.matcher(link);
        if (matcher1.find()) {
            String keyStr = matcher1.group().trim();
            return keyStr;
        }
        return "";
    }


    /**
     * 格式化字符串，每三位用逗号隔开
     *
     * @param str
     * @return
     */
    public static String addComma(String str) {
        str = new StringBuilder(str).reverse().toString();     //先将字符串颠倒顺序
        if (str.equals("0")) {
            return str;
        }
        String str2 = "";
        for (int i = 0; i < str.length(); i++) {
            if (i * 3 + 3 > str.length()) {
                str2 += str.substring(i * 3, str.length());
                break;
            }
            str2 += str.substring(i * 3, i * 3 + 3) + ",";
        }
        if (str2.endsWith(",")) {
            str2 = str2.substring(0, str2.length() - 1);
        }
        //最后再将顺序反转过来
        String temp = new StringBuilder(str2).reverse().toString();
        //将最后的,去掉
        return temp.substring(0, temp.lastIndexOf(",")) + temp.substring(temp.lastIndexOf(",") + 1, temp.length());
    }


    /**
     * 一段文字中要突显多个的颜色
     * 首页资讯插卡 标签和标题间距 问题
     */
    public static SpannableString modifyStringByColor(Context mActivity, String content, String[] modifyStr, int[] colors) {
        SpannableString style = new SpannableString(content);
        int start;
        int end;
        int leng = modifyStr.length;
        int clickleg = colors.length;
        for (int i = 0; i < leng; i++) {
            start = content.indexOf(modifyStr[i]);
            if (start == -1) {
                continue;
            }
            end = start + modifyStr[i].length();
            if (i < clickleg) {
                style.setSpan(new ForegroundColorSpan(colors[i]), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                style.setSpan(null, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        }
        return style;
    }

    /**
     * 正则判断是否存在表情
     *
     * @param codePoint
     * @return
     */
    public static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * Emoji表情校验
     *
     * @param string
     * @return
     */
    public static boolean isEmoji(String string) {
        //过滤Emoji表情
        Pattern p = Pattern.compile("[^\\u0000-\\uFFFF]");
        //过滤Emoji表情和颜文字
        //Pattern p = Pattern.compile("[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]|[\\ud83e\\udd00-\\ud83e\\uddff]|[\\u2300-\\u23ff]|[\\u2500-\\u25ff]|[\\u2100-\\u21ff]|[\\u0000-\\u00ff]|[\\u2b00-\\u2bff]|[\\u2d06]|[\\u3030]");
        Matcher m = p.matcher(string);
        return m.find();
    }

    /**
     * 手机号码展示 132****8882
     *
     * @param content
     * @return
     */
    public static String getStarPhone(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        return content.substring(0, 3) + "****" + content.substring(content.length() - 4, content.length());
    }


    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        return simpleDateFormat.format(date);
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*\\.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static String stringToHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    public static boolean isMatchName(String name) {

        String all = "[\\u4e00-\\u9fa5_a-zA-Z0-9_|\\s]{1,20}";
        Pattern pattern = Pattern.compile(all);
        boolean tf = pattern.matcher(name).matches();

        return tf;

    }
}
