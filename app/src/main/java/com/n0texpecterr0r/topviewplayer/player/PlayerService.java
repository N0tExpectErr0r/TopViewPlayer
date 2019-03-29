package com.n0texpecterr0r.topviewplayer.player;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.n0texpecterr0r.topviewplayer.AppApplication;
import com.n0texpecterr0r.topviewplayer.IPlayerService;
import com.n0texpecterr0r.topviewplayer.OnChangeSongListener;
import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.bean.Song;
import com.n0texpecterr0r.topviewplayer.bean.SongInfoUrl;
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
    public static final String ACTION_PREV = "PREV";
    public static final String ACTION_ACTION = "ACTION";
    public static final String ACTION_NEXT = "NEXT";
    public static final String ACTION_INIT = "INIT";
    private static final int NOTIFICATION_ID = 0x234;
    private static final String CHANNEL_ID = "PLAYER_CHANNEL";
    private SongListManager mSongListManager;
    private ModeManager mModeManager;
    private NotificationManager mNotificationManager;
    private RemoteViews mRemoteViews;
    private MediaPlayer mPlayer = new MediaPlayer();
    private CopyOnWriteArrayList<OnChangeSongListener> mCompleteListeners = new CopyOnWriteArrayList<>();
    private Disposable mRequest;
    private boolean mIsOnline;
    private NotificationBroadcast mBroadcast;
    private Notification mNotification;
    private NotificationTarget mTarget;

    @Override
    public void onCreate() {
        super.onCreate();
        mSongListManager = SongListManager.getInstance();
        mModeManager = ModeManager.getInstance();

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createNotification();
        initBroadcast();

        mPlayer.setOnCompletionListener(mediaPlayer->{
            nextSong();
        });
        mPlayer.setOnPreparedListener(mediaPlayer->{
            Song curSong = mSongListManager.getCurrentSong();
            if (!mediaPlayer.isPlaying())
                mediaPlayer.start();
            updateNotification(curSong);

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
                mRemoteViews.setImageViewResource(R.id.notification_iv_action,
                        R.drawable.ic_pause_notification);
                notifyActionListener(true);
                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
                mPlayer.start();
            }
        }

        @Override
        public void pause() throws RemoteException {
            if (mPlayer.isPlaying()) {
                mRemoteViews.setImageViewResource(R.id.notification_iv_action,
                        R.drawable.ic_play_notification);
                notifyActionListener(false);
                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
                mPlayer.pause();
            }
        }

        @Override
        public void play() throws RemoteException {
            if (!mPlayer.isPlaying()){
                mRemoteViews.setImageViewResource(R.id.notification_iv_action,
                        R.drawable.ic_pause_notification);
                notifyActionListener(true);
                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
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
        Intent initIntent = new Intent();
        initIntent.setAction(ACTION_INIT);
        sendBroadcast(initIntent);
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        unregisterReceiver(mBroadcast);
    }

    private void notifyActionListener(boolean isPlaying){
        try {
            for (OnChangeSongListener listener : mCompleteListeners) {
                listener.onAction(isPlaying);
            }
        } catch (RemoteException e){
            e.printStackTrace();
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
            SongInfoUrl infoUrl = new Gson().fromJson(picJson, SongInfoUrl.class);

            song.setPath(songUrl.get(0).getPath());
            song.setImgUrl(infoUrl.getPicUrl());
            if (song.getLrcLink() == null){
                song.setLrcLink(infoUrl.getLrclink());
            }
            return song;
        }).subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(curSong -> {
            prepareChange(curSong);
        }, Throwable::printStackTrace);
    }

    private void createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "TopViewPlayer", NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_player);
        // 上一首
        Intent prev = new Intent();
        prev.setAction(ACTION_PREV);
        PendingIntent intentPrev = PendingIntent.getBroadcast(this,
                1, prev, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.notification_iv_prev, intentPrev);

        // 下一首
        Intent next = new Intent();
        next.setAction(ACTION_NEXT);
        PendingIntent intentNext = PendingIntent.getBroadcast(this,
                2, next, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.notification_iv_next, intentNext);

        // 播放/暂停
        Intent action = new Intent();
        action.setAction(ACTION_ACTION);
        PendingIntent intentAction = PendingIntent.getBroadcast(this,
                3, action, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.notification_iv_action, intentAction);

        mNotification = builder
                .setCustomBigContentView(mRemoteViews)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        mTarget = new NotificationTarget(this, mRemoteViews,
                R.id.notification_iv_cover, mNotification, NOTIFICATION_ID);
    }


    private void updateNotification(Song song) {
        Glide.with(getApplicationContext())
                .load(song.getImgUrl())
                .asBitmap()
                .into(mTarget);
        mRemoteViews.setTextViewText(R.id.notification_tv_name, song.getName());
        if (song.getAlbum().isEmpty()) {
            mRemoteViews.setTextViewText(R.id.notification_tv_author,
                    song.getArtist());
        } else {
            mRemoteViews.setTextViewText(R.id.notification_tv_author,
                    song.getArtist() + " -《" + song.getAlbum() + "》");
        }
        mRemoteViews.setImageViewResource(R.id.notification_iv_action,
                R.drawable.ic_pause_notification);
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }

    private void initBroadcast() {
        mBroadcast = new NotificationBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_ACTION);
        filter.addAction(ACTION_NEXT);
        filter.addAction(ACTION_PREV);
        filter.addAction(ACTION_INIT);
        registerReceiver(mBroadcast, filter);
    }

    public class NotificationBroadcast extends BroadcastReceiver {
        private IPlayerService mPlayerService;
        private boolean isInit = false;

        private ServiceConnection mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mPlayerService = IPlayerService.Stub.asInterface(service);
                isInit = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mPlayerService = null;
            }
        };

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case ACTION_INIT:
                    Intent serviceIntent = new Intent(context, PlayerService.class);
                    bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
                    break;
                case ACTION_PREV:
                    Log.d("NotificationLog", "prev");
                    if (isInit)
                        prev();
                    break;
                case ACTION_NEXT:
                    Log.d("NotificationLog", "next");
                    if (isInit)
                        next();
                    break;
                case ACTION_ACTION:
                    Log.d("NotificationLog", "action");
                    if (isInit) {
                        try {
                            if (mPlayerService.isPlaying())
                                pause();
                            else
                                play();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        private void play() {
            try {
                mPlayerService.play();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        private void pause() {
            try {
                mPlayerService.pause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        private void next() {
            try {
                mPlayerService.next();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        private void prev() {
            try {
                mPlayerService.prev();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
