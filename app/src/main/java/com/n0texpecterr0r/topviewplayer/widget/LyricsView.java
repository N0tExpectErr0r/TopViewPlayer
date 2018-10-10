package com.n0texpecterr0r.topviewplayer.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.n0texpecterr0r.topviewplayer.IPlayerService;
import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.bean.Lyrics;
import com.n0texpecterr0r.topviewplayer.util.LyricsDecoder;
import java.io.IOException;
import java.util.List;

/**
 * @author Created by Nullptr
 * @date 2018/9/3 18:13
 * @describe 歌词View，用于显示歌词
 */
public class LyricsView extends View {

    private int mCurrentColor;
    private int mNormalColor;
    private int mWidth;
    private int mHeight;
    private List<Lyrics> mLyricsList;
    private Paint mCurrentPaint;
    private Paint mNormalPaint;
    private IPlayerService mPlayer;
    private int mCurrentPosition;
    private int mLastPosition;

    public LyricsView(Context context) {
        super(context);
        init();
    }

    public LyricsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LyricsView);
        mCurrentColor = typedArray.getColor(R.styleable.LyricsView_currentColor, Color.rgb(106, 59, 77));
        mNormalColor = typedArray.getColor(R.styleable.LyricsView_normalColor, Color.rgb(200, 200, 200));
        init();
    }

    public LyricsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mCurrentPosition = 0;
        mLastPosition = 0;
        setScrollY(0);
        invalidate();
        mCurrentPaint = new Paint();
        mCurrentPaint.setColor(mCurrentColor);
        mCurrentPaint.setTextSize(sp2px(getContext(), 16));
        mCurrentPaint.setAntiAlias(true);
        mNormalPaint = new Paint();
        mNormalPaint.setColor(mNormalColor);
        mNormalPaint.setTextSize(sp2px(getContext(), 16));
        mCurrentPaint.setAntiAlias(true);
    }

    /**
     * 设置歌词Lrc文本
     *
     * @param lyricsText Lrc文本
     */
    public void setLyricsText(String lyricsText) {
        try {
            mLyricsList = new LyricsDecoder().decodeLyrics(lyricsText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置歌词列表
     *
     * @param lyricsList 歌词列表
     */
    public void setLyricsList(List<Lyrics> lyricsList) {
        mLyricsList = lyricsList;
    }

    /**
     * 绑定Media播放器服务
     *
     * @param player 绑定的Media播放器服务
     */
    public void bindPlayer(IPlayerService player) {
        mPlayer = player;
    }

    /**
     * 设置当前颜色
     */
    public void setCurrentColor(int resId) {
        mCurrentColor = getResources().getColor(resId);
        mCurrentPaint.setColor(mCurrentColor);
    }

    /**
     * 设置当前颜色
     */
    public void setCurrentColor(int r, int g, int b) {
        setCurrentColor(255, r, g, b);
        mCurrentPaint.setColor(mCurrentColor);
    }

    /**
     * 设置当前颜色
     */
    public void setCurrentColor(int a, int r, int g, int b) {
        mCurrentColor = Color.argb(a, r, g, b);
        mCurrentPaint.setColor(mCurrentColor);
    }

    /**
     * 设置默认颜色
     */
    public void setNormalColor(int resId) {
        mNormalColor = getResources().getColor(resId);
        mNormalPaint.setColor(mNormalColor);
    }

    /**
     * 设置默认颜色
     */
    public void setNormalColor(int r, int g, int b) {
        setNormalColor(255, r, g, b);
        mNormalPaint.setColor(mNormalColor);
    }

    /**
     * 设置默认颜色
     */
    public void setNormalColor(int a, int r, int g, int b) {
        mNormalColor = Color.argb(a, r, g, b);
        mNormalPaint.setColor(mNormalColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mWidth == 0 || mHeight == 0) {
            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();
        }
        getCurrentPosition();

        int currentMillis = 0;
        try {
            currentMillis = mPlayer.getCurrentTime();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (mLyricsList == null || mLyricsList.size() == 0) {
            Rect bounds = new Rect();
            String text = "暂无歌词";
            mCurrentPaint.getTextBounds(text, 0, text.length(), bounds);
            canvas.drawText(text, mWidth / 2 - bounds.width() / 2, mHeight / 2 - bounds.height() / 2, mCurrentPaint);
        } else {
            drawLyrics(canvas);
            long start = mLyricsList.get(mCurrentPosition).getStart();
            float offset = (currentMillis - start) > 500 ? mCurrentPosition * 80
                    : mLastPosition * 80 + (mCurrentPosition - mLastPosition) * 80 * ((currentMillis - start) / 500f);
            setScrollY((int) offset);
            if (getScrollY() == mCurrentPosition * 80) {
                mLastPosition = mCurrentPosition;
            }
            postInvalidateDelayed(100);
        }
    }

    /**
     * 在canvas上绘制歌词
     *
     * @param canvas 传入的canvas
     */
    private void drawLyrics(Canvas canvas) {
        for (int i = 0; i < mLyricsList.size(); i++) {
            Rect bounds = new Rect();
            String text = mLyricsList.get(i).getText();
            if (text != null) {
                mCurrentPaint.getTextBounds(text, 0, text.length(), bounds);
                if (i == mCurrentPosition) {
                    canvas.drawText(text, mWidth / 2 - bounds.width() / 2,
                            mHeight / 2 - bounds.height() / 2 + 80 * i, mCurrentPaint);
                } else {
                    canvas.drawText(text, mWidth / 2 - bounds.width() / 2,
                            mHeight / 2 - bounds.height() / 2 + 80 * i, mNormalPaint);
                }
            }
        }
    }

    /**
     * 获取当前位置
     */
    private void getCurrentPosition() {
        try {
            int currentMillis = mPlayer.getCurrentTime();
            if (currentMillis < mLyricsList.get(0).getStart()) {
                mCurrentPosition = 0;
                return;
            }
            if (currentMillis > mLyricsList.get(mLyricsList.size() - 1).getStart()) {
                mCurrentPosition = mLyricsList.size() - 1;
                return;
            }
            for (int i = 0; i < mLyricsList.size(); i++) {
                if (i == mLyricsList.size() - 1) {
                    if (currentMillis >= mLyricsList.get(i).getStart()) {
                        mCurrentPosition = i;
                        return;
                    }
                } else if (currentMillis >= mLyricsList.get(i).getStart()
                        && currentMillis < mLyricsList.get(i + 1).getStart()) {
                    mCurrentPosition = i;
                    return;
                }
            }
        } catch (Exception e) {
            postInvalidateDelayed(100);
        }

    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}