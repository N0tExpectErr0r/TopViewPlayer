package com.n0texpecterr0r.topviewplayer.album.bean;

import com.google.gson.annotations.SerializedName;

public class AlbumSong {
    @SerializedName("lrclink")
    private String lrcLink;
    @SerializedName("song_id")
    private String songId;
    @SerializedName("title")
    private String name;
    @SerializedName("author")
    private String artist;
    @SerializedName("album_title")
    private String album;
    @SerializedName("pic_s500")
    private String imgUrl;

    public String getLrcLink() {
        return lrcLink;
    }

    public void setLrcLink(String lrcLink) {
        this.lrcLink = lrcLink;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
