package com.n0texpecterr0r.topviewplayer.base;

import android.os.Bundle;
import java.lang.ref.WeakReference;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/6 18:25
 * @describe Mvp Presenter基类
 */
public abstract class MvpBasePresenter<T extends MvpBaseView> {

    protected T mView;

    /**
     * 在Activity的onCreate中调用，绑定view
     *
     * @param view 要绑定的View
     */
    public void onAttach(T view) {
        mView = view;
    }

    /**
     * Activity调用onResume时的操作
     */
    public void onResume() {
    }

    /**
     * Activity调用onPause时的操作
     */
    public void onPause() {
    }

    /**
     * 在Activity的onDestroy中调用，解绑View
     */
    public void onDetach() {
        if (mView != null) {
            mView = null;
        }
    }

    /**
     * 在Activity被回收掉时保存数据
     */
    public void onSaveInstanceState(Bundle outState){

    }

}
