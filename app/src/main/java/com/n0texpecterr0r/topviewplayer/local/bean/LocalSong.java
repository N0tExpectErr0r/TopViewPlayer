package com.n0texpecterr0r.topviewplayer.local.bean;

/**
 * @author Created by Nullptr
 * @date 2018/9/10 12:51
 * @describe 本地音乐类
 */
public class LocalSong {
    private String name;
    private String artist;
    private String path;
    private String album;
    private int duration;
    private long size;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
