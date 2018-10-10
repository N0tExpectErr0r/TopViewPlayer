package com.n0texpecterr0r.topviewplayer.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Created by Nullptr
 * @date 2018/10/9 20:18
 * @describe TODO
 */
public class AlbumView extends CircleImageView {
    private boolean isPause;

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
        setBorderColor(Color.parseColor("#212121"));
        setBorderWidth(120);
        setScaleType(ScaleType.CENTER_CROP);
    }
}
