package com.n0texpecterr0r.topviewplayer.main.view;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.TabLayoutOnPageChangeListener;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.local.view.LocalFragment;
import com.n0texpecterr0r.topviewplayer.main.adapter.ViewPagerAdapter;
import com.n0texpecterr0r.topviewplayer.recommend.view.RecommendFragment;
import com.n0texpecterr0r.topviewplayer.search.view.SearchActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTlTab;
    private ViewPager mVpPager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_search:
                SearchActivity.actionStart(MainActivity.this);
                break;
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化控件
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        mTlTab = findViewById(R.id.main_tb_tab);
        mVpPager = findViewById(R.id.main_vp_pager);

        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted->{
                    if (!granted)
                        Toasty.warning(this, "无文件权限，加载本地音乐可能失败");
                    List<Fragment> fragments = new ArrayList<>();
                    fragments.add(new RecommendFragment());
                    fragments.add(new LocalFragment());
                    ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                    pagerAdapter.setFragments(fragments);
                    mVpPager.setAdapter(pagerAdapter);
                    mVpPager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(mTlTab));
                    mTlTab.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mVpPager));
                });

        setSupportActionBar(toolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
