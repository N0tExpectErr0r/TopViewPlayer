package com.n0texpecterr0r.topviewplayer.bottom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.n0texpecterr0r.topviewplayer.player.AudioPlayer;
import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.bean.Song;
import com.n0texpecterr0r.topviewplayer.detail.view.DetailActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/19 13:35
 * @describe TODO
 */
public class BottomFragment extends Fragment implements OnClickListener {
    private LinearLayout mLlBottomBar;
    private ImageView mIvCover;
    private TextView mTvName;
    private TextView mTvArtist;
    private ImageView mIvAction;


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
        mLlBottomBar = view.findViewById(R.id.bottom_ll_bottombar);
        mIvAction.setOnClickListener(this);
        mLlBottomBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AudioPlayer.get().isSongListEmpty()) {
                    DetailActivity.actionStart(getContext());
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        if (AudioPlayer.get().isInited() && !AudioPlayer.get().isSongListEmpty()) {
            // 恢复歌曲信息数据
            Song song = AudioPlayer.get().getCurrentSong();
            Glide.with(this)
                    .load(song.getImgUrl())
                    .placeholder(R.drawable.ic_empty)
                    .error(R.drawable.ic_empty)
                    .into(mIvCover);
            mTvName.setText(song.getName());
            mTvArtist.setText(song.getArtist());
            mIvAction.setImageResource(AudioPlayer.get().isPlaying()?
                            R.drawable.ic_pause:R.drawable.ic_play);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeSong(Song song) {
        mTvName.setText(song.getName());
        mTvArtist.setText(song.getArtist());
        mIvAction.setImageResource(R.drawable.ic_pause);
        Glide.with(this)
                .load(song.getImgUrl())
                .error(R.drawable.ic_empty)
                .dontAnimate()
                .into(mIvCover);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        if (!AudioPlayer.get().isSongListEmpty()) {
            if (AudioPlayer.get().isPlaying()) {
                AudioPlayer.get().pause();
                mIvAction.setImageResource(R.drawable.ic_play);
            } else {
                AudioPlayer.get().resume();
                mIvAction.setImageResource(R.drawable.ic_pause);
            }
        }
    }
}
