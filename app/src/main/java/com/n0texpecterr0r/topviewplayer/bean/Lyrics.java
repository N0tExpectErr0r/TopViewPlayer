package com.n0texpecterr0r.topviewplayer.bean;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/3 17:44
 * @describe 歌词bean
 */
public class Lyrics {

    private String text;
    private long start;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }
}
