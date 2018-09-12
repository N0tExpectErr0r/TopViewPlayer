package com.n0texpecterr0r.topviewplayer.recommend.bean.gedan;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * @author Created by Nullptr
 * @date 2018/9/8 15:04
 * @describe TODO
 */
public class GeDanRecommendBean {
    private List<GeDanRecommend> list;

    public List<GeDanRecommend> getList() {
        return list;
    }

    public void setList(List<GeDanRecommend> list) {
        this.list = list;
    }
}