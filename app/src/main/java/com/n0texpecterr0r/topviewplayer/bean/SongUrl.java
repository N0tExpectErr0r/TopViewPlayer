package com.n0texpecterr0r.topviewplayer.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/25 8:28
 * @describe TODO
 */
public class SongUrl {

    @SerializedName("file_link")
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
