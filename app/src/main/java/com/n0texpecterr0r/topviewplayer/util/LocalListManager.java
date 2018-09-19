package com.n0texpecterr0r.topviewplayer.util;

import static com.n0texpecterr0r.topviewplayer.util.ModeManager.MODE_DEFAULT;
import static com.n0texpecterr0r.topviewplayer.util.ModeManager.MODE_RANDOM;
import static com.n0texpecterr0r.topviewplayer.util.ModeManager.MODE_SINGLE;

import com.n0texpecterr0r.topviewplayer.base.AbstractListManager;
import com.n0texpecterr0r.topviewplayer.local.bean.LocalSong;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Created by Nullptr
 * @date 2018/9/19 15:17
 * @describe 本地播放列表Manager
 */
public class LocalListManager extends AbstractListManager {
    private static LocalListManager sInstance;
    private List<LocalSong> mSongList;
    private int mCurrentIndex;

    public LocalListManager() {
        mSongList = new ArrayList<>();
    }

    public static LocalListManager getInstance(){
        if (sInstance == null){
            synchronized (LocalListManager.class){
                if (sInstance == null){
                    sInstance = new LocalListManager();
                }
            }
        }
        return sInstance;
    }

    public void setSongList(List<LocalSong> songList){
        mSongList = songList;
    }

    public void setCurrentIndex(int currentIndex){
        mCurrentIndex = currentIndex;
    }

    @Override
    public void prev(){
        switch (ModeManager.getInstance().getCurrentMode()){
            case MODE_DEFAULT:
                if(mCurrentIndex--==0){
                    mCurrentIndex = mSongList.size()-1;
                }
                break;
            case MODE_RANDOM:
                mCurrentIndex = new Random().nextInt(mSongList.size()-1);
                break;
            case MODE_SINGLE:
                break;
        }
    }

    @Override
    public void next(){
        switch (ModeManager.getInstance().getCurrentMode()){
            case MODE_DEFAULT:
                if(mCurrentIndex++==mSongList.size()-1){
                    mCurrentIndex = 0;
                }
                break;
            case MODE_RANDOM:
                mCurrentIndex = new Random().nextInt(mSongList.size()-1);
                break;
            case MODE_SINGLE:
                break;
        }
    }

    public LocalSong getCurrentSong(){
        return mSongList.get(mCurrentIndex);
    }

}
