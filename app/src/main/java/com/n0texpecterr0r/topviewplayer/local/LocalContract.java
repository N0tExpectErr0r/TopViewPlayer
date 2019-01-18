package com.n0texpecterr0r.topviewplayer.local;

import android.content.Context;
import com.n0texpecterr0r.topviewplayer.base.MvpBaseView;
import com.n0texpecterr0r.topviewplayer.bean.Song;
import java.util.List;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/12 10:31
 * @describe TODO
 */
public class LocalContract {
    public interface LocalView extends MvpBaseView{
        void showSongs(List<Song> songList);
    }

    public interface LocalPresenterCallback{
        void solveSongs(List<Song> songList);
        void error();
    }

    public interface LocalPresenter{
        void getLocalSongs(Context context);
    }

    public interface LocalModel{
        void getLocalSongs(LocalPresenterCallback localPresenterCallback,Context context);
    }
}
