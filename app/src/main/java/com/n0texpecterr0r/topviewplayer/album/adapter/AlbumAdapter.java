package com.n0texpecterr0r.topviewplayer.album.adapter;

import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.base.BaseAdapter;
import com.n0texpecterr0r.topviewplayer.base.CommonViewHolder;
import com.n0texpecterr0r.topviewplayer.bean.Song;

import java.util.List;

/**
 * 描述
 *
 * @author N0tExpectErr0r
 * @time 2019/03/29
 */
public class AlbumAdapter extends BaseAdapter<Song> {
    public AlbumAdapter(List<Song> data, int itemLayoutId) {
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
