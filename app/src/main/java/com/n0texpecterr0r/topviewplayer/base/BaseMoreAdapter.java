package com.n0texpecterr0r.topviewplayer.base;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.n0texpecterr0r.topviewplayer.R;
import java.util.List;

/**
 * @AUTHOR nullptr
 * @DATE 创建时间: 2018/7/17
 * @DESCRIPTION
 */
public abstract class BaseMoreAdapter<T> extends RecyclerView.Adapter implements View.OnClickListener {

    private static final int TYPE_OTHER = 1;
    private static final int TYPE_BOTTOM = 2;
    private List<T> mDatas;
    private int mItemLayoutId;
    private int mItemCount;
    private boolean showBottom = true;
    private OnItemClickListener mOnItemClickListener = null;

    public BaseMoreAdapter(List<T> data, int itemLayoutId, int itemCount) {
        mDatas = data;
        mItemLayoutId = itemLayoutId;
        mItemCount = itemCount;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (TYPE_BOTTOM == viewType) {
            //返回加载中的布局Viewholder
            return new CommonViewHolder(LayoutInflater
                    .from(parent.getContext()).inflate(R.layout.footer_layout, parent, false));
        } else {
            //返回Viewholder
            View view = LayoutInflater
                    .from(parent.getContext()).inflate(mItemLayoutId, parent, false);
            view.setOnClickListener(this);
            return new CommonViewHolder(view);
        }
    }

    public void setDatas(List<T> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    public T getDataByIndex(int index){
        return mDatas.get(index);
    }

    public void setShowBottom(boolean showBottom) {
        this.showBottom = showBottom;
    }

    public void addDatas(List<T> datas) {
        mDatas.addAll(datas);
        Log.d("OnlineModel", datas.size() + "");
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
        if (showBottom) {
            return mDatas.size() < mItemCount ? mDatas.size() : mDatas.size() + 1;
        }else{
            return mDatas.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!mDatas.isEmpty() && mDatas.size() <= position) {
            return TYPE_BOTTOM;
        } else {
            return TYPE_OTHER;
        }
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
