package com.n0texpecterr0r.topviewplayer;

import android.app.Application;
import android.content.Context;
import android.database.MatrixCursor;
import android.text.style.TtsSpan.OrdinalBuilder;
import android.util.Log;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author Created by Nullptr
 * @date 2018/9/9 10:42
 * @describe TODO
 */
public class ContextApplication extends Application {
    private static Context sContext;

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
