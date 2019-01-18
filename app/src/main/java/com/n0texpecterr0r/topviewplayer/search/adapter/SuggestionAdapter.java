package com.n0texpecterr0r.topviewplayer.search.adapter;

import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.base.BaseAdapter;
import com.n0texpecterr0r.topviewplayer.base.CommonViewHolder;
import com.n0texpecterr0r.topviewplayer.search.bean.Suggestion;
import java.util.List;

/**
 * @author N0tExpectErr0r
 * @date 2018/9/6 20:59
 * @describe 建议Adapter
 */
public class SuggestionAdapter extends BaseAdapter<Suggestion> {

    public SuggestionAdapter(List<Suggestion> data, int itemLayoutId) {
        super(data, itemLayoutId);
    }

    @Override
    public void initItemView(CommonViewHolder holder, Suggestion suggestion) {
        holder.setText(R.id.suggestion_tv_name,suggestion.getName());
        switch (suggestion.getType()){
            case Suggestion.SONG:
                holder.setImageResource(R.id.suggestion_iv_image,R.drawable.ic_song);
                break;
            case Suggestion.ALBUM:
                holder.setImageResource(R.id.suggestion_iv_image,R.drawable.ic_album);
                break;
            case Suggestion.ARTIST:
                holder.setImageResource(R.id.suggestion_iv_image,R.drawable.ic_artist);
                break;
        }
    }
}
