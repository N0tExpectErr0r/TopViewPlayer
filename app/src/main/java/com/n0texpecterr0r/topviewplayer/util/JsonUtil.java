package com.n0texpecterr0r.topviewplayer.util;

import android.util.Log;

/**
 * @author Created by Nullptr
 * @date 2018/9/8 15:36
 * @describe TODO
 */
public class JsonUtil {

    public static String getNodeString(String json, String format) {
        String[] nodes = format.split("\\.");
        int start=0, end=0;
        int time = 0;
        for (int i = 0; i < json.length(); i++) {
            char ch = json.charAt(i);
            if (ch == '[' || ch == '{') {
                if (time < nodes.length - 2) {
                    time++;
                } else {
                    start = i;
                    break;
                }
            }
        }
        for (int i = json.length() - 1; i >= 0; i--) {
            char ch = json.charAt(i);
            if (ch == ']' || ch == '}') {
                if (time > 0) {
                    time--;
                } else {
                    end = i;
                    break;
                }
            }
        }
        if (start>=end){
            throw new IllegalArgumentException("json有误或格式有误");
        }
        return json.substring(start,end+1);
    }
}
