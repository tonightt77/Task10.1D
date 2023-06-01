package com.app.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.demo.activitys.AddOrderActivity;
import com.app.demo.activitys.OrderDetailActivity;
import com.app.demo.adapters.OrderListAdapter;
import com.app.demo.beans.OrderBean;
import com.app.demo.share.ShareUtil;
import com.app.shop.mylibrary.base.BaseActivity;
import com.app.shop.mylibrary.beans.EventMessage;
import com.app.shop.mylibrary.utils.UserManager;
import com.app.shop.mylibrary.widgts.CustomPopWindow;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.umeng.socialize.UMShareAPI;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recy)
    RecyclerView recy;
    @BindView(R.id.imgv_more)
    ImageView imgvMore;

    // Initialize order list and adapter
    List<OrderBean> list = new ArrayList<>();
    OrderListAdapter mAdapter;
    CustomPopWindow mPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        setSwipeEnabled(false);// Disable swipe
        initData();// Initialize data

    }

    @Override
    public void onEvent(EventMessage msg) {
        super.onEvent(msg);
        if (msg.getMessageType() == EventMessage.refresh) {
            initData();// Initialize data if the message type is "refresh"
        }
    }

    private void initData() {
        list.clear();
        List<OrderBean> list_all = DataSupport.findAll(OrderBean.class);// Find all OrderBean objects
        for (int i = 0; i < list_all.size(); i++) {
            if (UserManager.getUserId(this).equals(list_all.get(i).getUser_id())) {
                list.add(list_all.get(i)); // Add the order to the list
            }
        }
        Collections.reverse(list);
        mAdapter = new OrderListAdapter(R.layout.item_order_list, list);
        View empty = LayoutInflater.from(this).inflate(R.layout.view_list_empty, null);// Inflate the empty view
        mAdapter.setEmptyView(empty);
        TextView tv = empty.findViewById(R.id.tv_empty_title);
        tv.setText("Empty Order List,Please Add New Orders");
        recy.setLayoutManager(new LinearLayoutManager(this));
        recy.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("bean", list.get(position));
                bundle.putString("pos", position + "");
                showActivity(MainActivity.this, OrderDetailActivity.class, bundle);
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.imgv_share) {
                    share();
                }
            }
        });
    }

    private void share() {
        String url = "http://www.reddit.com";
        String title = "Reddit";
        String share_content = "Share on reddit";
        String thumb = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/07/Reddit_icon.svg/900px-Reddit_icon.svg.png";
        ShareUtil.share(this, share_content, title, url, thumb, 0, null);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @OnClick({R.id.imgv_more, R.id.imgv_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgv_more:
                showPop(imgvMore);
                break;
            case R.id.imgv_add:
                showActivity(this, AddOrderActivity.class);
                break;

        }
    }


    private void showPop(View view) {

        View conentView = LayoutInflater.from(this).inflate(R.layout.layout_pop, null);
        mPop = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(conentView)
                .size(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableBackgroundDark(true) //Whether the background is darkened when the popWindow pops up
                .setBgDarkAlpha(0.7f) // control brightness
                .create();


        TextView tv_home = conentView.findViewById(R.id.tv_home);
        TextView tv_account = conentView.findViewById(R.id.tv_account);
        TextView tv_order = conentView.findViewById(R.id.tv_order);
        tv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitle.setText("Home");
                mPop.dissmiss();
            }
        });
        tv_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dissmiss();
            }
        });
        tv_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dissmiss();
                tvTitle.setText("My Order");
            }
        });

        if (mPop != null) {
            mPop.showAsDropDown(view, 0, 0);
        }
    }
}
