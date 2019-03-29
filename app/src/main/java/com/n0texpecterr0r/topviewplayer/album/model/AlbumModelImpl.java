package com.n0texpecterr0r.topviewplayer.album.model;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.n0texpecterr0r.topviewplayer.album.AlbumContract.*;
import com.n0texpecterr0r.topviewplayer.album.bean.Album;

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

public class AlbumModelImpl implements AlbumModel {
    @SuppressLint("CheckResult")
    @Override
    public void getAlbum(AlbumPresenterCallback callback, String albumId) {
        getObservable(albumId).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Album>() {
                    @Override
                    public void accept(Album album) throws Exception {
                        callback.solveAlbum(album);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        callback.error();
                    }
                });
    }

    private Observable<Album> getObservable(String albumId){
        return Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(MusicApi.Album.albumInfo(albumId))
                        .addHeader("User-Agent", USER_AGENT)
                        .get()
                        .build();
                Call call = client.newCall(request);
                Response response = call.execute();
                emitter.onNext(response);
            }
        }).map(new Function<Response, Album>() {
            @Override
            public Album apply(Response response) throws Exception {
                String json = response.body().string();
                Album album = new Gson().fromJson(json, Album.class);
                album.generateSongList();
                return album;
            }
        });
    }
}
