package com.n0texpecterr0r.topviewplayer.search;

import com.n0texpecterr0r.topviewplayer.base.MvpBaseView;
import com.n0texpecterr0r.topviewplayer.search.bean.SearchSuggestion;
import com.n0texpecterr0r.topviewplayer.search.bean.Suggestion;
import java.util.List;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/6 19:03
 * @describe Search模块的Contract类
 */
public class SearchContract {
    public interface SearchView extends MvpBaseView {
        void showSuggestions(List<Suggestion> suggestions);
    }

    public interface SearchPresenterCallback {
        void solveSuggestion(SearchSuggestion searchSuggestion);
        void error();
    }

    public interface SearchPresenter {
        void getSuggestions(String query);
    }

    public interface SearchModel {
        void getSuggestions(String query,SearchPresenterCallback callback);
    }
}
