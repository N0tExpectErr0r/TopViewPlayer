package com.n0texpecterr0r.topviewplayer.gedan.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 描述
 *
 * @author N0tExpectErr0r
 * @time 2019/03/29
 */
public class GedanSong {
    private String title;
    @SerializedName("song_id")
    private String songId;
    private String author;
    @SerializedName("album_title")
    private String album;
    @SerializedName("pic_s130")
    private String pic;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
