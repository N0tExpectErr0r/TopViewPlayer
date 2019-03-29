package com.n0texpecterr0r.topviewplayer.album.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.n0texpecterr0r.topviewplayer.base.MvpBaseActivity;
import com.n0texpecterr0r.topviewplayer.album.AlbumContract;
import com.n0texpecterr0r.topviewplayer.album.adapter.AlbumAdapter;
import com.n0texpecterr0r.topviewplayer.album.bean.Album;
import com.n0texpecterr0r.topviewplayer.album.presenter.AlbumPresenterImpl;
import com.n0texpecterr0r.topviewplayer.player.AudioPlayer;
import com.zhouwei.blurlibrary.EasyBlur;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class AlbumActivity extends MvpBaseActivity<AlbumPresenterImpl>
        implements AlbumContract.AlbumView, BaseAdapter.OnItemClickListener {
    private RecyclerView mRcvList;
    private Toolbar mTbTitle;
    private ImageView mIvCover;
    private ImageView mIvBackground;
    private TextView mTvName;
    private TextView mTvArtist;
    private TextView mTvCompany;
    private FrameLayout mFlLoading;
    private String mAlbumId;
    private AlbumAdapter mAdapter;

    public static void actionStart(Context context, String albumId) {
        Intent intent = new Intent(context, com.n0texpecterr0r.topviewplayer.album.view.AlbumActivity.class);
        intent.putExtra("albumId", albumId);
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
        setContentView(R.layout.activity_album);
        mRcvList = findViewById(R.id.album_rcv_list);
        mTbTitle = findViewById(R.id.album_toolbar);
        mIvCover = findViewById(R.id.album_iv_cover);
        mIvBackground = findViewById(R.id.album_iv_background);
        mTvName = findViewById(R.id.album_tv_name);
        mTvArtist = findViewById(R.id.album_tv_author);
        mTvCompany = findViewById(R.id.album_tv_company);
        mFlLoading = findViewById(R.id.album_fl_loading);

        setSupportActionBar(mTbTitle);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mAlbumId = intent.getStringExtra("albumId");

        mIvBackground.setColorFilter(Color.parseColor("#88000000"));

        mRcvList.setLayoutManager(new LinearLayoutManager(this));
        mRcvList.setNestedScrollingEnabled(false);
        mAdapter = new AlbumAdapter(new ArrayList<>(), R.layout.item_song);
        mRcvList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        mPresenter.getAlbum(mAlbumId);
    }

    @Override
    protected AlbumPresenterImpl onCreatePresenter() {
        return new AlbumPresenterImpl();
    }

    @Override
    public void showAlbum(Album album) {
        mTvName.setText(album.getAlbumInfo().getTitle());
        mTvArtist.setText(album.getAlbumInfo().getAuthor());
        mTvCompany.setText(album.getAlbumInfo().getCompany());
        mTbTitle.setTitle(album.getAlbumInfo().getTitle());
        Glide.with(this)
                .load(album.getAlbumInfo().getImgUrl())
                .placeholder(R.drawable.ic_empty)
                .error(R.drawable.ic_empty)
                .dontAnimate()
                .into(mIvCover);
        Glide.with(this)
                .load(album.getAlbumInfo().getImgUrl())
                .asBitmap()
                .placeholder(R.drawable.ic_empty)
                .error(R.drawable.ic_empty)
                .dontAnimate()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mIvBackground.setImageBitmap(resource);
                        mIvBackground.setImageBitmap(
                                EasyBlur.with(com.n0texpecterr0r.topviewplayer.album.view.AlbumActivity.this)
                                        .bitmap(resource)
                                        .radius(40)
                                        .scale(4)
                                        .policy(EasyBlur.BlurPolicy.FAST_BLUR)
                                        .blur());
                    }
                });
        mAdapter.setDatas(album.getSongList());
    }

    @Override
    public void showError() {
        Toasty.warning(this, "网络错误或专辑不存在").show();
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

