package com.n0texpecterr0r.topviewplayer.online.bean;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * @author Created by Nullptr
 * @date 2018/9/13 16:39
 * @describe TODO
 */
public class OnlineResponse {
    private int total;
    @SerializedName("have_more")
    private int haveMore;
    @SerializedName("items")
    private List<OnlineSong> songList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getHaveMore() {
        return haveMore;
    }

    public void setHaveMore(int haveMore) {
        this.haveMore = haveMore;
    }

    public List<OnlineSong> getSongList() {
        return songList;
    }

    public void setSongList(List<OnlineSong> songList) {
        this.songList = songList;
    }
}
