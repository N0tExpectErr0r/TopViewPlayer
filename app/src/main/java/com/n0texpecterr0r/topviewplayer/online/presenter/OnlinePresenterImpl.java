package com.n0texpecterr0r.topviewplayer.online.presenter;

import com.n0texpecterr0r.topviewplayer.base.MvpBasePresenter;
import com.n0texpecterr0r.topviewplayer.online.OnlineContract.OnlineModel;
import com.n0texpecterr0r.topviewplayer.online.OnlineContract.OnlinePresenter;
import com.n0texpecterr0r.topviewplayer.online.OnlineContract.OnlinePresenterCallback;
import com.n0texpecterr0r.topviewplayer.online.OnlineContract.OnlineView;
import com.n0texpecterr0r.topviewplayer.bean.Song;
import com.n0texpecterr0r.topviewplayer.online.model.OnlineModelImpl;
import java.util.List;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/13 19:23
 * @describe TODO
 */
public class OnlinePresenterImpl extends MvpBasePresenter<OnlineView>
        implements OnlinePresenter, OnlinePresenterCallback {
    private OnlineModel mModel;

    public OnlinePresenterImpl(){
        mModel = new OnlineModelImpl();
    }

    @Override
    public void solveSongList(List<Song> songList) {
        mView.addSongList(songList);
        mView.hideLoading();
    }

    @Override
    public void error() {
        mView.showError();
        mView.hideLoading();
    }

    @Override
    public void loadCompelete() {
        mView.loadCompelete();
    }

    @Override
    public void getOnlineSongs(String query, int pageNo) {
        mView.showLoading();
        mModel.getOnlineSongs(query,pageNo,this);
    }
}
