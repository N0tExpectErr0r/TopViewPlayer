package com.n0texpecterr0r.topviewplayer.recommend.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.base.MvpBaseFragment;
import com.n0texpecterr0r.topviewplayer.recommend.RecommendContract.RecommendView;
import com.n0texpecterr0r.topviewplayer.recommend.adapter.RecommendAdapter;
import com.n0texpecterr0r.topviewplayer.recommend.adapter.RecommendAdapter.OnItemClickListener;
import com.n0texpecterr0r.topviewplayer.recommend.bean.Recommend;
import com.n0texpecterr0r.topviewplayer.recommend.bean.focus.Focus;
import com.n0texpecterr0r.topviewplayer.recommend.presenter.RecommendPresenterImpl;
import com.n0texpecterr0r.topviewplayer.widget.BannerView;
import com.n0texpecterr0r.topviewplayer.widget.BannerView.OnBannerItemClick;
import es.dmoral.toasty.Toasty;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by Nullptr
 * @date 2018/9/8 10:43
 * @describe 推荐页Fragment
 */
public class RecommendFragment extends MvpBaseFragment<RecommendPresenterImpl> implements RecommendView,
        OnItemClickListener {

    private RecyclerView mRcvPage;
    private RecommendAdapter mAdapter;
    private SwipeRefreshLayout mSrlRefresh;
    private BannerView mBvBanner;

    @Override
    public View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        mRcvPage = view.findViewById(R.id.recommend_rcv_list);
        mSrlRefresh = view.findViewById(R.id.recommend_srl_refresh);

        mPresenter.getRecommends();
        mPresenter.getFocus();

        mSrlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getRecommends();
                mPresenter.getFocus();
            }
        });

        mRcvPage.setLayoutManager(new GridLayoutManager(getContext(),
                6, GridLayoutManager.VERTICAL, false));
        mAdapter = new RecommendAdapter(getContext(), new ArrayList<Recommend>());
        mRcvPage.setAdapter(mAdapter);
        mRcvPage.post(new Runnable() {
            @Override
            public void run() {
                mBvBanner = mAdapter.getBanner();
                mBvBanner.setOnBannerItemClick(new OnBannerItemClick() {
                    @Override
                    public void onItemClick(Focus focus) {
                        Toasty.success(getContext(), focus.getUrl()).show();
                    }
                });
            }
        });
        mAdapter.setOnItemClickListener(this);

        return view;
    }

    @Override
    public RecommendPresenterImpl initPresenter() {
        return new RecommendPresenterImpl();
    }

    @Override
    public void showRecommends(List<Recommend> recommends) {
        mAdapter.setRecommends(recommends);
    }

    @Override
    public void showFocus(List<Focus> focusPics) {
        mBvBanner.setFocusList(focusPics);
    }

    @Override
    public void showLoading() {
        mSrlRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSrlRefresh.setRefreshing(false);
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showError() {
        Toasty.error(getContext(), "网络出现错误，请检查网络设置").show();
    }

    @Override
    public void onItemClick(Recommend recommend) {
        Toasty.success(getContext(),recommend.getTitle()).show();
    }
}
