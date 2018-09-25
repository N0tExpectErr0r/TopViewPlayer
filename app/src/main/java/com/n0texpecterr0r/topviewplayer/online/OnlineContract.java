package com.n0texpecterr0r.topviewplayer.online;

import com.n0texpecterr0r.topviewplayer.base.MvpBaseView;
import com.n0texpecterr0r.topviewplayer.base.Song;
import com.n0texpecterr0r.topviewplayer.base.SongUrl;
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
        void playSong(SongUrl url);
    }

    public interface OnlinePresenterCallback {
        void solveSong(List<Song> songList);
        void error();
        void loadCompelete();
        void solveSongUrl(SongUrl songUrl);
    }

    public interface OnlinePresenter{
        void getOnlineSongs(String query, int pageNo);
        void requestSongUrl(Song song);
    }

    public interface OnlineModel{
        void getOnlineSongs(String query, int pageNo, OnlinePresenterCallback callback);
        void requestSongUrl(Song song,OnlinePresenterCallback callback);
    }
}
