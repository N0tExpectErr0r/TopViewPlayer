package com.n0texpecterr0r.topviewplayer.search.bean;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/6 19:06
 * @describe 搜索建议Bean
 */
public class SearchSuggestion {

    @SerializedName("error_code")
    private int errorCode;
    @SerializedName("song")
    private List<SongSuggestion> songSuggestions;
    @SerializedName("album")
    private List<AlbumSuggestion> albumSuggestions;
    @SerializedName("artist")
    private List<ArtistSuggestion> artistSuggestions;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<SongSuggestion> getSongSuggestions() {
        return songSuggestions;
    }

    public void setSongSuggestions(List<SongSuggestion> songSuggestions) {
        this.songSuggestions = songSuggestions;
    }

    public List<AlbumSuggestion> getAlbumSuggestions() {
        return albumSuggestions;
    }

    public void setAlbumSuggestions(List<AlbumSuggestion> albumSuggestions) {
        this.albumSuggestions = albumSuggestions;
    }

    public List<ArtistSuggestion> getArtistSuggestions() {
        return artistSuggestions;
    }

    public void setArtistSuggestions(List<ArtistSuggestion> artistSuggestions) {
        this.artistSuggestions = artistSuggestions;
    }

}
