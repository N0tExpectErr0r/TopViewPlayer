package com.n0texpecterr0r.topviewplayer.search.presenter;

import com.n0texpecterr0r.topviewplayer.base.MvpBasePresenter;
import com.n0texpecterr0r.topviewplayer.search.SearchContract.SearchPresenter;
import com.n0texpecterr0r.topviewplayer.search.SearchContract.SearchPresenterCallback;
import com.n0texpecterr0r.topviewplayer.search.SearchContract.SearchView;
import com.n0texpecterr0r.topviewplayer.search.bean.AlbumSuggestion;
import com.n0texpecterr0r.topviewplayer.search.bean.ArtistSuggestion;
import com.n0texpecterr0r.topviewplayer.search.bean.SearchSuggestion;
import com.n0texpecterr0r.topviewplayer.search.bean.SongSuggestion;
import com.n0texpecterr0r.topviewplayer.search.bean.Suggestion;
import com.n0texpecterr0r.topviewplayer.search.model.SearchModelImpl;
import java.util.ArrayList;
import java.util.List;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/6 19:22
 * @describe 搜索Presenter
 */
public class SearchPresenterImpl extends MvpBasePresenter<SearchView> implements SearchPresenter,SearchPresenterCallback {

    private SearchModelImpl mModel;

    public SearchPresenterImpl() {
        mModel = new SearchModelImpl();
    }

    @Override
    public void solveSuggestion(SearchSuggestion searchSuggestion) {
        List<Suggestion> suggestions = createSuggestions(searchSuggestion);
        if (suggestions.size() != 0) {
            mView.showSuggestions(suggestions);
        } else {
            mView.showEmpty();
        }
        mView.hideLoading();
    }

    private List<Suggestion> createSuggestions(SearchSuggestion searchSuggestion) {
        List<Suggestion> suggestions = new ArrayList<>();
        List<ArtistSuggestion> artistSuggestions = searchSuggestion.getArtistSuggestions();
        List<SongSuggestion> songSuggestions = searchSuggestion.getSongSuggestions();
        List<AlbumSuggestion> albumSuggestions = searchSuggestion.getAlbumSuggestions();

        if (artistSuggestions != null) {
            for (int i = 0; i < (artistSuggestions.size() > 3 ? 3 : artistSuggestions.size()); i++) {
                ArtistSuggestion artistSuggestion = artistSuggestions.get(i);
                suggestions.add(new Suggestion(artistSuggestion.getArtistName(), Suggestion.ARTIST));
            }
        }
        if (songSuggestions != null) {
            for (int i = 0; i < (songSuggestions.size() > 2 ? 2 : songSuggestions.size()); i++) {
                SongSuggestion songSuggestion = songSuggestions.get(i);
                suggestions.add(new Suggestion(songSuggestion.getSongName() + "-" + songSuggestion.getArtistName(),
                        Suggestion.SONG));
            }
        }
        if (albumSuggestions != null) {
            for (int i = 0; i < (albumSuggestions.size() > 1 ? 1 : albumSuggestions.size()); i++) {
                AlbumSuggestion albumSuggestion = albumSuggestions.get(i);
                suggestions.add(new Suggestion(albumSuggestion.getAlbumName() + "-" + albumSuggestion.getArtistName(),
                        Suggestion.ALBUM));
            }
        }
        return suggestions;
    }

    @Override
    public void getSuggestions(String query) {
        mView.showLoading();
        mModel.getSuggestions(query, this);
    }

    @Override
    public void error() {
        mView.showError();
        mView.hideLoading();
    }
}
