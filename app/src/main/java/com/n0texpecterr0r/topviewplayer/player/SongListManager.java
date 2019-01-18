package com.n0texpecterr0r.topviewplayer.player;

import static com.n0texpecterr0r.topviewplayer.player.ModeManager.MODE_DEFAULT;
import static com.n0texpecterr0r.topviewplayer.player.ModeManager.MODE_RANDOM;
import static com.n0texpecterr0r.topviewplayer.player.ModeManager.MODE_SINGLE;

import com.n0texpecterr0r.topviewplayer.bean.Song;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/19 16:02
 * @describe TODO
 */
public class SongListManager{

    private static SongListManager sInstance;
    private List<Song> mSongList;
    private int mCurrentIndex;

    private SongListManager() {
        mSongList = new ArrayList<>();
    }

    public static SongListManager getInstance() {
        if (sInstance == null) {
            synchronized (SongListManager.class) {
                if (sInstance == null) {
                    sInstance = new SongListManager();
                }
            }
        }
        return sInstance;
    }

    public void setSongList(List<Song> songList) {
        mSongList = songList;
    }

    public void setCurrentIndex(int currentIndex) {
        mCurrentIndex = currentIndex;
    }

    public void prev() {
        switch (ModeManager.getInstance().getCurrentMode()) {
            case MODE_DEFAULT:
                if (mCurrentIndex-- == 0) {
                    mCurrentIndex = mSongList.size() - 1;
                }
                break;
            case MODE_RANDOM:
                mCurrentIndex = new Random().nextInt(mSongList.size() - 1);
                break;
            case MODE_SINGLE:
                break;
        }
    }

    public void next() {
        switch (ModeManager.getInstance().getCurrentMode()) {
            case MODE_DEFAULT:
                if (mCurrentIndex++ == mSongList.size() - 1) {
                    mCurrentIndex = 0;
                }
                break;
            case MODE_RANDOM:
                mCurrentIndex = new Random().nextInt(mSongList.size() - 1);
                break;
            case MODE_SINGLE:
                break;
        }
    }
    
    public boolean isEmpty() {
        if (mSongList==null){
            return true;
        }
        return mSongList.isEmpty();
    }

    public Song getCurrentSong() {
        if (!mSongList.isEmpty()) {
            return mSongList.get(mCurrentIndex);
        }else{
            return null;
        }
    }
}
