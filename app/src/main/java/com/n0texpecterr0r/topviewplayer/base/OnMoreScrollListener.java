package com.n0texpecterr0r.topviewplayer.base;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * @author N0tExpectErr0r
 * @DATE 创建时间: 2018/7/17
 * @DESCRIPTION 上拉加载更多Listener
 */
public abstract class OnMoreScrollListener extends RecyclerView.OnScrollListener {

    private RecyclerView mRecyclerView;

    protected OnMoreScrollListener(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
        BaseMoreAdapter adapter = (BaseMoreAdapter) mRecyclerView.getAdapter();

        if (null == manager) {
            throw new RuntimeException("you should call setLayoutManager() first!!");
        }
        if (manager instanceof LinearLayoutManager) {
            int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) manager)
                    .findLastCompletelyVisibleItemPosition();

            if (adapter.getItemCount() > 20 &&
                    lastCompletelyVisibleItemPosition == adapter.getItemCount() - 1) {
                onLoadMore();
            }
        }
    }

    public abstract void onLoadMore();
}
