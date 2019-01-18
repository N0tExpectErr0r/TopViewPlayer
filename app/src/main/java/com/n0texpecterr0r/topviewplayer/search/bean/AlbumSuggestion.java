package com.n0texpecterr0r.topviewplayer.search.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/6 19:15
 * @describe 专辑搜索建议
 */

public class AlbumSuggestion {
    @SerializedName("albumname")
    private String albumName;
    @SerializedName("artistname")
    private String artistName;
    @SerializedName("albumid")
    private String albumId;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
}
