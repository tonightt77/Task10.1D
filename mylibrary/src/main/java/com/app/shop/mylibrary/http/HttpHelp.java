package com.app.shop.mylibrary.http;

import android.content.Context;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.util.Map;

public class HttpHelp {


    I_success i_success;
    I_failure i_failure;
    Context context;
    String url;


    public HttpHelp(I_success i_success, I_failure i_failure, Context context, String url) {
        this.i_failure = i_failure;
        this.i_success = i_success;
        this.context = context;
        this.url = url;
    }


    public void getHttp2( ) {

        OkGo.<String>get(url).tag(context).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                

                Logger.v("-----------json-------" + response.body());
                String s = response.body();
                try {
                    i_success.doSuccess(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    public void getHttp(final Map map) {
        initHttpSecret(map);
    }


    private void initHttpSecret(final Map map) {
        HttpPublishConfig.setPublicParam(map);

        String paramJson = new Gson().toJson(map);

        OkGo.<String>post(url).tag(context).upJson(paramJson).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
              
                Logger.v("-----------json-------" + response.body());
                String s = response.body();
                try {
                    i_success.doSuccess(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
