package com.n0texpecterr0r.topviewplayer.search.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author Created by Nullptr
 * @date 2018/9/6 19:14
 * @describe 歌曲搜索建议
 */
public class SongSuggestion {
    @SerializedName("songname")
    private String songName;
    @SerializedName("songid")
    private String songId;
    @SerializedName("artistname")
    private String artistName;

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}