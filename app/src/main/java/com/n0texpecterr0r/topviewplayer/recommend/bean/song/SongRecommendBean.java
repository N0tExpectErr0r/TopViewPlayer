package com.n0texpecterr0r.topviewplayer.recommend.bean.song;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * @author Created by Nullptr
 * @date 2018/9/8 14:38
 * @describe TODO
 */
public class SongRecommendBean {
    @SerializedName("song_list")
    private List<SongRecommend> songRecommends;

    public List<SongRecommend> getSongRecommends() {
        return songRecommends;
    }

    public void setSongRecommends(List<SongRecommend> songRecommends) {
        this.songRecommends = songRecommends;
    }
}
