package com.n0texpecterr0r.topviewplayer.recommend.bean.album;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * @author Created by Nullptr
 * @date 2018/9/8 14:56
 * @describe TODO
 */
public class AlbumRecommendList {
    @SerializedName("list")
    private List<AlbumRecommend> recommendList;

    public List<AlbumRecommend> getAlbumRecommends() {
        return recommendList;
    }

    public void setRecommendList(List<AlbumRecommend> recommendList) {
        this.recommendList = recommendList;
    }
}
