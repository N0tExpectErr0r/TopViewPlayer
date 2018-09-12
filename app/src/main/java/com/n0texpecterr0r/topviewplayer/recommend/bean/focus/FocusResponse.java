package com.n0texpecterr0r.topviewplayer.recommend.bean.focus;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * @author Created by Nullptr
 * @date 2018/9/8 16:59
 * @describe TODO
 */
public class FocusResponse {
    @SerializedName("error_code")
    private int errorCode;
    @SerializedName("pic")
    private List<Focus> Focus;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<Focus> getFocuses() {
        return Focus;
    }

    public void setFocus(List<Focus> Focus) {
        this.Focus = Focus;
    }
}
