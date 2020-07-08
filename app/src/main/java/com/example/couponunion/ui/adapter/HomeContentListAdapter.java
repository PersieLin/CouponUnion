package com.example.couponunion.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.couponunion.R;
import com.example.couponunion.model.domain.HomePagerContent;
import com.example.couponunion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeContentListAdapter extends RecyclerView.Adapter<HomeContentListAdapter.InnerHolder> {

    private List<HomePagerContent.DataBean> mData = new ArrayList<>();
    private OnHomePagerItemClickListener itemClickListener;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_content, parent, false);
//        LogUtil.d(this, "onCreateViewHolder...");
        return new InnerHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        HomePagerContent.DataBean itemContent = mData.get(position);
//        LogUtil.d(this, "onBindViewHolder...  -->" + position);
        holder.setData(itemContent);
        //设置item的点击监听
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null){
                    itemClickListener.onHomePagerItemClick(itemContent);
                }
            }
        });
    }

    public void setItemClickListener(OnHomePagerItemClickListener listener){
        this.itemClickListener = listener;
    }


    /**
     * 设置列表的数据
     *
     * @param contents
     */
    public void setData(List<HomePagerContent.DataBean> contents) {
        this.mData.clear();
        mData.addAll(contents);
        notifyDataSetChanged();
    }

    public void addData(List<HomePagerContent.DataBean> content) {
        int olderSize = mData.size();
        mData.addAll(content);
        //避免从头开始刷新，调用范围更新的方法
        notifyItemRangeChanged(olderSize, content.size());

    }

    static class InnerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.goods_cover)
        ImageView coverIv;

        @BindView(R.id.goods_title)
        TextView titleTv;

        @BindView(R.id.goods_off_prise)
        TextView offPriceTv;

        @BindView(R.id.goods_after_off_prise)
        TextView totalPriceTv;

        @BindView(R.id.goods_original_prise)
        TextView originalPriceTv;


        @BindView(R.id.goods_sell_count)
        TextView sellCountTv;


        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(HomePagerContent.DataBean itemContent) {
            Context context = itemView.getContext();
            //动态调整请求图片的size，根据需求请求对应大小的图片，优化图片大小和节省流量
            ViewGroup.LayoutParams layoutParams = coverIv.getLayoutParams();
            int width = layoutParams.width;
            int height = layoutParams.height;
            int ivSize = (Math.max(width, height)) / 2;
            String coverUrl = UrlUtils.getCoverPath(itemContent.getPict_url(), ivSize);
//            LogUtil.d("coverUrl-->", coverUrl);

            Glide.with(context).load(coverUrl).into(coverIv);
            titleTv.setText(itemContent.getTitle());
            long offPrice = itemContent.getCoupon_amount();
            offPriceTv.setText(String.format(context.getString(R.string.text_goods_off_price), offPrice));
            String originalPrice = itemContent.getZk_final_price();
            float totalPrice = Float.parseFloat(originalPrice) - offPrice;
            originalPriceTv.setText(originalPrice);
            //添加横划线
            originalPriceTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            totalPriceTv.setText(String.format(context.getString(R.string.text_goods_total_price), totalPrice));
            long sellCount = itemContent.getVolume();
            sellCountTv.setText(String.format(context.getResources().getString(R.string.text_goods_sell_count), sellCount));

        }
    }

    public interface OnHomePagerItemClickListener{
        void onHomePagerItemClick(HomePagerContent.DataBean itemContent);
    }
}
