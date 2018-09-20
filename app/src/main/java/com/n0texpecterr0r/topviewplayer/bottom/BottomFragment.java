package com.n0texpecterr0r.topviewplayer.bottom;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.n0texpecterr0r.topviewplayer.IPlayerService;
import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.base.AbstractListManager;
import com.n0texpecterr0r.topviewplayer.base.Song;
import com.n0texpecterr0r.topviewplayer.local.bean.LocalSong;
import com.n0texpecterr0r.topviewplayer.online.bean.OnlineSong;
import com.n0texpecterr0r.topviewplayer.player.PlayerService;
import com.n0texpecterr0r.topviewplayer.util.ListManager;
import com.n0texpecterr0r.topviewplayer.util.LocalListManager;
import com.n0texpecterr0r.topviewplayer.util.OnlineListManager;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author Created by Nullptr
 * @date 2018/9/19 13:35
 * @describe TODO
 */
public class BottomFragment extends Fragment implements OnClickListener {

    private ImageView mIvCover;
    private TextView mTvName;
    private TextView mTvArtist;
    private ImageView mIvAction;
    private IPlayerService mPlayerService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayerService = IPlayerService.Stub.asInterface(service);

            try {
                if (mPlayerService.isPlaying()) {
                    mIvAction.setImageResource(R.drawable.ic_pause);
                } else {
                    mIvAction.setImageResource(R.drawable.ic_play);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPlayerService = null;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom, container, false);
        mIvCover = view.findViewById(R.id.bottom_iv_cover);
        mIvAction = view.findViewById(R.id.bottom_iv_action);
        mTvName = view.findViewById(R.id.bottom_tv_name);
        mTvArtist = view.findViewById(R.id.bottom_tv_artist);

        mIvAction.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initService();
        initView();
    }

    private void initService() {
        // 绑定服务
        Intent intent = new Intent(getContext(), PlayerService.class);
        getContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void initView(){
        AbstractListManager manager = ListManager.getCurrentManager();
        if (manager != null && !manager.isEmpty()) {
            // 恢复歌曲信息数据
            if (manager instanceof LocalListManager) {
                LocalSong song = ((LocalListManager) manager).getCurrentSong();
                mTvArtist.setText(song.getArtist());
                Glide.with(this).load(song.getImgUrl()).into(mIvCover);
                mTvName.setText(song.getName());
                mTvArtist.setText(song.getArtist());
            } else {
                OnlineSong song = ((OnlineListManager) manager).getCurrentSong();
                Glide.with(this).load(song.getImgUrl()).into(mIvCover);
                mTvName.setText(song.getName());
                mTvArtist.setText(song.getArtist());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeSong(Song song) {
        Glide.with(this).load(song.getImgUrl()).into(mIvCover);
        mTvName.setText(song.getName());
        mTvArtist.setText(song.getArtist());
        mIvAction.setImageResource(R.drawable.ic_pause);
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unbindService(mConnection);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        try {
            if (mPlayerService.isPlaying()) {
                mPlayerService.pause();
                mIvAction.setImageResource(R.drawable.ic_play);
            } else {
                mPlayerService.start();
                mIvAction.setImageResource(R.drawable.ic_pause);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
