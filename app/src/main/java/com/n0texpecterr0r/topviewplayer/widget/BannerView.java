package com.n0texpecterr0r.topviewplayer.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.recommend.bean.focus.Focus;
import java.util.List;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/8 11:10
 * @describe 轮播控件
 */
public class BannerView extends FrameLayout implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;

    // 网络图片集合
    private List<Focus> mFocusList;

    // 指示器Layout
    private LinearLayout indicatorLayout;

    // 当前位置
    private int currentPosition;

    // 轮播时间
    private int autoPlayTime = 4000;

    // 是否自动播放
    private boolean isAutoPlay;

    // 是否是单图片
    private boolean isOneImage;

    //监听事件
    private OnBannerItemClick onBannerItemClick;

    //这里利用handler实现循环播放
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            currentPosition++;
            currentPosition = currentPosition % (mFocusList.size() + 2);
            viewPager.setCurrentItem(currentPosition);
            handler.sendEmptyMessageDelayed(0, autoPlayTime);
            return false;
        }
    });


    public BannerView(@NonNull Context context) {
        this(context, null);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BannerView, 0, 0);
        // 默认自动播放
        isAutoPlay = typedArray.getBoolean(R.styleable.BannerView_isAutoPlay, true);
        typedArray.recycle();

        viewPager = new ViewPager(getContext());
        indicatorLayout = new LinearLayout(getContext());
        // 添加监听事件
        viewPager.addOnPageChangeListener(this);
        // 将指示器放置底部并居中
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.bottomMargin = 10;

        addView(viewPager);
        addView(indicatorLayout, params);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        if (!isOneImage) {
            switchIndicatorTo(caculatePosition(position));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // 根据滑动松开后的状态，去判断当前的current 并跳转到指定current
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            int currentPosition = viewPager.getCurrentItem();
            int lastPosition = viewPager.getAdapter().getCount() - 2;
            if (currentPosition == 0) {
                viewPager.setCurrentItem(lastPosition, false);
            } else if (currentPosition == lastPosition + 1) {
                viewPager.setCurrentItem(1, false);
            }
        }
    }

    /**
     * ViewPager的Adapter
     */
    private class BannerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mFocusList.size() + 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = new ImageView(getContext());
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBannerItemClick != null) {
                        onBannerItemClick.onItemClick(mFocusList.get(caculatePosition(position)));
                    }
                }
            });
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(getContext())
                    .load(mFocusList.get(caculatePosition(position)).getImgUrl())
                    .placeholder(R.drawable.ic_mock)
                    .error(R.drawable.ic_mock)
                    .into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 设置图片地址
     * @param focusList 图片地址
     */
    public void setFocusList(List<Focus> focusList) {
        this.mFocusList = focusList;
        if (focusList.size() <= 1) {
            isOneImage = true;
        } else {
            isOneImage = false;
        }
        initViewPager();
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        if (!isOneImage) {
            // 添加指示器
            showIndicator();
        }
        BannerAdapter adapter = new BannerAdapter();
        viewPager.setAdapter(adapter);
        // 默认当前图片
        viewPager.setCurrentItem(1);
        // 判断是否自动播放和是否是一张图片的情况
        if (isAutoPlay && !isOneImage && !handler.hasMessages(0)) {
            handler.sendEmptyMessageDelayed(0, autoPlayTime);
        }
    }

    /**
     * 添加指示点
     */
    private void showIndicator() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(40, 40);
        indicatorLayout.removeAllViews();
        lp.setMargins(1, 2, 1, 2);
        ImageView imageView;
        int size = mFocusList.size();
        for (int i = 0; i < size; i++) {
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(lp);
            imageView.setImageResource(R.drawable.ic_select_point);
            indicatorLayout.addView(imageView);
        }
        switchIndicatorTo(0);
    }

    /**
     * 切换指示器点
     * @param currentPosition 当前的位置
     */
    private void switchIndicatorTo(int currentPosition) {
        for (int i = 0; i < indicatorLayout.getChildCount(); i++) {
            ImageView point = (ImageView)indicatorLayout.getChildAt(i);
            point.setColorFilter(Color.WHITE);
        }
        ImageView currentPoint = (ImageView) indicatorLayout.getChildAt(currentPosition);
        currentPoint.setColorFilter(null);
    }

    /**
     * 计算真实位置
     * @param position 计算的位置
     * @return 计算后的真实位置
     */
    private int caculatePosition(int position) {
        int realPosition;
        if (mFocusList.size() > 0) {
            realPosition = (position - 1) % mFocusList.size();
            if (realPosition < 0) {
                realPosition += mFocusList.size();
            }
        } else {
            realPosition = 0;
        }
        return realPosition;
    }

    /**
     * 设置是否自动轮播
     * @param autoPlay 是否自动轮播
     */
    public void setAutoPlay(boolean autoPlay) {
        isAutoPlay = autoPlay;
    }

    /**
     * Banner的Item点击事件
     * @param onBannerItemClick 点击事件
     */
    public void setOnBannerItemClick(OnBannerItemClick onBannerItemClick) {
        this.onBannerItemClick = onBannerItemClick;
    }

    public interface OnBannerItemClick {

        void onItemClick(Focus focus);
    }
}

