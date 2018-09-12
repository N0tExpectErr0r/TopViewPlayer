package com.n0texpecterr0r.topviewplayer.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Created by Nullptr
 * @date 2018/9/6 18:50
 * @describe Mvp的基类Fragment
 */
public abstract class MvpBaseFragment<T extends MvpBasePresenter> extends Fragment implements MvpBaseView {
    protected T mPresenter;
    protected Context mContext; // activity的上下文对象
    protected Bundle mBundle;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mBundle != null) {
            outState.putBundle("bundle", mBundle);
        }
    }
    /**
     * 绑定activity
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    /**
     * 运行在onAttach之后
     * 可以接受别人传递过来的参数,实例化对象.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取bundle,并保存起来
        if (savedInstanceState != null) {
            mBundle = savedInstanceState.getBundle("bundle");
        } else {
            mBundle = getArguments() == null ? new Bundle() : getArguments();
        }
        //创建presenter
        mPresenter = initPresenter();
        mPresenter.onAttach(this);
    }

    /**
     * 运行在onCreate之后
     * 生成view视图
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return onCreateFragmentView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.onDetach();
    }

    public abstract View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    public abstract T initPresenter();

    @Override
    public Context getContext() {
        return mContext;
    }
}
