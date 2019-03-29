package com.n0texpecterr0r.topviewplayer.gedan.bean;

import com.google.gson.annotations.SerializedName;
import com.n0texpecterr0r.topviewplayer.bean.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述
 *
 * @author N0tExpectErr0r
 * @time 2019/03/29
 */
public class Gedan {
    @SerializedName("listid")
    private String listId;
    private String title;
    @SerializedName("pic_500")
    private String imgUrl;
    private String width;
    private String height;
    private String tag;
    private String desc;
    private String url;
    @SerializedName("content")
    private List<GedanSong> content;
    private List<Song> songList;

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<GedanSong> getContent() {
        return content;
    }

    public void setContent(List<GedanSong> content) {
        this.content = content;
    }

    public void generateSongList(){
        songList = new ArrayList<>();
        for (GedanSong gedanSong : content) {
            Song song = new Song();
            song.setName(gedanSong.getTitle());
            song.setArtist(gedanSong.getAuthor());
            song.setAlbum(gedanSong.getAlbum());
            song.setImgUrl(gedanSong.getPic());
            song.setSongId(gedanSong.getSongId());
            songList.add(song);
        }
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }
}
