package com.n0texpecterr0r.topviewplayer.detail.presenter;

import com.n0texpecterr0r.topviewplayer.base.MvpBasePresenter;
import com.n0texpecterr0r.topviewplayer.detail.DetailContract.*;
import com.n0texpecterr0r.topviewplayer.detail.model.DetailModelImpl;

/**
 * 详情界面 Presenter
 *
 * @author N0tExpectErr0r
 * @time 2019/01/18
 */
public class DetailPresenterImpl extends MvpBasePresenter<DetailView>
        implements DetailPresenter, DetailPresenterCallback {
    private DetailModel mModel;

    public DetailPresenterImpl(){
        mModel = new DetailModelImpl();
    }

    @Override
    public void getLyrics(String lrcLink) {
        mView.showLoading();
        mModel.getLyrics(lrcLink, this);
    }

    @Override
    public void error() {
        mView.showError();
        mView.hideLoading();
    }

    @Override
    public void solveLyrics(String lrcText) {
        mView.showLyrics(lrcText);
        mView.hideLoading();
    }

}
