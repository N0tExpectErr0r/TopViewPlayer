package com.n0texpecterr0r.topviewplayer.gedan.presenter;

import com.n0texpecterr0r.topviewplayer.base.MvpBasePresenter;
import com.n0texpecterr0r.topviewplayer.bean.Song;
import com.n0texpecterr0r.topviewplayer.gedan.GedanContract;
import com.n0texpecterr0r.topviewplayer.gedan.GedanContract.*;
import com.n0texpecterr0r.topviewplayer.gedan.bean.Gedan;
import com.n0texpecterr0r.topviewplayer.gedan.bean.GedanSong;
import com.n0texpecterr0r.topviewplayer.gedan.model.GedanModelImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述
 *
 * @author N0tExpectErr0r
 * @time 2019/03/29
 */
public class GedanPresenterImpl extends MvpBasePresenter<GedanView>
        implements GedanPresenter, GedanPresenterCallback{
    private GedanModel mModel;

    public GedanPresenterImpl(){
        mModel = new GedanModelImpl();
    }

    @Override
    public void solveGedan(Gedan gedan) {
        mView.showGedan(gedan);
        mView.hideLoading();
    }

    @Override
    public void error() {
        mView.showError();
        mView.hideLoading();
    }

    @Override
    public void getGedan(String listId) {
        mView.showLoading();
        mModel.getGedan(this,listId);
    }
}
