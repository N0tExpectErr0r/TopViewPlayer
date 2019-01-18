package com.n0texpecterr0r.topviewplayer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/13 20:24
 * @describe TODO
 */
public class Song implements Parcelable {
    @SerializedName("album_title")
    private String album;
    @SerializedName("title")
    private String name;
    @SerializedName("author")
    private String artist;
    @SerializedName("pic_small")
    private String imgUrl;
    @SerializedName("song_id")
    private String songId;
    @SerializedName("lrclink")
    private String lrcLink;
    private transient String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getLrcLink() {
        return lrcLink;
    }

    public void setLrcLink(String lrcLink) {
        this.lrcLink = lrcLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(album);
        dest.writeString(name);
        dest.writeString(artist);
        dest.writeString(imgUrl);
        dest.writeString(songId);
        dest.writeString(lrcLink);
        dest.writeString(path);
    }

    public void readFromParcel(Parcel reply) {
        album = reply.readString();
        name = reply.readString();
        artist = reply.readString();
        imgUrl = reply.readString();
        songId = reply.readString();
        lrcLink = reply.readString();
        path = reply.readString();
    }

    public Song(){}

    public Song(Parcel reply){
        album = reply.readString();
        name = reply.readString();
        artist = reply.readString();
        imgUrl = reply.readString();
        songId = reply.readString();
        lrcLink = reply.readString();
        path = reply.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

}
