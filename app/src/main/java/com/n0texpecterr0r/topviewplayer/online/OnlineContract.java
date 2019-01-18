package com.n0texpecterr0r.topviewplayer.online;

import com.n0texpecterr0r.topviewplayer.base.MvpBaseView;
import com.n0texpecterr0r.topviewplayer.bean.Song;
import java.util.List;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/13 16:44
 * @describe TODO
 */
public class OnlineContract {
    public interface OnlineView extends MvpBaseView{
        void addSongList(List<Song> songList);
        void loadCompelete();
    }

    public interface OnlinePresenterCallback {
        void solveSongList(List<Song> songList);
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
