package com.app.demo.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.demo.AppManager;
import com.app.demo.MainActivity;
import com.app.demo.R;
import com.app.demo.beans.OrderBean;
import com.app.shop.mylibrary.base.BaseActivity;
import com.app.shop.mylibrary.utils.GlideUtils;
import com.app.shop.mylibrary.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.imgv)
    ImageView imgv;
    @BindView(R.id.tv_user_from)
    TextView tv_user_from;
    @BindView(R.id.tv_time_1)
    TextView tv_time_1;
    @BindView(R.id.tv_user_to)
    TextView tv_user_to;
    @BindView(R.id.tv_time_2)
    TextView tv_time_2;
    @BindView(R.id.tv_goods_type)
    TextView tv_goods_type;
    @BindView(R.id.tv_vehicle_type)
    TextView tv_vehicle_type;
    @BindView(R.id.tv_weight)
    TextView tv_weight;
    @BindView(R.id.tv_wight)
    TextView tv_wight;
    @BindView(R.id.tv_length)
    TextView tv_length;
    @BindView(R.id.tv_height)
    TextView tv_height;


    OrderBean bean;
    String pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initData();
        tvTitle.setText("Order Details");
    }


    private void initData() {

        Bundle bundle = getIntent().getExtras();
        bean = (OrderBean) bundle.getParcelable("bean");
        pos =  bundle.getString("pos");
        GlideUtils.getInstance().loadImage(this, bean.getImg(), imgv, R.mipmap.ic_launcher);
        tv_user_from.setText("From sender:" + bean.getUser_name());
        tv_user_to.setText("To receiver:" + bean.getUser_to());
        tv_time_1.setText("Pick up time:" + bean.getTime());
        tv_time_2.setText("Drop off time:");
        tv_goods_type.setText("Goods Type:\n" + bean.getGoodsType());
        tv_vehicle_type.setText("vehicle Type:\n" + bean.getVehicleType());
        tv_weight.setText("weight:\n" + bean.getWeight() + " kg");
        tv_wight.setText("width:\n" + bean.getWight() + " m");
        tv_length.setText("Length:\n" + bean.getLength() + " m");
        tv_height.setText("Height:\n" + bean.getHeight() + " m");


    }

    @OnClick({R.id.imgv_return, R.id.tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgv_return:
                onBackPressed();
                break;

            case R.id.tv_ok:
                Bundle bundle = new Bundle();
                bundle.putParcelable("bean", bean);
                bundle.putString("pos", pos);
                showActivity(OrderDetailActivity.this, LocationActivity.class, bundle);
                break;
        }
    }


}
