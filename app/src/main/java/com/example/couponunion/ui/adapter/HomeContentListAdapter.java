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

    private List<HomePagerContent.DataBean> contentList = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_content,parent,false);
        return new InnerHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        HomePagerContent.DataBean itemContent = contentList.get(position);
        holder.setData(itemContent);
    }

    /**
     * 设置列表的数据
     * @param contents
     */
    public void setData(List<HomePagerContent.DataBean> contents) {
        this.contentList.clear();
        contentList.addAll(contents);
        notifyDataSetChanged();
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
            String coverUrl = UrlUtils.getCoverPath(itemContent.getPict_url());
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
            sellCountTv.setText(String.format(context.getResources().getString(R.string.text_goods_sell_count),sellCount));

        }
    }
}
