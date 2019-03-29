package com.n0texpecterr0r.topviewplayer.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author N0tExpectErr0r
 * @date 2018/10/9 21:08
 * @describe TODO
 */
public class SongInfoUrl {
    @SerializedName("pic_premium")
    private String picUrl;
    private String lrclink;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getLrclink() {
        return lrclink;
    }

    public void setLrclink(String lrclink) {
        this.lrclink = lrclink;
    }
}
