package com.n0texpecterr0r.topviewplayer.recommend.model;

import static com.n0texpecterr0r.topviewplayer.ContextApplication.USER_AGENT;

import android.annotation.SuppressLint;
import api.MusicApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.n0texpecterr0r.topviewplayer.recommend.RecommendContract.RecommendModel;
import com.n0texpecterr0r.topviewplayer.recommend.RecommendContract.RecommendPresenterCallback;
import com.n0texpecterr0r.topviewplayer.recommend.bean.AlbumRecommend;
import com.n0texpecterr0r.topviewplayer.recommend.bean.GeDanRecommend;
import com.n0texpecterr0r.topviewplayer.recommend.bean.Recommend;
import com.n0texpecterr0r.topviewplayer.recommend.bean.SongRecommend;
import com.n0texpecterr0r.topviewplayer.recommend.bean.SongRecommendBean;
import com.n0texpecterr0r.topviewplayer.recommend.bean.focus.Focus;
import com.n0texpecterr0r.topviewplayer.recommend.bean.focus.FocusResponse;
import com.n0texpecterr0r.topviewplayer.util.JsonUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/8 14:12
 * @describe 推荐Model
 */
public class RecommendModelImpl implements RecommendModel {

    @SuppressLint("CheckResult")
    @Override
    public void getRecommends(final RecommendPresenterCallback callback) {
        final Observable songObservable = createSongObservable();
        Observable albumObservable = createAlbumObservable();
        final Observable geDanObservable = createGeDanObservable();
        Observable.zip(geDanObservable, albumObservable, songObservable,
                new Function3<List<GeDanRecommend>, List<AlbumRecommend>, List<SongRecommend>, List<Recommend>>() {
                    @Override
                    public List<Recommend> apply(List<GeDanRecommend> geDanRecommends,
                            List<AlbumRecommend> albumRecommends, List<SongRecommend> songRecommends) throws Exception {
                        return getRecommends(geDanRecommends, albumRecommends, songRecommends);
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Recommend>>() {
                    @Override
                    public void accept(List<Recommend> recommends) throws Exception {
                        callback.solveRecommends(recommends);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                        callback.error();
                    }
                });
    }

    @Override
    public void getFocus(final RecommendPresenterCallback callback) {
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(MusicApi.focusPic(6))
                        .addHeader("User-Agent",USER_AGENT)
                        .get()
                        .build();
                Call call = client.newCall(request);
                Response response = call.execute();
                emitter.onNext(response);
            }
        }).map(new Function<Response, List<Focus>>() {
            @Override
            public List<Focus> apply(Response response) throws Exception {
                String json = JsonUtil.getNodeString(response.body().string(),"pic");
                return new Gson().fromJson(json, new TypeToken<List<Focus>>(){}.getType());
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Focus>>() {
                    @Override
                    public void accept(List<Focus> focusList) throws Exception {
                        callback.solveFocus(focusList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        callback.error();
                    }
                });
    }

    private List<Recommend> getRecommends(List<GeDanRecommend> geDanRecommends,
            List<AlbumRecommend> albumRecommends, List<SongRecommend> songRecommends) {
        List<Recommend> recommends = new ArrayList<>();
        for (GeDanRecommend geDanRecommend : geDanRecommends) {
            Recommend recommend = new Recommend();
            recommend.setTitle(geDanRecommend.getTitle());
            recommend.setDesc(geDanRecommend.getDesc());
            recommend.setId(geDanRecommend.getListId());
            recommend.setImgUrl(geDanRecommend.getImgUrl());
            recommend.setType(Recommend.TYPE_GEDAN);
            recommends.add(recommend);
        }
        for (AlbumRecommend albumRecommend : albumRecommends) {
            Recommend recommend = new Recommend();
            recommend.setTitle(albumRecommend.getTitle());
            recommend.setDesc(albumRecommend.getDesc());
            recommend.setId(albumRecommend.getAlbumId());
            recommend.setImgUrl(albumRecommend.getImgUrl());
            recommend.setType(Recommend.TYPE_ALBUM);
            recommends.add(recommend);
        }
        for (SongRecommend songRecommend : songRecommends) {
            Recommend recommend = new Recommend();
            recommend.setTitle(songRecommend.getTitle());
            recommend.setDesc(songRecommend.getDesc());
            recommend.setId(songRecommend.getSongId());
            recommend.setImgUrl(songRecommend.getImgUrl());
            recommend.setType(Recommend.TYPE_SONG);
            recommends.add(recommend);
        }
        return recommends;
    }

    private Observable createGeDanObservable() {
        return Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(MusicApi.GeDan.hotGeDan(6))
                        .addHeader("User-Agent",USER_AGENT)
                        .get()
                        .build();
                Call call = client.newCall(request);
                Response response = call.execute();
                emitter.onNext(response);
            }
        }).map(new Function<Response, List<GeDanRecommend>>() {
            @Override
            public List<GeDanRecommend> apply(Response response) throws Exception {
                String json = JsonUtil.getNodeString(response.body().string(), "content.list");
                List<GeDanRecommend> geDanRecommends = new Gson()
                        .fromJson(json, new TypeToken<List<GeDanRecommend>>() {
                        }.getType());
                return geDanRecommends;
            }
        });
    }

    private Observable createAlbumObservable() {
        return Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(MusicApi.Album.recommendAlbum(0, 6))
                        .addHeader("User-Agent",USER_AGENT)
                        .get()
                        .build();
                Call call = client.newCall(request);
                Response response = call.execute();
                emitter.onNext(response);
            }
        }).map(new Function<Response, List<AlbumRecommend>>() {
            @Override
            public List<AlbumRecommend> apply(Response response) throws Exception {
                String json = JsonUtil.getNodeString(response.body().string(), "plaze_album_list.RM.album_list.list");
                List<AlbumRecommend> albumRecommends = new Gson()
                        .fromJson(json, new TypeToken<List<AlbumRecommend>>() {
                        }.getType());
                return albumRecommends;
            }
        });
    }

    private Observable createSongObservable() {
        return Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(MusicApi.Song.recommendSong(6))
                        .addHeader("User-Agent",USER_AGENT)
                        .get()
                        .build();
                Call call = client.newCall(request);
                Response response = call.execute();
                emitter.onNext(response);
            }
        }).map(new Function<Response, List<SongRecommend>>() {
            @Override
            public List<SongRecommend> apply(Response response) throws Exception {
                String json = JsonUtil.getNodeString(response.body().string(), "content");
                List<SongRecommendBean> songRecommendResponses = new Gson()
                        .fromJson(json, new TypeToken<List<SongRecommendBean>>() {
                        }.getType());
                List<SongRecommend> songRecommends = songRecommendResponses
                        .get(0)
                        .getSongRecommends();
                return songRecommends;
            }
        });
    }
}
