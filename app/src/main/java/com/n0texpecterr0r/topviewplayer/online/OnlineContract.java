package com.n0texpecterr0r.topviewplayer.online;

import com.n0texpecterr0r.topviewplayer.base.MvpBaseView;
import com.n0texpecterr0r.topviewplayer.online.bean.Song;
import java.util.List;

/**
 * @author Created by Nullptr
 * @date 2018/9/13 16:44
 * @describe TODO
 */
public class OnlineContract {
    public interface OnlineView extends MvpBaseView{
        void addSong(List<Song> songList);
        void loadCompelete();
    }

    public interface OnlinePresenterCallback {
        void solveSong(List<Song> songList);
        void error();
        void loadCompelete();
    }

    public interface OnlinePresenter{
        void getOnlineSongs(String query, int pageNo);
    }

    public interface OnlineModel{
        void getOnlineSongs(String query, int pageNo, OnlinePresenterCallback callback);
    }

}
