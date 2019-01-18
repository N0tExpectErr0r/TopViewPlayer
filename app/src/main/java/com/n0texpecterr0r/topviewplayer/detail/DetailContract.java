package com.n0texpecterr0r.topviewplayer.detail;

import com.n0texpecterr0r.topviewplayer.base.MvpBaseView;

/**
 * 描述
 *
 * @author N0tExpectErr0r
 * @time 2019/01/18
 */
public class DetailContract {
    public interface DetailView extends MvpBaseView{
        void showLyrics(String lrcText);
    }

    public interface DetailPresenterCallback{
        void solveLyrics(String lrcText);
        void error();
    }

    public interface DetailPresenter{
        void getLyrics(String lrcLink);
    }

    public interface DetailModel{
        void getLyrics(String lrcLink, DetailPresenterCallback callback);
    }
}
