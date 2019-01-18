package com.n0texpecterr0r.topviewplayer.search.model;

import static com.n0texpecterr0r.topviewplayer.ContextApplication.USER_AGENT;

import android.annotation.SuppressLint;
import android.util.Log;
import api.MusicApi.Search;
import com.google.gson.Gson;
import com.n0texpecterr0r.topviewplayer.search.SearchContract.SearchModel;
import com.n0texpecterr0r.topviewplayer.search.SearchContract.SearchPresenterCallback;
import com.n0texpecterr0r.topviewplayer.search.bean.SearchSuggestion;
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
import okhttp3.Request.Builder;
import okhttp3.Response;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/6 19:27
 * @describe 搜索Model
 */
public class SearchModelImpl implements SearchModel {

    @SuppressLint("CheckResult")
    @Override
    public void getSuggestions(final String query, final SearchPresenterCallback callback) {
        Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request request = new Builder()
                        .url(Search.searchSugestion(query))
                        .addHeader("User-Agent", USER_AGENT)
                        .get()
                        .build();
                Call call = client.newCall(request);

                Response response = call.execute();
                emitter.onNext(response);
            }
        }).map(new Function<Response, SearchSuggestion>() {
            @Override
            public SearchSuggestion apply(Response response) throws Exception {
                return new Gson().fromJson(response.body().string(), SearchSuggestion.class);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SearchSuggestion>() {
                    @Override
                    public void accept(SearchSuggestion searchSuggestion) throws Exception {
                        callback.solveSuggestion(searchSuggestion);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callback.error();
                        throwable.printStackTrace();
                    }
                });
    }
}
