package com.n0texpecterr0r.topviewplayer.recommend.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.n0texpecterr0r.topviewplayer.R;
import com.n0texpecterr0r.topviewplayer.base.CommonViewHolder;
import com.n0texpecterr0r.topviewplayer.recommend.bean.Recommend;
import com.n0texpecterr0r.topviewplayer.widget.BannerView;
import java.util.List;

/**
 * @author Created by Nullptr
 * @date 2018/9/8 12:02
 * @describe TODO
 */
public class RecommendAdapter extends RecyclerView.Adapter<CommonViewHolder> {

    final private static int TYPE_BANNER = 0x523468;
    final private static int TYPE_GEDAN = 0x454787;
    final private static int TYPE_GEDAN_HEADER = 0x5478879;
    final private static int TYPE_SONG = 0x65487979;
    final private static int TYPE_SONG_HEADER = 0x57985789;
    final private static int TYPE_ALBUM = 0x567899;
    final private static int TYPE_ALBUM_HEADER = 0x5897202;

    private Context mContext;
    private BannerView mBanner;
    private List<Recommend> mRecommends;
    private OnItemClickListener mOnItemClickListener;

    public RecommendAdapter(Context context, List<Recommend> recommends) {
        mContext = context;
        mRecommends = recommends;
    }

    public void setRecommends(List<Recommend> recommends) {
        mRecommends = recommends;
        notifyDataSetChanged();
    }

    public BannerView getBanner() {
        return mBanner;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        // 用Span Size来决定占用多少
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type) {
                        case TYPE_BANNER:
                        case TYPE_GEDAN_HEADER:
                        case TYPE_SONG_HEADER:
                        case TYPE_ALBUM_HEADER:
                            return 6;
                        case TYPE_SONG:
                        case TYPE_GEDAN:
                        case TYPE_ALBUM:
                            return 2;
                        default:
                            return 6;
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        CommonViewHolder viewHolder = null;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case TYPE_BANNER:
                view = layoutInflater.inflate(R.layout.item_banner, parent, false);
                mBanner = view.findViewById(R.id.recommend_bv_banner);
                break;
            case TYPE_SONG:
            case TYPE_GEDAN:
            case TYPE_ALBUM:
                view = layoutInflater.inflate(R.layout.item_recommend, parent, false);
                break;
            case TYPE_SONG_HEADER:
            case TYPE_GEDAN_HEADER:
            case TYPE_ALBUM_HEADER:
                view = layoutInflater.inflate(R.layout.item_recommend_header, parent, false);
                break;
            default:
                break;
        }
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() != null) {
                    int position = (int) v.getTag();
                    if (mOnItemClickListener!=null){
                        mOnItemClickListener.onItemClick(mRecommends.get(position));
                    }
                }
            }
        });
        viewHolder = new CommonViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position) {
        Recommend recommend;
        ImageView imageView;
        switch (getItemViewType(position)) {
            case TYPE_BANNER:
                holder.itemView.setTag(null);
                break;
            case TYPE_GEDAN_HEADER:
                holder.setText(R.id.recommend_tv_header, "精选歌单");
                holder.itemView.setTag(null);
                break;
            case TYPE_GEDAN:
                recommend = mRecommends.get(position - 2);  // 减去banner和header
                holder.setText(R.id.recommend_tv_title, recommend.getTitle());
                holder.setText(R.id.recommend_tv_desc, recommend.getDesc());
                imageView = holder.getView(R.id.recommend_iv_image);
                Glide.with(mContext)
                        .load(recommend.getImgUrl())
                        .placeholder(R.drawable.ic_mock)
                        .error(R.drawable.ic_mock)
                        .into(imageView);
                holder.itemView.setTag(position - 2);
                break;
            case TYPE_ALBUM_HEADER:
                holder.setText(R.id.recommend_tv_header, "热门专辑");
                holder.itemView.setTag(null);
                break;
            case TYPE_ALBUM:
                recommend = mRecommends.get(position - 3);  // 减去banner和header
                holder.setText(R.id.recommend_tv_title, recommend.getTitle());
                holder.setText(R.id.recommend_tv_desc, recommend.getDesc());
                imageView = holder.getView(R.id.recommend_iv_image);
                Glide.with(mContext)
                        .load(recommend.getImgUrl())
                        .placeholder(R.drawable.ic_mock)
                        .error(R.drawable.ic_mock)
                        .into(imageView);
                holder.itemView.setTag(position - 3);
                break;
            case TYPE_SONG_HEADER:
                holder.setText(R.id.recommend_tv_header, "新歌首发");
                holder.itemView.setTag(null);
                break;
            case TYPE_SONG:
                recommend = mRecommends.get(position - 4);  // 减去banner和header
                holder.setText(R.id.recommend_tv_title, recommend.getTitle());
                holder.setText(R.id.recommend_tv_desc, recommend.getDesc());
                imageView = holder.getView(R.id.recommend_iv_image);
                Glide.with(mContext)
                        .load(recommend.getImgUrl())
                        .placeholder(R.drawable.ic_mock)
                        .error(R.drawable.ic_mock)
                        .into(imageView);
                holder.itemView.setTag(position - 4);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        } else if (position == 1) {
            return TYPE_GEDAN_HEADER;
        } else if (position >= 2 && position <= 7) {
            return TYPE_GEDAN;
        } else if (position == 8) {
            return TYPE_ALBUM_HEADER;
        } else if (position >= 9 && position <= 14) {
            return TYPE_ALBUM;
        } else if (position == 15) {
            return TYPE_SONG_HEADER;
        } else if (position >= 16 && position <= 21) {
            return TYPE_SONG;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mRecommends.size() > 0 ? mRecommends.size() + 4 : 1;    // 加上一个Banner和四个Header
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(Recommend recommend);
    }

}
