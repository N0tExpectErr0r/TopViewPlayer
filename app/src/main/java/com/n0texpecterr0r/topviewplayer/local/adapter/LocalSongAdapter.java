package com.n0texpecterr0r.topviewplayer.local.adapter;

import android.util.Log;
import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.base.BaseAdapter;
import com.n0texpecterr0r.topviewplayer.base.CommonViewHolder;
import com.n0texpecterr0r.topviewplayer.local.bean.LocalSong;
import com.n0texpecterr0r.topviewplayer.util.TextUtil;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Created by Nullptr
 * @date 2018/9/12 14:49
 * @describe TODO
 */
public class LocalSongAdapter extends BaseAdapter<LocalSong> {

    private Map<Character, Integer> mCharIndexs;

    public LocalSongAdapter(List<LocalSong> data, int itemLayoutId) {
        super(data, itemLayoutId);
        mCharIndexs = new HashMap<>();
    }

    @Override
    public void setDatas(List<LocalSong> datas) {
        Collections.sort(datas, new Comparator<LocalSong>() {
            @Override
            public int compare(LocalSong song1, LocalSong song2) {
                String pingyin1 = TextUtil.getPingYin(song1.getName());
                String pingyin2 = TextUtil.getPingYin(song2.getName());
                return pingyin1.compareTo(pingyin2);
            }
        });
        for (int i = 0; i < datas.size(); i++) {
            LocalSong song = datas.get(i);
            char ch = TextUtil.getPingYin(song.getName()).charAt(0);
            if (ch >= 'A' && ch <= 'Z') {
                if (mCharIndexs.get(ch) == null) {
                    Log.d("PinYin", ch+"");
                    mCharIndexs.put(ch, i);
                }
            }else{
                if (mCharIndexs.get('#') == null) {
                    Log.d("PinYin", "#");
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
    public void initItemView(CommonViewHolder holder, LocalSong song) {
        holder.setText(R.id.song_tv_name, song.getName());
        holder.setText(R.id.song_tv_desc, song.getArtist() + " -《" + song.getAlbum() + "》");
    }
}
