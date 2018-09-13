package com.n0texpecterr0r.topviewplayer.online.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author Created by Nullptr
 * @date 2018/9/13 20:24
 * @describe TODO
 */
public class OnlineSong {
    @SerializedName("album_title")
    private String album;
    @SerializedName("artist_id")
    private String artistId;
    @SerializedName("album_id")
    private String albumId;
    @SerializedName("title")
    private String name;
    @SerializedName("author")
    private String artist;
    @SerializedName("pic_small")
    private String imgUrl;
    @SerializedName("song_id")
    private String songId;

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
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
}
