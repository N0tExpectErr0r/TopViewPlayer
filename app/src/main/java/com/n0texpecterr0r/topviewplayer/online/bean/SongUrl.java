package com.n0texpecterr0r.topviewplayer.online.bean;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * @author Created by Nullptr
 * @date 2018/9/21 0:29
 * @describe TODO
 */
public class SongUrl {

    @SerializedName("url")
    private List<Url> urls;

    public List<Url> getUrls() {
        return urls;
    }

    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }

    public static class Url {
        @SerializedName("show_link")
        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
