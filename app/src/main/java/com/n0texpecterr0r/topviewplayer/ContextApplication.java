package com.n0texpecterr0r.topviewplayer;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.MatrixCursor;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.style.TtsSpan.OrdinalBuilder;
import android.util.Log;
import android.widget.Toast;
import com.n0texpecterr0r.topviewplayer.base.Song;
import com.n0texpecterr0r.topviewplayer.player.PlayerService;
import com.n0texpecterr0r.topviewplayer.util.SongListManager;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import org.greenrobot.eventbus.EventBus;

/**
 * @author Created by Nullptr
 * @date 2018/9/9 10:42
 * @describe TODO
 */
public class ContextApplication extends Application {
    private static Context sContext;
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36";

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        // 处理下游无法被捕获的异常
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d("ContextApplication", "Can't resolve Exception:"+throwable.getMessage());
            }
        });
    }

    public static Context getContext(){
        return sContext;
    }
}
