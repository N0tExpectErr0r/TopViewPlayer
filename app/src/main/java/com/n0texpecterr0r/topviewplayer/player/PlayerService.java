package com.n0texpecterr0r.topviewplayer.player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import com.n0texpecterr0r.topviewplayer.IPlayerService;
import java.io.IOException;

/**
 * @author Created by Nullptr
 * @date 2018/9/19 11:16
 * @describe 播放音乐Service
 */
public class PlayerService extends Service {

    private MediaPlayer mPlayer = new MediaPlayer();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private Binder mBinder = new IPlayerService.Stub() {

        @Override
        public void start() throws RemoteException {
            if (!mPlayer.isPlaying()) {
                mPlayer.start();
            }
        }

        @Override
        public void pause() throws RemoteException {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
            }
        }

        @Override
        public void setSource(String url) throws RemoteException {
            try {
                mPlayer.reset();
                mPlayer.setDataSource(url);
                mPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void seekTo(int time) throws RemoteException {
            if (time > 0 && time < mPlayer.getDuration()) {
                mPlayer.seekTo(time);
            }
        }

        @Override
        public int getDuration() throws RemoteException {
            return mPlayer.getDuration();
        }

        @Override
        public int getCurrentTime() throws RemoteException {
            return mPlayer.getCurrentPosition();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
