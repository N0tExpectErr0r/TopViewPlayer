package com.n0texpecterr0r.topviewplayer.detail.view;

import static com.n0texpecterr0r.topviewplayer.player.ModeManager.MODE_DEFAULT;
import static com.n0texpecterr0r.topviewplayer.player.ModeManager.MODE_RANDOM;
import static com.n0texpecterr0r.topviewplayer.player.ModeManager.MODE_SINGLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.n0texpecterr0r.topviewplayer.player.AudioPlayer;
import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.base.MvpBaseActivity;
import com.n0texpecterr0r.topviewplayer.bean.Song;
import com.n0texpecterr0r.topviewplayer.detail.DetailContract;
import com.n0texpecterr0r.topviewplayer.detail.presenter.DetailPresenterImpl;
import com.n0texpecterr0r.topviewplayer.util.BlurUtil;
import com.n0texpecterr0r.topviewplayer.util.TextUtil;
import com.n0texpecterr0r.topviewplayer.widget.AlbumView;
import com.n0texpecterr0r.topviewplayer.widget.LyricsView;
import com.zhouwei.blurlibrary.EasyBlur;

import es.dmoral.toasty.Toasty;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DetailActivity extends MvpBaseActivity<DetailPresenterImpl> implements OnClickListener, DetailContract.DetailView {

    private ImageView mIvBack;
    private ImageView mIvMode;
    private ImageView mIvPrev;
    private ImageView mIvAction;
    private ImageView mIvNext;
    private ImageView mIvList;
    private TextView mTvName;
    private TextView mTvArtist;
    private TextView mTvCurrent;
    private TextView mTvDuration;
    private SeekBar mSbTimebar;
    private AlbumView mAvAlbum;
    private LyricsView mLvLrcView;
    private ImageView mIvBackground;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, DetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail);
        // 初始化变量
        mIvAction = findViewById(R.id.detail_iv_action);
        mIvBack = findViewById(R.id.detail_iv_back);
        mIvList = findViewById(R.id.detail_iv_list);
        mIvMode = findViewById(R.id.detail_iv_mode);
        mIvPrev = findViewById(R.id.detail_iv_prev);
        mIvNext = findViewById(R.id.detail_iv_next);
        mTvName = findViewById(R.id.detail_tv_name);
        mTvArtist = findViewById(R.id.detail_tv_artist);
        mTvCurrent = findViewById(R.id.detail_tv_current);
        mTvDuration = findViewById(R.id.detail_tv_duration);
        mSbTimebar = findViewById(R.id.detail_sb_timebar);
        mAvAlbum = findViewById(R.id.detail_av_album);
        mLvLrcView = findViewById(R.id.detail_lv_lrcview);
        mIvBackground = findViewById(R.id.detail_iv_background);

        // 歌词View滑动监听
        mLvLrcView.setOnSeekListener(startTime -> AudioPlayer.get().seekTo((int) startTime));
        // 播放暂停键监听
        mIvAction.setOnClickListener(this);
        // 下一首监听
        mIvNext.setOnClickListener(this);
        // 上一首监听
        mIvPrev.setOnClickListener(this);
        // 更换模式
        mIvMode.setOnClickListener(this);
        // 返回
        mIvBack.setOnClickListener(this);
        // 专辑界面
        mAvAlbum.setOnClickListener(this);
        // 歌词界面
        mLvLrcView.setOnClickListener(this);

        // 初始化View
        initView();
        // 开始定时刷新UI
        mUpdateTimeHandler.sendEmptyMessage(1);
        // 注册EventBus
        EventBus.getDefault().register(this);
        // 设置进度条改变监听
        mSbTimebar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    AudioPlayer.get().seekTo(progress);
                    mTvCurrent.setText(TextUtil.getTimeStr(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void initView() {
        int duration = AudioPlayer.get().getDuration();
        int current = AudioPlayer.get().getCurrentTime();
        Song song = AudioPlayer.get().getCurrentSong();

        mTvName.setText(song.getName());
        mTvArtist.setText(song.getArtist());
        mSbTimebar.setMax(duration);
        mSbTimebar.setProgress(current);
        mTvCurrent.setText(TextUtil.getTimeStr(current));
        mTvDuration.setText(TextUtil.getTimeStr(duration));
        mLvLrcView.bindPlayer(AudioPlayer.get().getPlayer());
        mPresenter.getLyrics(song.getLrcLink());
        mIvBackground.setColorFilter(Color.parseColor("#88000000"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 设置透明状态栏
        }

        Glide.with(this)
                .load(song.getImgUrl())
                .asBitmap()
                .placeholder(R.drawable.ic_empty)
                .error(R.drawable.ic_empty)
                .dontAnimate()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mAvAlbum.setImageBitmap(resource);
                        mIvBackground.setImageBitmap(
                            EasyBlur.with(DetailActivity.this)
                                .bitmap(resource)
                                .radius(25)
                                .scale(4)
                                .policy(EasyBlur.BlurPolicy.FAST_BLUR)
                                .blur());
                    }
                });

        if (AudioPlayer.get().isPlaying()) {
            mIvAction.setImageResource(R.drawable.ic_pause_white);
            mAvAlbum.setPause(false);
        } else {
            mIvAction.setImageResource(R.drawable.ic_play_white);
            mAvAlbum.setPause(true);
        }

        switch (AudioPlayer.get().getCurrentMode()) {
            case MODE_DEFAULT:
                mIvMode.setImageResource(R.drawable.ic_default);
                break;
            case MODE_RANDOM:
                mIvMode.setImageResource(R.drawable.ic_random);
                break;
            case MODE_SINGLE:
                mIvMode.setImageResource(R.drawable.ic_single);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected DetailPresenterImpl onCreatePresenter() {
        return new DetailPresenterImpl();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeSong(Song song) {
        int duration = 0;
        duration = AudioPlayer.get().getDuration();
        mTvName.setText(song.getName());
        mTvArtist.setText(song.getArtist());
        mIvAction.setImageResource(R.drawable.ic_pause_white);
        mAvAlbum.setPause(false);
        mSbTimebar.setProgress(0);
        mSbTimebar.setMax(duration);
        mTvCurrent.setText("00:00");
        mTvDuration.setText(TextUtil.getTimeStr(duration));
        mPresenter.getLyrics(song.getLrcLink());
        Glide.with(this)
                .load(song.getImgUrl())
                .asBitmap()
                .placeholder(R.drawable.ic_empty)
                .error(R.drawable.ic_empty)
                .dontAnimate()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mAvAlbum.setImageBitmap(resource);
                        mIvBackground.setImageBitmap(
                                EasyBlur.with(DetailActivity.this)
                                        .bitmap(resource)
                                        .radius(25)
                                        .scale(4)
                                        .policy(EasyBlur.BlurPolicy.FAST_BLUR)
                                        .blur());
                    }
                });
    }

    @SuppressLint("HandlerLeak")
    private Handler mUpdateTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (AudioPlayer.get().isPlaying()) {
                int currentTime = AudioPlayer.get().getCurrentTime();
                mTvCurrent.setText(TextUtil.getTimeStr(currentTime));
                mSbTimebar.setProgress(currentTime);
            }
            sendEmptyMessageDelayed(1, 100);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_iv_action:
                playAndPause();
                break;
            case R.id.detail_iv_next:
                nextSong();
                break;
            case R.id.detail_iv_prev:
                prevSong();
                break;
            case R.id.detail_iv_mode:
                changeMode();
                break;
            case R.id.detail_iv_back:
                finish();
                break;
            case R.id.detail_av_album:
                mLvLrcView.startAnimation(getShowAnimate());
                mLvLrcView.setVisibility(View.VISIBLE);
                mAvAlbum.startAnimation(getHideAnimate());
                mAvAlbum.setVisibility(View.GONE);
                break;
            case R.id.detail_lv_lrcview:
                mAvAlbum.startAnimation(getShowAnimate());
                mAvAlbum.setVisibility(View.VISIBLE);
                mLvLrcView.startAnimation(getHideAnimate());
                mLvLrcView.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 播放/暂停
     */
    private void playAndPause() {
        if (AudioPlayer.get().isPlaying()) {
            AudioPlayer.get().pause();
            mIvAction.setImageResource(R.drawable.ic_play_white);
            mAvAlbum.setPause(true);
        } else {
            AudioPlayer.get().resume();
            mIvAction.setImageResource(R.drawable.ic_pause_white);
            mAvAlbum.setPause(false);
        }
    }

    /**
     * 上一首
     */
    private void prevSong() {
        AudioPlayer.get().prev();
        mLvLrcView.setLyricsText(null);
    }

    /**
     * 下一首
     */
    private void nextSong() {
        AudioPlayer.get().next();
        mLvLrcView.setLyricsText(null);
    }

    /**
     * 切换播放模式
     */
    private void changeMode() {
        AudioPlayer.get().changeMode();
        switch (AudioPlayer.get().getCurrentMode()) {
            case MODE_DEFAULT:
                Toasty.info(this, "顺序播放").show();
                mIvMode.setImageResource(R.drawable.ic_default);
                break;
            case MODE_RANDOM:
                Toasty.info(this, "随机播放").show();
                mIvMode.setImageResource(R.drawable.ic_random);
                break;
            case MODE_SINGLE:
                Toasty.info(this, "单曲循环").show();
                mIvMode.setImageResource(R.drawable.ic_single);
                break;
        }
    }

    @Override
    public void showLyrics(String lrcText) {
        Log.d("DetailActivity", lrcText);
        mLvLrcView.setLyricsText(lrcText);
    }

    @Override
    public void showLoading() {
        mLvLrcView.setLyricsText("正在加载歌词");
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showEmpty() {
    }

    @Override
    public void showError() {
        mLvLrcView.setLyricsText("暂无歌词");
    }

    private AlphaAnimation getShowAnimate() {
        AlphaAnimation showAction = new AlphaAnimation(0.0f, 1.0f);
        showAction.setDuration(200);
        return showAction;
    }

    private AlphaAnimation getHideAnimate() {
        AlphaAnimation hideAction = new AlphaAnimation(1.0f, 0.0f);
        hideAction.setDuration(200);
        return hideAction;
    }
}
