package com.n0texpecterr0r.topviewplayer.search.bean;

/**
 * @author Created by Nullptr
 * @date 2018/9/6 21:17
 * @describe 搜索建议
 */
public class Suggestion {
    public static final int ARTIST = 0x56456;
    public static final int SONG = 0x56789;
    public static final int ALBUM = 0x56489798;

    private String name;
    private int type;

    public Suggestion(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
