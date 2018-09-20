package com.n0texpecterr0r.topviewplayer.base;

/**
 * @author Created by Nullptr
 * @date 2018/9/19 19:04
 * @describe 携带基本歌曲信息的类
 */
public class Song {
    private String name;
    private String imgUrl;
    private String album;
    private String artist;

    public Song(String name, String imgUrl, String album, String artist) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.album = album;
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
