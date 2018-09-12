package com.n0texpecterr0r.topviewplayer.recommend.bean.song;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * @author Created by Nullptr
 * @date 2018/9/8 14:32
 * @describe TODO
 */
public class SongRecommendResponse{
    @SerializedName("error_code")
    private int errorCode;
    private List<SongRecommendBean> content;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<SongRecommendBean> getContent() {
        return content;
    }

    public void setContent(List<SongRecommendBean> content) {
        this.content = content;
    }
}
