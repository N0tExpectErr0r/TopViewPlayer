package com.n0texpecterr0r.topviewplayer.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author Created by Nullptr
 * @date 2018/9/8 12:18
 * @describe 方形ImageView
 */
public class SquareImageView extends android.support.v7.widget.AppCompatImageView {

    public SquareImageView(Context context) {
        this(context,null);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setScaleType(ScaleType.FIT_XY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}