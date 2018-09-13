package com.n0texpecterr0r.topviewplayer.online.adapter;

import android.provider.MediaStore;
import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.base.BaseMoreAdapter;
import com.n0texpecterr0r.topviewplayer.base.CommonViewHolder;
import com.n0texpecterr0r.topviewplayer.online.bean.OnlineSong;
import java.util.List;

/**
 * @author Created by Nullptr
 * @date 2018/9/13 20:34
 * @describe TODO
 */
public class OnlineAdapter extends BaseMoreAdapter<OnlineSong> {

    public OnlineAdapter(List<OnlineSong> data, int itemLayoutId, int itemCount) {
        super(data, itemLayoutId, itemCount);
    }

    @Override
    public void initItemView(CommonViewHolder holder, OnlineSong song) {
        holder.setText(R.id.song_tv_name, song.getName());
        if (song.getAlbum().equals("")) {
            holder.setText(R.id.song_tv_desc, song.getArtist());
        } else {
            holder.setText(R.id.song_tv_desc, song.getArtist() + " -《" + song.getAlbum() + "》");
        }
    }
}
