package com.n0texpecterr0r.topviewplayer.online.model;

import android.annotation.SuppressLint;

import static com.n0texpecterr0r.topviewplayer.AppApplication.USER_AGENT;

import api.MusicApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.n0texpecterr0r.topviewplayer.online.OnlineContract;
import com.n0texpecterr0r.topviewplayer.online.OnlineContract.OnlinePresenterCallback;
import com.n0texpecterr0r.topviewplayer.bean.Song;
import com.n0texpecterr0r.topviewplayer.util.JsonUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/13 16:53
 * @describe TODO
 */
public class OnlineModelImpl implements OnlineContract.OnlineModel{

    @SuppressLint("CheckResult")
    @Override
    public void getOnlineSongs(final String query, final int pageNo, final OnlinePresenterCallback callback) {
        getObservable(query,pageNo).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Song>>() {
                    @Override
                    public void accept(List<Song> songList) throws Exception {
                        callback.solveSongList(songList);
                        if (songList.size() < 20) {
                            callback.loadCompelete();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        callback.error();
                    }
                });
    }

    private String getSearchUrl(String query, int pageNo, int pageSize, int type) {
        StringBuffer sb = new StringBuffer(
                "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json");
        sb.append("&method=").append("baidu.ting.search.merge").append("&query=").append(MusicApi.encode(query))
                .append("&page_no=").append(pageNo).append("&page_size=").append(pageSize).append("&type=").append(type)
                .append("&data_source=0");
        return sb.toString();
    }

    private Observable<List<Song>> getObservable(final String query, final int pageNo){
        return Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(getSearchUrl(query, pageNo, 20, 0))
                        .addHeader("User-Agent", USER_AGENT)
                        .get()
                        .build();
                Call call = client.newCall(request);
                Response response = call.execute();
                emitter.onNext(response);
            }
        }).map(new Function<Response, List<Song>>() {
            @Override
            public List<Song> apply(Response response) throws Exception {
                String json = JsonUtil.getNodeString(response.body().string(), "result.song_info.song_list");
                List<Song> songList = new Gson().fromJson(json, new TypeToken<List<Song>>() {}.getType());
                return songList;
            }
        });
    }
}
