package com.n0texpecterr0r.topviewplayer.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * @author N0tExpectErr0r
 * @date 创建时间: 2018/9/6 18:57
 * @describe Mvp的基类Activity
 */

public abstract class MvpBaseActivity<T extends MvpBasePresenter> extends AppCompatActivity implements MvpBaseView {

    protected T mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = onCreatePresenter();
        if (mPresenter != null) {
            mPresenter.onAttach(this);
        }
        onCreateActivity(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPresenter != null) {
            mPresenter.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDetach();
        }
    }

    /**
     * 初始化Activity
     *
     * @param savedInstanceState 保存的数据
     */
    protected abstract void onCreateActivity(Bundle savedInstanceState);

    /**
     * 初始化Presenter
     *
     * @return 返回初始化后的Presenter
     */
    protected abstract T onCreatePresenter();

    @Override
    public void showLoading(){}
    @Override
    public void hideLoading(){}
    @Override
    public void showEmpty(){}
    @Override
    public void showError(){}
}
