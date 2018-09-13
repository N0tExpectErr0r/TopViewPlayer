package com.n0texpecterr0r.topviewplayer.online.model;

import api.MusicApi.Search;
import com.google.gson.Gson;
import com.n0texpecterr0r.topviewplayer.online.OnlineContract;
import com.n0texpecterr0r.topviewplayer.online.OnlineContract.OnlinePresenterCallback;
import com.n0texpecterr0r.topviewplayer.online.bean.OnlineResponse;
import com.n0texpecterr0r.topviewplayer.util.JsonUtil;
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

/**
 * @author Created by Nullptr
 * @date 2018/9/13 16:53
 * @describe TODO
 */
public class OnlineModelImpl implements OnlineContract.OnlineModel {

    @Override
    public void getOnlineSongs(final String query, final int pageNo, final OnlinePresenterCallback callback) {
        Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Search.searchAccompany(query, pageNo, 20))
                        .addHeader("User-Agent",
                                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36")
                        .get()
                        .build();
                Call call = client.newCall(request);
                Response response = call.execute();
                emitter.onNext(response);
            }
        }).map(new Function<Response, OnlineResponse>() {
            @Override
            public OnlineResponse apply(Response response) throws Exception {
                String json = JsonUtil.getNodeString(response.body().string(), "Json.result");
                return new Gson().fromJson(json, OnlineResponse.class);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OnlineResponse>() {
                    @Override
                    public void accept(OnlineResponse onlineResponse) throws Exception {
                        callback.solveSong(onlineResponse.getSongList());
                        if (onlineResponse.getHaveMore() == 0) {
                            callback.loadCompelete();
                        }
                    }
                });
    }
}
