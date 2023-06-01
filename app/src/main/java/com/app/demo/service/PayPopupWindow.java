package com.app.demo.service;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demo.R;
import com.luck.picture.lib.tools.ToastUtils;

public class PayPopupWindow extends PopupWindow implements View.OnClickListener {
    private View conentView;
    private Activity mContext;
    private Button btn_confirm_pay;
    private ImageView btn_close_popupwindow;
    private String moneys;

    public PayPopupWindow(Activity context, String money) {
        mContext = context;
        moneys = money;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.pay_popu_window, null);
        int width = context.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(conentView);
        this.setWidth(width);
        this.setHeight(getHeight());
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        initPopupWindowView();
        initListener();
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else {
            this.dismiss();
        }
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
        if (isShowing()) {
            WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
            lp.alpha = 0.4f;
            mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mContext.getWindow().setAttributes(lp);
        }
    }

    public void initPopupWindowView() {
        btn_confirm_pay = (Button) conentView.findViewById(R.id.btn_confirm_pay);
        btn_close_popupwindow = (ImageView) conentView.findViewById(R.id.btn_close_popupwindow);
    }


    private void initListener() {
        btn_confirm_pay.setOnClickListener(this);
        btn_close_popupwindow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_pay:
                ToastUtils.s(mContext, "Payment successful");
                break;
            case R.id.btn_close_popupwindow:
                this.dismiss();
                break;
        }
    }
}
