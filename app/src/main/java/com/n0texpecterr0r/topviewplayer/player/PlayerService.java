package com.n0texpecterr0r.topviewplayer.player;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.n0texpecterr0r.topviewplayer.IPlayerService;
import com.n0texpecterr0r.topviewplayer.OnChangeSongListener;
import com.n0texpecterr0r.topviewplayer.OnPreparedListener;
import com.n0texpecterr0r.topviewplayer.bean.Song;
import com.n0texpecterr0r.topviewplayer.bean.SongPicUrl;
import com.n0texpecterr0r.topviewplayer.bean.SongUrl;
import com.n0texpecterr0r.topviewplayer.util.JsonUtil;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import api.MusicApi;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.n0texpecterr0r.topviewplayer.AppApplication.USER_AGENT;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/19 11:16
 * @describe 播放音乐Service
 */
public class PlayerService extends Service {
    private SongListManager mSongListManager;
    private ModeManager mModeManager;
    private MediaPlayer mPlayer = new MediaPlayer();
    private CopyOnWriteArrayList<OnChangeSongListener> mCompleteListeners = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<OnPreparedListener> mPrepareListeners = new CopyOnWriteArrayList<>();
    private Disposable mRequest;
    private boolean mIsOnline;

    @Override
    public void onCreate() {
        super.onCreate();
        mSongListManager = SongListManager.getInstance();
        mModeManager = ModeManager.getInstance();
        mPlayer.setOnCompletionListener(mediaPlayer->{
            nextSong();
        });
        mPlayer.setOnPreparedListener(mediaPlayer->{
            Song curSong = mSongListManager.getCurrentSong();
            if (!mediaPlayer.isPlaying())
                mediaPlayer.start();
            notifyChangeListener(curSong);
        });
    }

    private Binder mBinder = new IPlayerService.Stub() {

        @Override
        public void setSongList(List<Song> songList) throws RemoteException {
            mSongListManager.setSongList(songList);
        }

        @Override
        public void setCurrentIndex(int currentIndex) throws RemoteException {
            mSongListManager.setCurrentIndex(currentIndex);
            Song curSong = mSongListManager.getCurrentSong();
            if(!mIsOnline){
                prepareChange(curSong);
            }else{
                refreshSongInfo(curSong);
            }
        }

        @Override
        public void prev() throws RemoteException {
            if (mRequest!=null && !mRequest.isDisposed())
                mRequest.dispose();
            prevSong();
        }

        @Override
        public void next() throws RemoteException {
            if (mRequest!=null && !mRequest.isDisposed())
                mRequest.dispose();
            nextSong();
        }

        @Override
        public boolean isSongListEmpty() throws RemoteException {
            return mSongListManager.isEmpty();
        }

        @Override
        public Song getCurrentSong() throws RemoteException {
            return mSongListManager.getCurrentSong();
        }

        @Override
        public int getCurrentMode() throws RemoteException {
            return mModeManager.getCurrentMode();
        }

        @Override
        public void changeMode() throws RemoteException {
            mModeManager.changeMode();
        }

        @Override
        public void resume() throws RemoteException {
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
        public void play() throws RemoteException {
            if (!mPlayer.isPlaying()){
                mPlayer.start();
            }
        }

        @Override
        public void seekTo(int time) throws RemoteException {
            if (time >= 0 && time < mPlayer.getDuration()) {
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

        @Override
        public boolean isPlaying() throws RemoteException {
            return mPlayer.isPlaying();
        }

        @Override
        public void setOnline(boolean isOnline) throws RemoteException {
            mIsOnline = isOnline;
        }

        @Override
        public void addPrepareListener(OnPreparedListener listener) throws RemoteException {
            if(!mPrepareListeners.contains(listener)){
                mPrepareListeners.add(listener);
            }
        }

        @Override
        public void addChangeListener(OnChangeSongListener listener) throws RemoteException {
            if (!mCompleteListeners.contains(listener)) {
                mCompleteListeners.add(listener);
            }
        }
    };

    private void prepareChange(Song curSong) {
        try {
            mPlayer.reset();
            mPlayer.setDataSource(curSong.getPath());
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
    }

    private void notifyChangeListener(Song song){
        try {
            for (OnChangeSongListener listener : mCompleteListeners) {
                listener.onChanged(song);
            }
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    private void notifyPrepareListener(Song song){
        try {
            for (OnPreparedListener listener : mPrepareListeners) {
                listener.onPrepared(song);
            }
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    private void nextSong() {
        mSongListManager.next();
        Song nextSong = mSongListManager.getCurrentSong();
        if (!mIsOnline) {
            prepareChange(nextSong);
        } else {
            refreshSongInfo(nextSong);
        }
    }

    private void prevSong(){
        mSongListManager.prev();
        Song prevSong = mSongListManager.getCurrentSong();
        if (!mIsOnline) {
            prepareChange(prevSong);
        } else {
            refreshSongInfo(prevSong);
        }
    }

    @SuppressLint("CheckResult")
    private void refreshSongInfo(final Song song) {
        mRequest = Observable.create((ObservableOnSubscribe<Response>) emitter -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(MusicApi.Song.songInfo(song.getSongId()))
                    .addHeader("User-Agent", USER_AGENT)
                    .get()
                    .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            emitter.onNext(response);
        }).map(response -> {
            String json = response.body().string();
            String urlJson = JsonUtil.getNodeString(json, "songurl.url");
            List<SongUrl> songUrl = new Gson().fromJson(urlJson, new TypeToken<List<SongUrl>>() {
            }.getType());

            String picJson = JsonUtil.getNodeString(json, "songinfo");
            SongPicUrl picUrl = new Gson().fromJson(picJson, SongPicUrl.class);

            song.setPath(songUrl.get(0).getPath());
            song.setImgUrl(picUrl.getPicUrl());
            return song;
        }).subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(curSong -> {
            prepareChange(curSong);
        }, Throwable::printStackTrace);
    }
}
