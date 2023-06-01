package com.app.demo;

import android.app.Application;

import com.alipay.sdk.app.EnvUtils;
import com.app.shop.mylibrary.utils.AppDir;
import com.app.shop.mylibrary.utils.FrescoUtil;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.xiasuhuei321.loadingdialog.manager.StyleManager;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import org.litepal.LitePal;


public class MyApplication extends Application {
    public static String TAG = "xxx";

    @Override
    public void onCreate() {
        super.onCreate();

        UMConfigure.init(this, "5e0afc290cafb2c63b000257", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");

        PlatformConfig.setWeixin("wxa7974d581a6cef99", "93e0a10abb1ee53c3c6073063aeeab94");

        UMConfigure.setLogEnabled(true);


        //database initialization
        LitePal.initialize(this);

        //Create application cache path
        AppDir.getInstance(this);

        //fresco initialization
        FrescoUtil.init(this);

        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);

        StyleManager s = new StyleManager();

        s.Anim(false).repeatTime(0).contentSize(-1).intercept(true);

        LoadingDialog.initStyle(s);
    }
}
