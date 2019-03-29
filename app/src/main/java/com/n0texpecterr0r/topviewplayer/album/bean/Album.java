package com.n0texpecterr0r.topviewplayer.album.bean;

import com.google.gson.annotations.SerializedName;
import com.n0texpecterr0r.topviewplayer.bean.Song;

import java.util.ArrayList;
import java.util.List;

public class Album {
    private AlbumInfo albumInfo;
    @SerializedName("songlist")
    private List<AlbumSong> content;
    private List<Song> songList;

    public AlbumInfo getAlbumInfo() {
        return albumInfo;
    }

    public void setAlbumInfo(AlbumInfo albumInfo) {
        this.albumInfo = albumInfo;
    }

    public List<AlbumSong> getContent() {
        return content;
    }

    public void setContent(List<AlbumSong> content) {
        this.content = content;
    }

    public void generateSongList(){
        songList = new ArrayList<>();
        for (AlbumSong albumSong : content) {
            Song song = new Song();
            song.setName(albumSong.getName());
            song.setArtist(albumSong.getArtist());
            song.setAlbum(albumSong.getAlbum());
            song.setImgUrl(albumSong.getImgUrl());
            song.setSongId(albumSong.getSongId());
            song.setLrcLink(albumSong.getLrcLink());
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
