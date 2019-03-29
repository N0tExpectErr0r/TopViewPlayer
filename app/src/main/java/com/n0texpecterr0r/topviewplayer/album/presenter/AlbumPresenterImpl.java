package com.n0texpecterr0r.topviewplayer.album.presenter;

import com.n0texpecterr0r.topviewplayer.album.AlbumContract.*;
import com.n0texpecterr0r.topviewplayer.album.bean.Album;
import com.n0texpecterr0r.topviewplayer.album.model.AlbumModelImpl;
import com.n0texpecterr0r.topviewplayer.base.MvpBasePresenter;

/**
 * 描述
 *
 * @author N0tExpectErr0r
 * @time 2019/03/29
 */
public class AlbumPresenterImpl extends MvpBasePresenter<AlbumView>
        implements AlbumPresenter, AlbumPresenterCallback {
    private AlbumModel mModel;

    public AlbumPresenterImpl(){
        mModel = new AlbumModelImpl();
    }

    @Override
    public void solveAlbum(Album album) {
        mView.showAlbum(album);
        mView.hideLoading();
    }

    @Override
    public void error() {
        mView.showError();
        mView.hideLoading();
    }

    @Override
    public void getAlbum(String albumId) {
        mView.showLoading();
        mModel.getAlbum(this, albumId);
    }
}
