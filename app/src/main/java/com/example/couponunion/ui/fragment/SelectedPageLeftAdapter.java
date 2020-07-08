package com.example.couponunion.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.couponunion.R;
import com.example.couponunion.model.domain.SelecetdPageCategory;
import com.example.couponunion.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectedPageLeftAdapter extends RecyclerView.Adapter<SelectedPageLeftAdapter.InnerHolder> {

    private List<SelecetdPageCategory.DataBean> mDataList = new ArrayList<>();

    private int mCurrentPosition = 0;

    private OnLeftItemClickListener mListener;

    private int countNum = 0;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_left, parent, false);
        countNum++;
        //TODO 问题 :每次切换点击目标都会重新执行此方法？
        LogUtils.d(this, "onCreateViewHolder ==> " + countNum);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        SelecetdPageCategory.DataBean dataBean = mDataList.get(position);
        holder.mTitleTv.setText(dataBean.getFavorites_title());
        if (mCurrentPosition == position) {
            holder.mTitleTv.setBackgroundColor(holder.mTitleTv.getResources().getColor(R.color.colorEEEEEE, null));
        } else {
            holder.mTitleTv.setBackgroundColor(holder.mTitleTv.getResources().getColor(R.color.white, null));
        }
        holder.mTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当左侧分类的item被点击后回调给监听
                if (mCurrentPosition != position && mListener != null) {
                    mCurrentPosition = position;
                    mListener.onLeftItemClick(dataBean);
                    notifyDataSetChanged();
                }
            }
        });
    }

    public void setOnLeftItemClickListener(OnLeftItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 设置数据
     *
     * @param categories
     */
    public void setData(SelecetdPageCategory categories) {
        if (categories.getData() != null) {
            mDataList.clear();
            mDataList.addAll(categories.getData());
            //切记要通知更新
            notifyDataSetChanged();
        }
        if(mDataList.size() > 0) {
            mListener.onLeftItemClick(mDataList.get(mCurrentPosition));
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        TextView mTitleTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.left_category_tv);
        }
    }

    public interface OnLeftItemClickListener {
        void onLeftItemClick(SelecetdPageCategory.DataBean content);
    }
}
