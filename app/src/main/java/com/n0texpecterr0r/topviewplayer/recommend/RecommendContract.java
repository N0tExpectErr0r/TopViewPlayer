package com.n0texpecterr0r.topviewplayer.recommend;

import com.n0texpecterr0r.topviewplayer.base.MvpBaseView;
import com.n0texpecterr0r.topviewplayer.recommend.bean.Recommend;
import com.n0texpecterr0r.topviewplayer.recommend.bean.focus.Focus;
import java.util.List;

/**
 * @author Created by Nullptr
 * @date 2018/9/8 14:13
 * @describe TODO
 */
public class RecommendContract {
    public interface RecommendView extends MvpBaseView{
        void showRecommends(List<Recommend> recommends);
        void showFocus(List<Focus> focusPics);
    }

    public interface RecommendPresenterCallback {
        void solveRecommends(List<Recommend> recommends);
        void solveFocus(List<Focus> focusPics);
        void error();
    }

    public interface RecommendPresenter {
        void getRecommends();
        void getFocus();
    }

    public interface RecommendModel {
        void getRecommends(RecommendPresenterCallback callback);
        void getFocus(RecommendPresenterCallback callback);
    }
}
