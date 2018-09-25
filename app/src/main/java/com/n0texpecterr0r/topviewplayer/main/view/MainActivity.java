package com.n0texpecterr0r.topviewplayer.main.view;

import static com.n0texpecterr0r.topviewplayer.ContextApplication.USER_AGENT;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.TabLayoutOnPageChangeListener;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import api.MusicApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.n0texpecterr0r.topviewplayer.IPlayerService;
import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.base.Song;
import com.n0texpecterr0r.topviewplayer.base.SongUrl;
import com.n0texpecterr0r.topviewplayer.local.view.LocalFragment;
import com.n0texpecterr0r.topviewplayer.main.adapter.ViewPagerAdapter;
import com.n0texpecterr0r.topviewplayer.player.PlayerService;
import com.n0texpecterr0r.topviewplayer.recommend.view.RecommendFragment;
import com.n0texpecterr0r.topviewplayer.search.view.SearchActivity;
import com.n0texpecterr0r.topviewplayer.util.JsonUtil;
import com.n0texpecterr0r.topviewplayer.util.SongListManager;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTlTab;
    private ViewPager mVpPager;
    private CompleteReceiver mReceiver;
    private IPlayerService mPlayerService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayerService = IPlayerService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPlayerService = null;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_search:
                SearchActivity.actionStart(MainActivity.this);
                break;
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.n0texpecterr0r.topviewplayer.complete");
        mReceiver = new CompleteReceiver();
        registerReceiver(mReceiver, intentFilter);
        // 绑定服务
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        // 初始化控件
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        mTlTab = findViewById(R.id.main_tb_tab);
        mVpPager = findViewById(R.id.main_vp_pager);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new RecommendFragment());
        fragments.add(new LocalFragment());
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.setFragments(fragments);
        mVpPager.setAdapter(pagerAdapter);
        mVpPager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(mTlTab));
        mTlTab.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mVpPager));
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private class CompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            SongListManager manager = SongListManager.getInstance();
            manager.next();
            Song song = manager.getCurrentSong();
            try {
                if (!song.isOnline()) {
                    mPlayerService.setSource(song.getPath());
                    mPlayerService.start();
                }else{
                    requestOnlineSong(song);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(song);
        }
    }

    private void requestOnlineSong(final Song song) {
        Observable.create(new ObservableOnSubscribe<Response>() {
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
        }).map(new Function<Response, SongUrl>() {
            @Override
            public SongUrl apply(Response response) throws Exception {
                String json = JsonUtil.getNodeString(response.body().string(),"songurl.url");
                List<SongUrl> songUrl = new Gson().fromJson(json,new TypeToken<List<SongUrl>>(){}.getType());
                return songUrl.get(0);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SongUrl>() {
                    @Override
                    public void accept(SongUrl songUrl) throws Exception {
                        mPlayerService.setSource(songUrl.getPath());
                        mPlayerService.start();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toasty.error(MainActivity.this,"网络出现错误，请检查网络设置").show();
                    }
                });
    }
}
