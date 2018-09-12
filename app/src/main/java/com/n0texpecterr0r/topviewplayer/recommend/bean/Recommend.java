package com.n0texpecterr0r.topviewplayer.recommend.bean;

/**
 * @author Created by Nullptr
 * @date 2018/9/8 12:35
 * @describe 推荐
 */
public class Recommend {
    final public static int TYPE_SONG = 0x56786789;
    final public static int TYPE_ALBUM = 0x45788979;
    final public static int TYPE_GEDAN = 0x7878778;

    private String imgUrl;
    private String title;
    private String desc;
    private String id;
    private int type;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
