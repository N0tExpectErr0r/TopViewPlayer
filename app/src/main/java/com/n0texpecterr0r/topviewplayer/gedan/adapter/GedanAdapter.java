package com.n0texpecterr0r.topviewplayer.gedan.adapter;

import android.support.v7.widget.RecyclerView;

import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.base.BaseAdapter;
import com.n0texpecterr0r.topviewplayer.base.CommonViewHolder;
import com.n0texpecterr0r.topviewplayer.bean.Song;

import java.util.List;

public class GedanAdapter extends BaseAdapter<Song> {

    public GedanAdapter(List<Song> data, int itemLayoutId) {
        super(data, itemLayoutId);
    }

    @Override
    public void initItemView(CommonViewHolder holder, Song song) {
        holder.setText(R.id.song_tv_name, song.getName());
        if (song.getAlbum().equals("")) {
            holder.setText(R.id.song_tv_desc, song.getArtist());
        } else {
            holder.setText(R.id.song_tv_desc, song.getArtist() + " -《" + song.getAlbum() + "》");
        }
    }
}
