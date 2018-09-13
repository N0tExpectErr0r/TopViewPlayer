package com.n0texpecterr0r.topviewplayer.online.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author Created by Nullptr
 * @date 2018/9/13 16:42
 * @describe TODO
 */
public class OnlineSong {
    @SerializedName("song_id")
    private String songId;
    @SerializedName("song_title")
    private String name;
    @SerializedName("artist_id")
    private String artistId;
    @SerializedName("artist_name")
    private String artist;
    @SerializedName("album_id")
    private String albumId;
    @SerializedName("album_title")
    private String album;

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

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}