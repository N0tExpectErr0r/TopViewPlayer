package com.n0texpecterr0r.topviewplayer.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author N0tExpectErr0r
 * @date 2018/10/9 21:08
 * @describe TODO
 */
public class SongPicUrl {
    @SerializedName("pic_premium")
    private String picUrl;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
