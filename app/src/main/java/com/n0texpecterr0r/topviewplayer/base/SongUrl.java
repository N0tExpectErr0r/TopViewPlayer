package com.n0texpecterr0r.topviewplayer.base;

import com.google.gson.annotations.SerializedName;

/**
 * @author Created by Nullptr
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
