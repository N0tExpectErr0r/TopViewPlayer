package com.n0texpecterr0r.topviewplayer.gedan.model;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;
import com.n0texpecterr0r.topviewplayer.gedan.GedanContract;
import com.n0texpecterr0r.topviewplayer.gedan.bean.Gedan;
import com.n0texpecterr0r.topviewplayer.gedan.bean.GedanSong;

import java.util.List;

import api.MusicApi;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.n0texpecterr0r.topviewplayer.AppApplication.USER_AGENT;

/**
 * 描述
 *
 * @author N0tExpectErr0r
 * @time 2019/03/29
 */
public class GedanModelImpl implements GedanContract.GedanModel {
    @SuppressLint("CheckResult")
    @Override
    public void getGedan(GedanContract.GedanPresenterCallback callback, String listId) {
        getObservable(listId).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Gedan>() {
                    @Override
                    public void accept(Gedan songList) throws Exception {
                        callback.solveGedan(songList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        callback.error();
                    }
                });
    }

    private Observable<Gedan> getObservable(String listId){
        return Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(MusicApi.GeDan.geDanInfo(listId))
                        .addHeader("User-Agent", USER_AGENT)
                        .get()
                        .build();
                Call call = client.newCall(request);
                Response response = call.execute();
                emitter.onNext(response);
            }
        }).map(new Function<Response, Gedan>() {
            @Override
            public Gedan apply(Response response) throws Exception {
                String json = response.body().string();
                Gedan gedan = new Gson().fromJson(json, Gedan.class);
                gedan.generateSongList();
                return gedan;
            }
        });
    }
}
