package com.n0texpecterr0r.topviewplayer.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author N0tExpectErr0r
 * @date 2018/10/9 20:18
 * @describe TODO
 */
public class AlbumView extends CircleImageView {

    private boolean isPause;
    private ObjectAnimator mRotateAnimator;

    public AlbumView(Context context) {
        super(context);
        initView();
    }

    public AlbumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AlbumView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        setBorderColor(Color.parseColor("#000000"));
        setBorderWidth(120);
        setScaleType(ScaleType.CENTER_CROP);
        mRotateAnimator = ObjectAnimator.ofFloat(this, "rotation", 0F, 360F)
                .setDuration(20000);
        mRotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mRotateAnimator.start();
    }

    public void setPause(boolean isPause){
        this.isPause = isPause;
        if (isPause){
            mRotateAnimator.pause();
        }else {
            mRotateAnimator.resume();
        }
    }
}
