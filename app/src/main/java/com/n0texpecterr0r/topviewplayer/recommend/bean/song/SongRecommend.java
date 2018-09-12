package com.n0texpecterr0r.topviewplayer.recommend.bean.song;

import com.google.gson.annotations.SerializedName;

/**
 * @author Created by Nullptr
 * @date 2018/9/8 14:38
 * @describe TODO
 */
public class SongRecommend {
    @SerializedName("pic_big")
    private String imgUrl;
    @SerializedName("song_id")
    private String songId;
    private String title;
    @SerializedName("recommend_reason")
    private String desc;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
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
