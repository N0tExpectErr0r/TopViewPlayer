package com.n0texpecterr0r.topviewplayer.online.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.base.MvpBaseActivity;
import com.n0texpecterr0r.topviewplayer.base.MvpBasePresenter;
import com.n0texpecterr0r.topviewplayer.online.OnlineContract.OnlineView;
import com.n0texpecterr0r.topviewplayer.online.bean.OnlineSong;
import com.n0texpecterr0r.topviewplayer.online.presenter.OnlinePresenterImpl;
import java.util.List;

public class OnlineActivity extends MvpBaseActivity<OnlinePresenterImpl> implements OnlineView {
    private RecyclerView mRcvList;
    private String query;
    private int pageNo;

    public static void actionStart(Context context,String query){
        Intent intent = new Intent(context,OnlineActivity.class);
        intent.putExtra("query",query);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_result);
        mRcvList = findViewById(R.id.result_rcv_list);

        pageNo = 0;

        Intent intent = getIntent();
        query = intent.getStringExtra("query");
    }

    @Override
    protected OnlinePresenterImpl initPresenter() {
        return null;
    }

    @Override
    public void addSong(List<OnlineSong> songList) {

    }

    @Override
    public void loadCompelete() {

    }
}
