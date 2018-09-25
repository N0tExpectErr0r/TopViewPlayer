package com.n0texpecterr0r.topviewplayer.online.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import com.n0texpecterr0r.topviewplayer.IPlayerService;
import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.base.BaseMoreAdapter.OnItemClickListener;
import com.n0texpecterr0r.topviewplayer.base.MvpBaseActivity;
import com.n0texpecterr0r.topviewplayer.base.OnMoreScrollListener;
import com.n0texpecterr0r.topviewplayer.base.Song;
import com.n0texpecterr0r.topviewplayer.base.SongUrl;
import com.n0texpecterr0r.topviewplayer.online.OnlineContract.OnlineView;
import com.n0texpecterr0r.topviewplayer.online.adapter.OnlineAdapter;
import com.n0texpecterr0r.topviewplayer.online.presenter.OnlinePresenterImpl;
import com.n0texpecterr0r.topviewplayer.player.PlayerService;
import com.n0texpecterr0r.topviewplayer.util.SongListManager;
import es.dmoral.toasty.Toasty;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

public class OnlineActivity extends MvpBaseActivity<OnlinePresenterImpl> implements OnlineView, OnItemClickListener {

    private RecyclerView mRcvList;
    private OnlineAdapter mAdapter;
    private IPlayerService mPlayerService;
    private SwipeRefreshLayout mSrlRefresh;
    private String query;
    private int pageNo;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayerService = IPlayerService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPlayerService = null;
        }
    };

    public static void actionStart(Context context, String query) {
        Intent intent = new Intent(context, OnlineActivity.class);
        intent.putExtra("query", query);
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
        setContentView(R.layout.activity_online);
        mRcvList = findViewById(R.id.online_rcv_list);
        mSrlRefresh = findViewById(R.id.online_srl_refresh);

        pageNo = 1;
        Intent intent = getIntent();
        query = intent.getStringExtra("query");

        // 绑定服务
        Intent serviceIntent = new Intent(OnlineActivity.this, PlayerService.class);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);

        Toolbar toolbar = findViewById(R.id.online_toolbar);
        toolbar.setTitle(query + "的搜索结果");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        mRcvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new OnlineAdapter(new ArrayList<Song>(), R.layout.item_song, 20);
        mRcvList.setAdapter(mAdapter);
        mRcvList.addOnScrollListener(new OnMoreScrollListener(mRcvList) {
            @Override
            public void onLoadMore() {
                mPresenter.getOnlineSongs(query, pageNo++);
            }
        });
        mAdapter.setOnItemClickListener(this);

        mPresenter.getOnlineSongs(query, pageNo++);
    }

    @Override
    protected OnlinePresenterImpl initPresenter() {
        return new OnlinePresenterImpl();
    }

    @Override
    public void addSong(List<Song> songList) {
        mAdapter.addDatas(songList);
        mSrlRefresh.setEnabled(false);
    }

    @Override
    public void showError() {
        Toasty.warning(this, "网络问题或者没有对应结果").show();
        finish();
    }

    @Override
    public void showLoading() {
        if (pageNo == 2) {
            mSrlRefresh.setRefreshing(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void hideLoading() {
        if (pageNo == 2) {
            mSrlRefresh.setRefreshing(false);
        }
    }

    @Override
    public void loadCompelete() {
        mRcvList.clearOnScrollListeners();
        mAdapter.setShowBottom(false);
    }

    @Override
    public void playSong(SongUrl url) {
        try {
            mPlayerService.setSource(url.getPath());
            mPlayerService.start();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        // 设置当前歌曲及歌曲列表
        SongListManager manager = SongListManager.getInstance();
        manager.setCurrentIndex(position);
        manager.setSongList(mAdapter.getDatas());
        Song song = manager.getCurrentSong();
        mPresenter.requestSongUrl(song);
        EventBus.getDefault().post(song);
    }
}
