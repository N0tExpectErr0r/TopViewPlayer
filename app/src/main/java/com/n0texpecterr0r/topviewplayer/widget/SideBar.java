package com.n0texpecterr0r.topviewplayer.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.n0texpecterr0r.topviewplayer.ContextApplication;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/10 14:21
 * @describe TODO
 */
public class SideBar extends View {

    private Paint mPaint = new Paint();
    private int mCurrentIndex;
    private boolean showBackground;
    public static String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z","#"};


    private OnChooseLetterListener mOnChooseLetterListener;

    public SideBar(Context context) {
        super(context);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBackground) {
            canvas.drawColor(Color.parseColor("#EEEEEE"));
        }
        int height = getHeight();
        int width = getWidth();
        int letterHeight = height / letters.length;
        for (int i = 0; i < letters.length; i++) {
            mPaint.setColor(Color.BLACK);
            mPaint.setAntiAlias(true);
            mPaint.setTextSize(dpToPx(14));
            if (i == mCurrentIndex) {
                mPaint.setColor(Color.parseColor("#FF2828"));
                mPaint.setFakeBoldText(true);
            }
            float x = width / 2 - mPaint.measureText(letters[i]) / 2;
            float y = letterHeight * i + letterHeight;
            canvas.drawText(letters[i], x, y, mPaint);
            mPaint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float y = event.getY();
        int lastIndex = mCurrentIndex;
        int index = (int) (y / getHeight() * letters.length);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                showBackground = true;
                if (lastIndex != index) {
                    if (index > -1 && index < letters.length) {
                        if (mOnChooseLetterListener != null) {
                            mOnChooseLetterListener.onChoose(letters[index]);
                        }
                        mCurrentIndex = index;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (lastIndex != index) {
                    if (index > -1 && index < letters.length) {
                        if (mOnChooseLetterListener != null) {
                            mOnChooseLetterListener.onChoose(letters[index]);
                        }
                        mCurrentIndex = index;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                showBackground = false;
                mCurrentIndex = -1;
                if (mOnChooseLetterListener != null) {
                    mOnChooseLetterListener.onCancel();
                }
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 设置选择Listener
     * @param onChooseLetterListener
     */
    public void setOnChooseLetterListener(OnChooseLetterListener onChooseLetterListener) {
        this.mOnChooseLetterListener = onChooseLetterListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public interface OnChooseLetterListener {

        void onChoose(String letter);

        void onCancel();
    }

    private int dpToPx(float dpValue) {
        float scale = ContextApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
