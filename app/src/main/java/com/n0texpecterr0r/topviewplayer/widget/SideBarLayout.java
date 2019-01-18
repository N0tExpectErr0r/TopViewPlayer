package com.n0texpecterr0r.topviewplayer.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.n0texpecterr0r.topviewplayer.ContextApplication;
import com.n0texpecterr0r.topviewplayer.widget.SideBar.OnChooseLetterListener;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/10 15:34
 * @describe 带有中心提示的Sidebar
 */
public class SideBarLayout extends RelativeLayout implements OnChooseLetterListener {

    private OnChooseLetterListener mOnChooseLetterListener;
    private TextView mTvHint;
    private SideBar mSideBar;

    public SideBarLayout(Context context) {
        super(context);
        initView(context);
    }

    public SideBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SideBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(21)
    private void initView(Context context) {
        LayoutParams layoutParams = new LayoutParams(dpToPx(30), ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(ALIGN_PARENT_RIGHT);
        mSideBar = new SideBar(context);
        mSideBar.setLayoutParams(layoutParams);
        mSideBar.setTranslationZ(8);
        addView(mSideBar);

        LayoutParams layoutParams1 = new LayoutParams(dpToPx(100), dpToPx(100));
        layoutParams1.addRule(CENTER_IN_PARENT);
        mTvHint = new TextView(context);
        mTvHint.setVisibility(GONE);
        mTvHint.setGravity(Gravity.CENTER);
        mTvHint.setTextSize(dpToPx(20));
        mTvHint.setTextColor(Color.WHITE);
        mTvHint.setBackgroundColor(Color.parseColor("#88AAAAAA"));
        mTvHint.setLayoutParams(layoutParams1);
        mTvHint.setTranslationZ(8);
        addView(mTvHint);

        mSideBar.setOnChooseLetterListener(this);
    }

    /**
     * 设置选择Listener
     */
    public void setOnChooseLetterListener(OnChooseLetterListener onChooseLetterListener) {
        this.mOnChooseLetterListener = onChooseLetterListener;
    }

    @Override
    public void onChoose(String letter) {
        mTvHint.setVisibility(View.VISIBLE);
        mTvHint.setText(letter);
        if (mOnChooseLetterListener != null) {
            mOnChooseLetterListener.onChoose(letter);
        }
    }

    @Override
    public void onCancel() {
        mTvHint.setVisibility(View.GONE);
        if (mOnChooseLetterListener != null) {
            mOnChooseLetterListener.onCancel();
        }
    }

    private int dpToPx(float dpValue) {
        float scale = ContextApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
