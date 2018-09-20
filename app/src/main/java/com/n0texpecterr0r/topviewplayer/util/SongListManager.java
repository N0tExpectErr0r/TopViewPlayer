package com.n0texpecterr0r.topviewplayer.util;

import static com.n0texpecterr0r.topviewplayer.util.ModeManager.MODE_DEFAULT;
import static com.n0texpecterr0r.topviewplayer.util.ModeManager.MODE_RANDOM;
import static com.n0texpecterr0r.topviewplayer.util.ModeManager.MODE_SINGLE;

import com.n0texpecterr0r.topviewplayer.base.AbstractListManager;
import com.n0texpecterr0r.topviewplayer.base.Song;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Created by Nullptr
 * @date 2018/9/19 16:02
 * @describe TODO
 */
public class SongListManager extends AbstractListManager {

    private static SongListManager sInstance;
    private List<Song> mSongList;
    private int mCurrentIndex;

    public SongListManager() {
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

    @Override
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

    @Override
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

    @Override
    public boolean isEmpty() {
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
