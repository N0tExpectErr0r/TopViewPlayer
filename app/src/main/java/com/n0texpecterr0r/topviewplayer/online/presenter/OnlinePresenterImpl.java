package com.n0texpecterr0r.topviewplayer.online.presenter;

import com.n0texpecterr0r.topviewplayer.base.MvpBasePresenter;
import com.n0texpecterr0r.topviewplayer.online.OnlineContract.OnlineModel;
import com.n0texpecterr0r.topviewplayer.online.OnlineContract.OnlinePresenter;
import com.n0texpecterr0r.topviewplayer.online.OnlineContract.OnlinePresenterCallback;
import com.n0texpecterr0r.topviewplayer.online.OnlineContract.OnlineView;
import com.n0texpecterr0r.topviewplayer.online.bean.OnlineSong;
import com.n0texpecterr0r.topviewplayer.online.model.OnlineModelImpl;
import java.util.List;

/**
 * @author Created by Nullptr
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
    public void solveSong(List<OnlineSong> songList) {
        mView.addSong(songList);
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