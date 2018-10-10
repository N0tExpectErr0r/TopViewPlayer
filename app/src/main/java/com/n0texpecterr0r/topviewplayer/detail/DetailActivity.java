package com.n0texpecterr0r.topviewplayer.detail;

import static com.n0texpecterr0r.topviewplayer.ContextApplication.USER_AGENT;
import static com.n0texpecterr0r.topviewplayer.util.ModeManager.MODE_DEFAULT;
import static com.n0texpecterr0r.topviewplayer.util.ModeManager.MODE_RANDOM;
import static com.n0texpecterr0r.topviewplayer.util.ModeManager.MODE_SINGLE;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import api.MusicApi;
import api.MusicApi.Album;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.n0texpecterr0r.topviewplayer.IPlayerService;
import com.n0texpecterr0r.topviewplayer.IPlayerService.Stub;
import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.base.Song;
import com.n0texpecterr0r.topviewplayer.base.SongPicUrl;
import com.n0texpecterr0r.topviewplayer.base.SongUrl;
import com.n0texpecterr0r.topviewplayer.main.view.MainActivity;
import com.n0texpecterr0r.topviewplayer.player.PlayerService;
import com.n0texpecterr0r.topviewplayer.util.JsonUtil;
import com.n0texpecterr0r.topviewplayer.util.ModeManager;
import com.n0texpecterr0r.topviewplayer.util.SongListManager;
import com.n0texpecterr0r.topviewplayer.util.TextUtil;
import com.n0texpecterr0r.topviewplayer.widget.AlbumView;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DetailActivity extends AppCompatActivity implements OnClickListener{
    private ImageView mIvBack;
    private ImageView mIvMode;
    private ImageView mIvPrev;
    private ImageView mIvAction;
    private ImageView mIvNext;
    private ImageView mIvList;
    private TextView mTvName;
    private TextView mTvArtist;
    private TextView mTvCurrent;
    private TextView mTvDuration;
    private SeekBar mSbTimebar;
    private AlbumView mAvAlbum;
    private IPlayerService mPlayerService;
    private Disposable mRequest;

    public static void actionStart(Context context){
        Intent intent = new Intent(context,DetailActivity.class);
        context.startActivity(intent);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayerService = Stub.asInterface(service);

            // 初始化界面
            initView();
            // 开始定时更新UI
            mUpdateTimeHandler.sendEmptyMessageDelayed(1,100);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPlayerService = null;
        }
    };

    /**
     * 成功连接Service后，初始化界面
     */
    private void initView() {
        int duration = 0;
        int current = 0;
        try {
            if (mPlayerService.isPlaying()) {
                mIvAction.setImageResource(R.drawable.ic_pause_white);
            } else {
                mIvAction.setImageResource(R.drawable.ic_play_white);
            }
            duration = mPlayerService.getDuration();
            current = mPlayerService.getCurrentTime();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Song song = SongListManager.getInstance().getCurrentSong();
        mTvName.setText(song.getName());
        mTvArtist.setText(song.getArtist());
        mSbTimebar.setMax(duration);
        mSbTimebar.setProgress(current);
        mTvCurrent.setText(TextUtil.getTimeStr(current));
        mTvDuration.setText(TextUtil.getTimeStr(duration));

        ModeManager modeManager = ModeManager.getInstance();
        switch (modeManager.getCurrentMode()){
            case MODE_DEFAULT:
                mIvMode.setImageResource(R.drawable.ic_default);
                break;
            case MODE_RANDOM:
                mIvMode.setImageResource(R.drawable.ic_random);
                break;
            case MODE_SINGLE:
                mIvMode.setImageResource(R.drawable.ic_single);
                break;
        }
        Glide.with(this)
                .load(song.getImgUrl())
                .into(mAvAlbum);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // 初始化变量
        mIvAction = findViewById(R.id.detail_iv_action);
        mIvBack = findViewById(R.id.detail_iv_back);
        mIvList = findViewById(R.id.detail_iv_list);
        mIvMode = findViewById(R.id.detail_iv_mode);
        mIvPrev = findViewById(R.id.detail_iv_prev);
        mIvNext = findViewById(R.id.detail_iv_next);
        mTvName = findViewById(R.id.detail_tv_name);
        mTvArtist = findViewById(R.id.detail_tv_artist);
        mTvCurrent = findViewById(R.id.detail_tv_current);
        mTvDuration = findViewById(R.id.detail_tv_duration);
        mSbTimebar = findViewById(R.id.detail_sb_timebar);
        mAvAlbum = findViewById(R.id.detail_av_album);

        // 注册EventBus
        EventBus.getDefault().register(this);
        // 绑定服务
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        // 设置进度条改变监听
        mSbTimebar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                try {
                    mPlayerService.seekTo(progress);
                    mTvCurrent.setText(TextUtil.getTimeStr(progress));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        // 播放暂停键监听
        mIvAction.setOnClickListener(this);
        // 下一首监听
        mIvNext.setOnClickListener(this);
        // 上一首监听
        mIvPrev.setOnClickListener(this);
        // 更换模式
        mIvMode.setOnClickListener(this);
        // 返回
        mIvBack.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeSong(Song song) {
        int duration = 0;
        try {
             duration = mPlayerService.getDuration();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mTvName.setText(song.getName());
        mTvArtist.setText(song.getArtist());
        mIvAction.setImageResource(R.drawable.ic_pause_white);
        mSbTimebar.setProgress(0);
        mSbTimebar.setMax(duration);
        mTvCurrent.setText("00:00");
        mTvDuration.setText(TextUtil.getTimeStr(duration));
        Glide.with(this)
                .load(song.getImgUrl())
                .into(mAvAlbum);
    }

    private Handler mUpdateTimeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            try {
                int currentTime = mPlayerService.getCurrentTime();
                mTvCurrent.setText(TextUtil.getTimeStr(currentTime));
                mSbTimebar.setProgress(currentTime);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            sendEmptyMessageDelayed(1,100);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detail_iv_action:
                playAndPause();
                break;
            case R.id.detail_iv_next:
                nextSong();
                break;
            case R.id.detail_iv_prev:
                prevSong();
                break;
            case R.id.detail_iv_mode:
                changeMode();
                break;
            case R.id.detail_iv_back:
                finish();
                break;
        }
    }

    /**
     * 播放/暂停
     */
    private void playAndPause() {
        try {
            if (mPlayerService.isPlaying()) {
                mPlayerService.pause();
                mIvAction.setImageResource(R.drawable.ic_play_white);
            } else {
                mPlayerService.start();
                mIvAction.setImageResource(R.drawable.ic_pause_white);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上一首
     */
    private void prevSong() {
        if (mRequest!=null && !mRequest.isDisposed())
            mRequest.dispose();
        SongListManager manager = SongListManager.getInstance();
        manager.prev();
        Song prevSong = manager.getCurrentSong();
        changeSong(prevSong);
        try {
            if (!prevSong.isOnline()) {
                mPlayerService.setSource(prevSong.getPath());
                mPlayerService.start();
            }else{
                requestOnlineSong(prevSong);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下一首
     */
    private void nextSong() {
        if (mRequest!=null && !mRequest.isDisposed())
            mRequest.dispose();
        SongListManager manager = SongListManager.getInstance();
        manager.next();
        Song nextSong = manager.getCurrentSong();
        changeSong(nextSong);
        try {
            if (!nextSong.isOnline()) {
                mPlayerService.setSource(nextSong.getPath());
                mPlayerService.start();
                changeSong(nextSong);
            }else{
                requestOnlineSong(nextSong);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换播放模式
     */
    private void changeMode() {
        ModeManager modeManager = ModeManager.getInstance();
        modeManager.changeMode();
        switch (modeManager.getCurrentMode()){
            case MODE_DEFAULT:
                Toasty.info(this,"顺序播放").show();
                mIvMode.setImageResource(R.drawable.ic_default);
                break;
            case MODE_RANDOM:
                Toasty.info(this,"随机播放").show();
                mIvMode.setImageResource(R.drawable.ic_random);
                break;
            case MODE_SINGLE:
                Toasty.info(this,"单曲循环").show();
                mIvMode.setImageResource(R.drawable.ic_single);
                break;
        }
    }

    /**
     * 请求网络歌曲
     * @param song
     */
    private void requestOnlineSong(final Song song) {
        mRequest = Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(MusicApi.Song.songInfo(song.getSongId()))
                        .addHeader("User-Agent",USER_AGENT)
                        .get()
                        .build();
                Call call = client.newCall(request);
                Response response = call.execute();
                emitter.onNext(response);
            }
        }).map(new Function<Response, Song>() {
            @Override
            public Song apply(Response response) throws Exception {
                String json = response.body().string();
                String urlJson = JsonUtil.getNodeString(json,"songurl.url");
                List<SongUrl> songUrl = new Gson().fromJson(urlJson,new TypeToken<List<SongUrl>>(){}.getType());
                String picJson = JsonUtil.getNodeString(json,"songinfo");
                SongPicUrl picUrl = new Gson().fromJson(picJson,SongPicUrl.class);
                song.setPath(songUrl.get(0).getPath());
                song.setImgUrl(picUrl.getPicUrl());
                return song;
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Song>() {
                    @Override
                    public void accept(Song song) throws Exception {
                        changeSong(song);
                        mPlayerService.setSource(song.getPath());
                        mPlayerService.start();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toasty.error(DetailActivity.this,"网络出现错误，请检查网络设置").show();
                    }
                });
    }
}