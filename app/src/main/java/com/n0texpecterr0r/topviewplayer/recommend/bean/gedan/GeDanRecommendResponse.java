package com.n0texpecterr0r.topviewplayer.recommend.bean.gedan;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * @author Created by Nullptr
 * @date 2018/9/8 15:02
 * @describe TODO
 */
public class GeDanRecommendResponse {
    @SerializedName("error_code")
    private int errorCode;
    private GeDanRecommendBean content;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public GeDanRecommendBean getContent() {
        return content;
    }

    public void setContent(GeDanRecommendBean content) {
        this.content = content;
    }
}
