package com.app.shop.mylibrary.widgts;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.shop.mylibrary.R;
import com.app.shop.mylibrary.utils.StringUtil;



public class LoadingDialog {

    static TextView tipTextView;

    public static Dialog createLoadingDialog(Context context, boolean isCancelable, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.dialog_loading_view);
        tipTextView = (TextView) v.findViewById(R.id.tipTextView);
        tipTextView.setText(msg);

        Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);
        loadingDialog.setCancelable(isCancelable);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);

        return loadingDialog;
    }


    public static void showDialog(Dialog mDialogUtils) {
        if (mDialogUtils != null && !mDialogUtils.isShowing()) {
            mDialogUtils.show();
        }
    }


    public static void showDialog(Dialog mDialogUtils, String msg) {
        if (mDialogUtils != null && !mDialogUtils.isShowing()) {
            mDialogUtils.show();
        }

        if (tipTextView != null) {
            tipTextView.setText(StringUtil.getContent(msg));
        }
    }


    public static void closeDialog(Dialog mDialogUtils) {
        if (mDialogUtils != null && mDialogUtils.isShowing()) {
            mDialogUtils.dismiss();
        }
    }
}
