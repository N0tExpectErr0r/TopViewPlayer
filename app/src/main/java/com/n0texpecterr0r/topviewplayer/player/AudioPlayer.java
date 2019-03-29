package com.n0texpecterr0r.topviewplayer.player;

import android.os.RemoteException;
import android.util.Log;

import com.n0texpecterr0r.topviewplayer.IPlayerService;
import com.n0texpecterr0r.topviewplayer.OnChangeSongListener;
import com.n0texpecterr0r.topviewplayer.bean.Song;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.n0texpecterr0r.topviewplayer.player.ModeManager.MODE_DEFAULT;


/**
 * 采用单例模式的音乐管理类
 */
public class AudioPlayer {
    private static volatile AudioPlayer singleton;
    private IPlayerService mPlayerService;
    private boolean isInited = false;

    private AudioPlayer() {
    }

    public static AudioPlayer get() {
        if (singleton == null) {
            synchronized (AudioPlayer.class) {
                if (singleton == null) {
                    singleton = new AudioPlayer();
                }
            }
        }
        return singleton;
    }

    public void init(IPlayerService playerService) {
        mPlayerService = playerService;

        try {
            // 注册歌曲改变Listener
            mPlayerService.addChangeListener(mChangeListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        isInited = true;
    }

    public boolean isInited() {
        return isInited;
    }

    private OnChangeSongListener mChangeListener = new OnChangeSongListener.Stub() {
        @Override
        public void onChanged(Song song) {
            EventBus.getDefault().post(song);
        }

        @Override
        public void onAction(boolean isPlaying) {
            EventBus.getDefault().post(isPlaying);
        }
    };

    public void setSongList(List<Song> songList) {
        try {
            mPlayerService.setSongList(songList);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void changeCurrent(int currentIndex) {
        try {
            mPlayerService.setCurrentIndex(currentIndex);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void prev() {
        try {
            mPlayerService.prev();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void next() {
        try {
            mPlayerService.next();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public boolean isSongListEmpty() {
        try {
            return mPlayerService.isSongListEmpty();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return true;
    }


    public Song getCurrentSong() {
        try {
            return mPlayerService.getCurrentSong();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


    public int getCurrentMode() {
        try {
            return mPlayerService.getCurrentMode();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return MODE_DEFAULT;
    }


    public void changeMode() {
        try {
            mPlayerService.changeMode();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void resume() {
        try {
            mPlayerService.resume();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void pause() {
        try {
            mPlayerService.pause();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void play() {
        try {
            mPlayerService.play();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void seekTo(int time) {
        try {
            mPlayerService.seekTo(time);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public int getDuration() {
        try {
            return mPlayerService.getDuration();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int getCurrentTime() {
        try {
            return mPlayerService.getCurrentTime();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public boolean isPlaying() {
        try {
            return mPlayerService.isPlaying();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setOnline(boolean isOnline) {
        try {
            mPlayerService.setOnline(isOnline);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public IPlayerService getPlayer() {
        if (mPlayerService != null) {
            return mPlayerService;
        }
        return null;
    }
}