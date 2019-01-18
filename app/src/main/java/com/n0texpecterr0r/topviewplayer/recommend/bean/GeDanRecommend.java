package com.n0texpecterr0r.topviewplayer.recommend.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/8 15:05
 * @describe TODO
 */
public class GeDanRecommend {
    @SerializedName("listid")
    private String listId;
    @SerializedName("pic")
    private String imgUrl;
    private String title;
    @SerializedName("tag")
    private String desc;

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

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
}