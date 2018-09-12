package com.n0texpecterr0r.topviewplayer.recommend.bean.focus;

import com.google.gson.annotations.SerializedName;

public class Focus {
    @SerializedName("code")
    private String url;
    @SerializedName("randpic")
    private String imgUrl;
    private int type;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}