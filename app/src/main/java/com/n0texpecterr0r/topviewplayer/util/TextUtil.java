package com.n0texpecterr0r.topviewplayer.util;

import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

/**
 * @author Created by Nullptr
 * @date 2018/9/12 15:23
 * @describe TODO
 */
public class TextUtil {

    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     */
    public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim().toCharArray();// 把字符串转化成字符数组
        String output = "";

        try {
            for (int i = 0; i < input.length; i++) {
                if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    // 判断是否是中文
                    // 将汉语拼音的全拼存到temp数组
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(
                            input[i], format);
                    // 取拼音的第一个读音
                    output += temp[0];
                } else if (input[i] > 'a' && input[i] < 'z') {
                    // 是英文，全部转为大写字母
                    output += Character.toString(input[i]);
                    output = output.toUpperCase();
                }
                output += Character.toString(input[i]);
            }
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        return output;
    }

    /**
     * 将时间转换为mm:ss
     * @param duration
     * @return
     */
    public static String getTimeStr(int duration){
        Date date = new Date(duration);
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
        return timeFormat.format(date);
    }
}
