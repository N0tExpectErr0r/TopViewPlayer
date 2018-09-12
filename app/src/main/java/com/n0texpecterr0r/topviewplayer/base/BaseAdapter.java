package com.n0texpecterr0r.topviewplayer.base;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * @AUTHOR nullptr
 * @DATE 创建时间: 2018/7/17
 * @DESCRIPTION
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter implements View.OnClickListener {

    private static final int TYPE_OTHER = 1;
    private static final int TYPE_BOTTOM = 2;
    private List<T> mDatas;
    private int mItemLayoutId;
    private OnItemClickListener mOnItemClickListener = null;

    public BaseAdapter(List<T> data, int itemLayoutId) {
        mDatas = data;
        mItemLayoutId = itemLayoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext()).inflate(mItemLayoutId, parent, false);
        view.setOnClickListener(this);
        return new CommonViewHolder(view);
    }

    public void setDatas(List<T> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (getItemViewType(position) != TYPE_BOTTOM) {
            T data = mDatas.get(position);
            CommonViewHolder holder = (CommonViewHolder) viewHolder;
            holder.itemView.setTag(position);
            initItemView(holder, data);
        }
        //对相应的onBindViewHolder进行处理
    }

    //需要在继承的类中实现，初始化item布局的操作
    public abstract void initItemView(CommonViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_OTHER;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    //点击事件接口
    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }
}
