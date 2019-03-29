package com.n0texpecterr0r.topviewplayer.recommend.presenter;

import com.n0texpecterr0r.topviewplayer.base.MvpBasePresenter;
import com.n0texpecterr0r.topviewplayer.bean.Song;
import com.n0texpecterr0r.topviewplayer.recommend.RecommendContract.RecommendModel;
import com.n0texpecterr0r.topviewplayer.recommend.RecommendContract.RecommendPresenter;
import com.n0texpecterr0r.topviewplayer.recommend.RecommendContract.RecommendPresenterCallback;
import com.n0texpecterr0r.topviewplayer.recommend.RecommendContract.RecommendView;
import com.n0texpecterr0r.topviewplayer.recommend.bean.Recommend;
import com.n0texpecterr0r.topviewplayer.recommend.bean.focus.Focus;
import com.n0texpecterr0r.topviewplayer.recommend.model.RecommendModelImpl;
import java.util.List;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/8 15:57
 * @describe TODO
 */
public class RecommendPresenterImpl extends MvpBasePresenter<RecommendView>
        implements RecommendPresenter,RecommendPresenterCallback {
    private RecommendModel mModel;

    public RecommendPresenterImpl(){
        mModel = new RecommendModelImpl();
    }

    @Override
    public void solveRecommends(List<Recommend> recommends) {
        mView.showRecommends(recommends);
        mView.hideLoading();
    }

    @Override
    public void error() {
        mView.showError();
        mView.hideLoading();
    }

    @Override
    public void getRecommends() {
        mView.showLoading();
        mModel.getRecommends(this);
    }


    @Override
    public void solveFocus(List<Focus> focusPics) {
        mView.showFocus(focusPics);
    }

    @Override
    public void solveSongInfo(Song song) {
        mView.playSong(song);
    }

    @Override
    public void getFocus() {
        mModel.getFocus(this);
    }

    @Override
    public void getSongInfo(String songId) {
        mModel.getSongInfo(this, songId);
    }
}
