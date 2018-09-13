package com.n0texpecterr0r.topviewplayer.recommend.bean;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import java.security.spec.KeySpec;

/**
 * @author Created by Nullptr
 * @date 2018/9/8 14:56
 * @describe TODO
 */
public class AlbumRecommend {
    @SerializedName("album_id")
    private String albumId;
    private String title;
    @SerializedName("publishcompany")
    private String desc;
    @SerializedName("pic_big")
    private String imgUrl;

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
