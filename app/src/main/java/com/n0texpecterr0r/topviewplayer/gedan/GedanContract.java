package com.n0texpecterr0r.topviewplayer.gedan;

import android.content.Context;

import com.n0texpecterr0r.topviewplayer.base.MvpBaseView;

import java.util.List;

import com.n0texpecterr0r.topviewplayer.bean.Song;
import com.n0texpecterr0r.topviewplayer.gedan.bean.Gedan;
import com.n0texpecterr0r.topviewplayer.gedan.bean.GedanSong;

/**
 * 描述
 *
 * @author N0tExpectErr0r
 * @time 2019/03/29
 */
public class GedanContract {
    public interface GedanView extends MvpBaseView {
        void showGedan(Gedan gedan);
    }

    public interface GedanPresenterCallback{
        void solveGedan(Gedan gedan);
        void error();
    }

    public interface GedanPresenter{
        void getGedan(String listId);
    }

    public interface GedanModel{
        void getGedan(GedanPresenterCallback gedanPresenterCallback,String listId);
    }
}
