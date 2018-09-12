package com.n0texpecterr0r.topviewplayer.local.model;

import android.content.Context;
import com.n0texpecterr0r.topviewplayer.local.LocalContract;
import com.n0texpecterr0r.topviewplayer.local.LocalContract.LocalPresenterCallback;
import com.n0texpecterr0r.topviewplayer.local.bean.LocalSong;
import com.n0texpecterr0r.topviewplayer.util.SongUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * @author Created by Nullptr
 * @date 2018/9/12 12:26
 * @describe TODO
 */
public class LocalModelImpl implements LocalContract.LocalModel {

    @Override
    public void getLocalSongs(final LocalPresenterCallback callback, final Context context) {
        Observable.create(new ObservableOnSubscribe<List<LocalSong>>() {
            @Override
            public void subscribe(ObservableEmitter<List<LocalSong>> emitter) throws Exception {
                emitter.onNext(SongUtil.queryLocalSong(context));
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<LocalSong>>() {
                    @Override
                    public void accept(List<LocalSong> songList) throws Exception {
                        callback.solveSongs(songList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callback.error();
                    }
                });
    }
}
