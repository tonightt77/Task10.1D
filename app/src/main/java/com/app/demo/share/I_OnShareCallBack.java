package com.app.demo.share;


public abstract class I_OnShareCallBack {

    // show the result of share
    public abstract void onShareSuccess(String shareChannel);

    public abstract void onShareCancel(String shareChannel);

    public abstract void onShareError(String shareChannel);
}
