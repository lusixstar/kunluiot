package com.example.kiotsdk.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Chris
 * Date: 2021/12/13
 * Desc:
 */
public class StringUtils {

    /**
     * 判断是否包含特殊字符
     * @return  false:未包含 true：包含
     */
    public static boolean inputJudge(String editText) {
        String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(speChat);
        Matcher matcher = pattern.matcher(editText);
        return matcher.find();
    }
}
