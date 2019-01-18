package com.n0texpecterr0r.topviewplayer.search.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/6 19:16
 * @describe 歌手搜索建议
 */
public class ArtistSuggestion {
    @SerializedName("artistname")
    private String artistName;
    @SerializedName("artistid")
    private String artistId;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }
}
