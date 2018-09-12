package com.n0texpecterr0r.topviewplayer.base;

/**
 * @author Created by Nullptr
 * @date 2018/9/6 18:26
 * @describe Mvp View基类
 */
public interface MvpBaseView {

    /**
     * 显示加载效果
     */
    public void showLoading();

    /**
     * 隐藏加载效果
     */
    public void hideLoading();

    /**
     * 展示空内容效果
     */
    public void showEmpty();

    /**
     * 展示错误效果
     */
    public void showError();
}
