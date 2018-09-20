package com.n0texpecterr0r.topviewplayer.local.presenter;

import android.content.Context;
import com.n0texpecterr0r.topviewplayer.base.MvpBasePresenter;
import com.n0texpecterr0r.topviewplayer.local.LocalContract;
import com.n0texpecterr0r.topviewplayer.local.LocalContract.LocalModel;
import com.n0texpecterr0r.topviewplayer.local.LocalContract.LocalPresenterCallback;
import com.n0texpecterr0r.topviewplayer.local.LocalContract.LocalView;
import com.n0texpecterr0r.topviewplayer.local.model.LocalModelImpl;
import com.n0texpecterr0r.topviewplayer.base.Song;
import java.util.List;

/**
 * @author Created by Nullptr
 * @date 2018/9/12 12:19
 * @describe TODO
 */
public class LocalPresenterImpl extends MvpBasePresenter<LocalView>
        implements LocalContract.LocalPresenter ,LocalPresenterCallback {
    private LocalModel mModel;

    public LocalPresenterImpl(){
        mModel = new LocalModelImpl();
    }

    @Override
    public void getLocalSongs(Context context) {
        mView.showLoading();
        mModel.getLocalSongs(this,context);
    }

    @Override
    public void solveSongs(List<Song> songList) {
        mView.showSongs(songList);
        mView.hideLoading();
    }

    @Override
    public void error() {
        mView.showError();
        mView.hideLoading();
        mView.showEmpty();
    }
}
