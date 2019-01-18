package com.n0texpecterr0r.topviewplayer.search.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.SearchView.SearchAutoComplete;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.base.MvpBaseActivity;
import com.n0texpecterr0r.topviewplayer.online.view.OnlineActivity;
import com.n0texpecterr0r.topviewplayer.search.SearchContract.SearchView;
import com.n0texpecterr0r.topviewplayer.search.adapter.SuggestionAdapter;
import com.n0texpecterr0r.topviewplayer.search.bean.Suggestion;
import com.n0texpecterr0r.topviewplayer.search.presenter.SearchPresenterImpl;
import es.dmoral.toasty.Toasty;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends MvpBaseActivity<SearchPresenterImpl> implements SearchView {

    private RecyclerView mRvSuggestions;
    private android.support.v7.widget.SearchView mSvSearch;
    private SuggestionAdapter mAdapter;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search_search);
        // 获取SearchView
        mSvSearch = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
        mSvSearch.setQueryHint("搜索网络歌曲");
        mSvSearch.setIconifiedByDefault(false);
        mSvSearch.setSubmitButtonEnabled(true);
        mSvSearch.setMaxWidth(5000);
        mSvSearch.setBackgroundResource(R.drawable.searchview_card);
        mSvSearch.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!mSvSearch.getQuery().equals("")){
                    OnlineActivity.actionStart(SearchActivity.this,query);
                }else{
                    Toasty.warning(SearchActivity.this,"未填写搜索信息").show();
                }
                finish();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPresenter.getSuggestions(newText);
                return false;
            }
        });
        // 设置字体大小
        SearchAutoComplete searchTextArea =
                mSvSearch.findViewById(R.id.search_src_text);
        searchTextArea.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        // 初始化控件
        Toolbar toolbar = findViewById(R.id.search_toolbar);
        mRvSuggestions = findViewById(R.id.search_rv_suggestions);
        mRvSuggestions.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SuggestionAdapter(new ArrayList<Suggestion>(), R.layout.item_suggestion);
        mRvSuggestions.setAdapter(mAdapter);

        setSupportActionBar(toolbar);
    }

    @Override
    protected SearchPresenterImpl onCreatePresenter() {
        return new SearchPresenterImpl();
    }

    @Override
    public void showSuggestions(List<Suggestion> suggestions) {
        mRvSuggestions.setVisibility(View.VISIBLE);
        mAdapter.setDatas(suggestions);
    }

    @Override
    public void showError() {
        super.showError();
        Toasty.error(this, "网络出现错误，请检查网络设置 ").show();
    }

    @Override
    public void showEmpty() {
        super.showEmpty();
        mRvSuggestions.setVisibility(View.GONE);
    }
}
