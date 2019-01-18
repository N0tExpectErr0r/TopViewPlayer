package com.n0texpecterr0r.topviewplayer.local.view;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.n0texpecterr0r.topviewplayer.OnPreparedListener;
import com.n0texpecterr0r.topviewplayer.PlayerCore;
import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.base.BaseAdapter.OnItemClickListener;
import com.n0texpecterr0r.topviewplayer.base.MvpBaseFragment;
import com.n0texpecterr0r.topviewplayer.local.LocalContract.LocalView;
import com.n0texpecterr0r.topviewplayer.local.adapter.LocalSongAdapter;
import com.n0texpecterr0r.topviewplayer.local.presenter.LocalPresenterImpl;
import com.n0texpecterr0r.topviewplayer.bean.Song;
import com.n0texpecterr0r.topviewplayer.widget.SideBar.OnChooseLetterListener;
import com.n0texpecterr0r.topviewplayer.widget.SideBarLayout;

import es.dmoral.toasty.Toasty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/9 14:14
 * @describe TODO
 */
public class LocalFragment extends MvpBaseFragment<LocalPresenterImpl> implements LocalView, OnItemClickListener {

    private SideBarLayout mSblSidebar;
    private SwipeRefreshLayout mSrlRefresh;
    private RecyclerView mRcvList;
    private TextView mTvEmpty;
    private LocalSongAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container,
                                     @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local, container, false);
        mSblSidebar = view.findViewById(R.id.local_sbl_sidebar);
        mRcvList = view.findViewById(R.id.local_rcv_list);
        mSrlRefresh = view.findViewById(R.id.local_srl_refresh);
        mTvEmpty = view.findViewById(R.id.local_tv_empty);

        mSrlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getLocalSongs(getContext());
            }
        });

        mAdapter = new LocalSongAdapter(new ArrayList<>(), R.layout.item_song);
        mRcvList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mPresenter.getLocalSongs(getContext());

        mSblSidebar.setOnChooseLetterListener(new OnChooseLetterListener() {
            @Override
            public void onChoose(String letter) {
                LinearLayoutManager manager = (LinearLayoutManager) mRcvList.getLayoutManager();
                Integer index = mAdapter.getIndexOfChar(letter.charAt(0));
                if (index != null) {
                    manager.scrollToPositionWithOffset(index, 0);
                    manager.setStackFromEnd(false);
                }
            }

            @Override
            public void onCancel() {

            }
        });
        return view;
    }

    @Override
    public LocalPresenterImpl initPresenter() {
        return new LocalPresenterImpl();
    }

    @Override
    public void showSongs(List<Song> songList) {
        mAdapter.setDatas(songList);
        mSrlRefresh.setEnabled(false);
    }

    @Override
    public void showLoading() {
        mSrlRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSrlRefresh.setRefreshing(false);
    }

    @Override
    public void showEmpty() {
        mRcvList.setVisibility(View.GONE);
        mSblSidebar.setVisibility(View.GONE);
        mTvEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError() {
        Toasty.error(getContext(), "本地音乐扫描音乐失败").show();
    }

    @Override
    public void onItemClick(View view, int position) {
        // 设置当前歌曲及歌曲列表
        PlayerCore.get().setOnline(false);
        PlayerCore.get().setSongList(mAdapter.getDatas());
        PlayerCore.get().changeCurrent(position);
        PlayerCore.get().addPrepareListener(new OnPreparedListener.Stub() {
            @Override
            public void onPrepared(Song curSong) throws RemoteException {
                PlayerCore.get().play();
            }
        });
    }
}
