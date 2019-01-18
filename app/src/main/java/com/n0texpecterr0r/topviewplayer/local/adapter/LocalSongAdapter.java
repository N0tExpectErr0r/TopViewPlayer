package com.n0texpecterr0r.topviewplayer.local.adapter;

import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.base.BaseAdapter;
import com.n0texpecterr0r.topviewplayer.base.CommonViewHolder;
import com.n0texpecterr0r.topviewplayer.bean.Song;
import com.n0texpecterr0r.topviewplayer.util.TextUtil;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/12 14:49
 * @describe TODO
 */
public class LocalSongAdapter extends BaseAdapter<Song> {

    private Map<Character, Integer> mCharIndexs;

    public LocalSongAdapter(List<Song> data, int itemLayoutId) {
        super(data, itemLayoutId);
        mCharIndexs = new HashMap<>();
    }

    @Override
    public void setDatas(List<Song> datas) {
        Collections.sort(datas, new Comparator<Song>() {
            @Override
            public int compare(Song song1, Song song2) {
                String pingyin1 = TextUtil.getPingYin(song1.getName());
                String pingyin2 = TextUtil.getPingYin(song2.getName());
                return pingyin1.compareTo(pingyin2);
            }
        });
        for (int i = 0; i < datas.size(); i++) {
            Song song = datas.get(i);
            char ch = TextUtil.getPingYin(song.getName()).charAt(0);
            if (ch >= 'A' && ch <= 'Z') {
                if (mCharIndexs.get(ch) == null) {
                    mCharIndexs.put(ch, i);
                }
            } else {
                if (mCharIndexs.get('#') == null) {
                    mCharIndexs.put('#', i);
                }
            }
        }
        super.setDatas(datas);
    }

    public Integer getIndexOfChar(char ch) {
        return mCharIndexs.get(ch);
    }

    @Override
    public void initItemView(CommonViewHolder holder, Song song) {
        holder.setText(R.id.song_tv_name, song.getName());
        holder.setText(R.id.song_tv_desc, song.getArtist() + " -《" + song.getAlbum() + "》");
    }
}
