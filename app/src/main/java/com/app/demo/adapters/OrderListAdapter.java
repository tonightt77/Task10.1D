package com.app.demo.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.app.demo.R;
import com.app.demo.beans.OrderBean;
import com.app.shop.mylibrary.utils.GlideUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class OrderListAdapter extends BaseQuickAdapter<OrderBean, OrderListAdapter.ViewHolder> {


    public OrderListAdapter(int layout, @Nullable List<OrderBean> data) {
        super(layout, data);
    }

    @Override
    protected void convert(ViewHolder holder, OrderBean bean) {
        holder.tv_title.setText("OrderId:" + bean.getO_id());
        holder.tv_content.setText(bean.getGoodsType());
        GlideUtils.getInstance().loadImage(mContext, bean.getImg(), holder.imgv);
        holder.addOnClickListener(R.id.imgv_share);
    }


    public class ViewHolder extends BaseViewHolder {
        TextView tv_title;
        TextView tv_content;
        ImageView imgv;
        ImageView imgv_share;


        public ViewHolder(View view) {
            super(view);
            tv_title = view.findViewById(R.id.tv_title);
            tv_content = view.findViewById(R.id.tv_content);
            imgv = view.findViewById(R.id.imgv);
            imgv_share = view.findViewById(R.id.imgv_share);
        }
    }

}
