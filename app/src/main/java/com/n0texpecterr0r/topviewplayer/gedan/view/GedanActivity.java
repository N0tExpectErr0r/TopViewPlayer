package com.n0texpecterr0r.topviewplayer.gedan.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.base.BaseAdapter;
import com.n0texpecterr0r.topviewplayer.base.BaseMoreAdapter;
import com.n0texpecterr0r.topviewplayer.base.MvpBaseActivity;
import com.n0texpecterr0r.topviewplayer.base.MvpBaseView;
import com.n0texpecterr0r.topviewplayer.detail.view.DetailActivity;
import com.n0texpecterr0r.topviewplayer.gedan.GedanContract;
import com.n0texpecterr0r.topviewplayer.gedan.adapter.GedanAdapter;
import com.n0texpecterr0r.topviewplayer.gedan.bean.Gedan;
import com.n0texpecterr0r.topviewplayer.gedan.presenter.GedanPresenterImpl;
import com.n0texpecterr0r.topviewplayer.online.view.OnlineActivity;
import com.n0texpecterr0r.topviewplayer.gedan.GedanContract.*;
import com.n0texpecterr0r.topviewplayer.player.AudioPlayer;
import com.zhouwei.blurlibrary.EasyBlur;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class GedanActivity extends MvpBaseActivity<GedanPresenterImpl>
        implements GedanView, BaseAdapter.OnItemClickListener {
    private RecyclerView mRcvList;
    private Toolbar mTbTitle;
    private ImageView mIvCover;
    private ImageView mIvBackground;
    private TextView mTvName;
    private TextView mTvTag;
    private TextView mTvDesc;
    private FrameLayout mFlLoading;
    private String mListId;
    private GedanAdapter mAdapter;

    public static void actionStart(Context context, String listId) {
        Intent intent = new Intent(context, GedanActivity.class);
        intent.putExtra("listId", listId);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_gedan);
        mRcvList = findViewById(R.id.gedan_rcv_list);
        mTbTitle = findViewById(R.id.gedan_toolbar);
        mIvCover = findViewById(R.id.gedan_iv_cover);
        mIvBackground = findViewById(R.id.gedan_iv_background);
        mTvName = findViewById(R.id.gedan_tv_name);
        mTvTag = findViewById(R.id.gedan_tv_tag);
        mTvDesc = findViewById(R.id.gedan_tv_desc);
        mFlLoading = findViewById(R.id.gedan_fl_loading);

        setSupportActionBar(mTbTitle);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mListId = intent.getStringExtra("listId");

        mIvBackground.setColorFilter(Color.parseColor("#88000000"));

        mRcvList.setLayoutManager(new LinearLayoutManager(this));
        mRcvList.setNestedScrollingEnabled(false);
        mAdapter = new GedanAdapter(new ArrayList<>(), R.layout.item_song);
        mRcvList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        mPresenter.getGedan(mListId);
    }

    @Override
    protected GedanPresenterImpl onCreatePresenter() {
        return new GedanPresenterImpl();
    }

    @Override
    public void showGedan(Gedan gedan) {
        mTvName.setText(gedan.getTitle());
        mTvDesc.setText(gedan.getDesc());
        mTvTag.setText(gedan.getTag());
        mTbTitle.setTitle(gedan.getTitle());
        Glide.with(this)
                .load(gedan.getImgUrl())
                .asBitmap()
                .placeholder(R.drawable.ic_empty)
                .error(R.drawable.ic_empty)
                .dontAnimate()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mIvCover.setImageBitmap(resource);
                        mIvBackground.setImageBitmap(
                                EasyBlur.with(GedanActivity.this)
                                        .bitmap(resource)
                                        .radius(40)
                                        .scale(4)
                                        .policy(EasyBlur.BlurPolicy.FAST_BLUR)
                                        .blur());
                    }
                });
        mAdapter.setDatas(gedan.getSongList());
    }

    @Override
    public void showError() {
        Toasty.warning(this, "网络错误或歌单不存在").show();
        finish();
    }

    @Override
    public void showLoading() {
        mFlLoading.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void hideLoading() {
        mFlLoading.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(View view, int position) {
        // 设置当前歌曲及歌曲列表
        AudioPlayer.get().setOnline(true);
        AudioPlayer.get().setSongList(mAdapter.getDatas());
        AudioPlayer.get().changeCurrent(position);
    }
}
