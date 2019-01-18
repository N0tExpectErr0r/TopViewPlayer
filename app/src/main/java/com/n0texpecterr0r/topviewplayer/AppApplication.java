package com.n0texpecterr0r.topviewplayer;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.n0texpecterr0r.topviewplayer.player.PlayerService;
import com.n0texpecterr0r.topviewplayer.player.AudioPlayer;

import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/9 10:42
 * @describe TODO
 */
public class AppApplication extends Application {
    private static Context sContext;
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36";
    private IPlayerService mPlayerService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayerService = IPlayerService.Stub.asInterface(service);
            AudioPlayer.get().init(mPlayerService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPlayerService = null;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        // 处理下游无法被捕获的异常
        RxJavaPlugins.setErrorHandler(throwable ->
                Log.d("AppApplication", "Can't resolve Exception:"+throwable.getMessage()));
        Intent serviceIntent = new Intent(this, PlayerService.class);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public static Context getContext(){
        return sContext;
    }
}
