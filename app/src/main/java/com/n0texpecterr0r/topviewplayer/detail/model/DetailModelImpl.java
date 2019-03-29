package com.n0texpecterr0r.topviewplayer.detail.model;

import android.annotation.SuppressLint;
import android.util.Log;

import com.n0texpecterr0r.topviewplayer.detail.DetailContract.*;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.n0texpecterr0r.topviewplayer.AppApplication.USER_AGENT;

/**
 * 详情界面 Model
 *
 * @author N0tExpectErr0r
 * @time 2019/01/18
 */
public class DetailModelImpl implements DetailModel {
    @SuppressLint("CheckResult")
    @Override
    public void getLyrics(String lrcLink, DetailPresenterCallback callback) {
        Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> emitter) throws Exception {
                Log.d("DetailModel", "lrclink:"+lrcLink);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(lrcLink)
                        .addHeader("User-Agent", USER_AGENT)
                        .get()
                        .build();
                Call call = client.newCall(request);
                Response response = call.execute();
                emitter.onNext(response);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Response>() {
                    @Override
                    public void accept(Response response) throws Exception {
                        String lrc = response.body().string();
                        callback.solveLyrics(lrc);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        callback.error();
                    }
                });
    }
}
