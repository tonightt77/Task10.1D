package com.app.demo.activitys;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.demo.AppManager;
import com.app.demo.MainActivity;
import com.luoshihai.xxdialog.DialogViewHolder;
import com.luoshihai.xxdialog.XXDialog;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;
import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.app.demo.R;
import com.app.demo.beans.OrderBean;
import com.app.demo.service.OrderInfoUtil2_0;
import com.app.demo.service.PayPopupWindow;
import com.app.shop.mylibrary.base.BaseActivity;
import com.app.shop.mylibrary.utils.GlideUtils;
import com.luck.picture.lib.tools.ToastUtils;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnBindViewListener;
import com.timmy.tdialog.listener.OnViewClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayActivity extends BaseActivity {

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
    @BindView(R.id.ll_authorized)
    LinearLayout ll;
    OrderBean bean;
    private static final int SDK_PAY_FLAG = 1;

    private String money;
    private LinearLayout a;
    private LoadingDialog ld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initData();
        tvTitle.setText("Order Details");

        XXDialog xxDialog = new XXDialog(this, R.layout.dialog_click) {
            @Override
            public void convert(DialogViewHolder holder) {
                LinearLayout zfb = holder.getView(R.id.zfb);
                zfb.setOnClickListener(view -> {
                    ll.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ll.setVisibility(View.GONE);
                            new TDialog.Builder(getSupportFragmentManager()).setLayoutRes(R.layout.pay_popu_window).setScreenWidthAspect(PayActivity.this, 1f).setGravity(Gravity.BOTTOM).addOnClickListener(R.id.btn_close_popupwindow, R.id.btn_confirm_pay).setOnBindViewListener(new OnBindViewListener() {
                                @Override
                                public void bindView(BindViewHolder viewHolder) {
                                    TextView m = viewHolder.getView(R.id.money);
                                    TextView m1 = viewHolder.getView(R.id.money1);
                                    m.setText(money);
                                    m1.setText(money);
                                }
                            }).setOnViewClickListener(new OnViewClickListener() {
                                @Override
                                public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
                                    switch (view.getId()) {
                                        case R.id.btn_close_popupwindow:
                                            tDialog.dismiss();
                                            break;
                                        case R.id.btn_confirm_pay:
                                            tDialog.dismiss();
                                            ld = new LoadingDialog(PayActivity.this);
                                            ld.setSuccessText("Payment successful")//显示加载成功时的文字
                                                    .setInterceptBack(false)
                                                    .setLoadSpeed(LoadingDialog.Speed.SPEED_ONE)
                                                    .setRepeatCount(-1)
                                                    .show();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ld.loadSuccess();
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            showActivity(PayActivity.this, MainActivity.class);
                                                            AppManager.getAppManager().finishAllActivity();
                                                        }
                                                    }, 1000);
                                                }
                                            }, 2000);
                                            break;
                                    }
                                }
                            }).create().show();
                        }
                    }, 2000);
                });
                LinearLayout paypal = holder.getView(R.id.paypal);
                paypal.setOnClickListener(view -> {
                    startActivity(new Intent(PayActivity.this, PayPalActivity.class));
                });
            }
        };
        xxDialog.fromBottom().backgroundLight(0.2).fullWidth().showDialog();
    }

    @Override
    protected void onDestroy() {
        if (ld != null) {
            ld.close();
        }
        super.onDestroy();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        bean = (OrderBean) bundle.getParcelable("bean");
        money = bundle.getString("money");
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

    @OnClick({R.id.imgv_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgv_return:
                onBackPressed();
                break;
        }
    }
}