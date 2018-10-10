package com.n0texpecterr0r.topviewplayer.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author Created by Nullptr
 * @date 2018/9/13 20:24
 * @describe TODO
 */
public class Song {
    @SerializedName("album_title")
    private String album;
    @SerializedName("title")
    private String name;
    @SerializedName("author")
    private String artist;
    @SerializedName("pic_small")
    private String imgUrl;
    @SerializedName("song_id")
    private String songId;
    @SerializedName("lrclink")
    private String lrcLink;
    private transient String path;
    private transient boolean isOnline;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

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

    public String getLrcLink() {
        return lrcLink;
    }

    public void setLrcLink(String lrcLink) {
        this.lrcLink = lrcLink;
    }
}
