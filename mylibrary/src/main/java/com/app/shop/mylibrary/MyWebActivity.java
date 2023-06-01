package com.app.shop.mylibrary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.app.shop.mylibrary.base.BaseActivity;
import com.app.shop.mylibrary.utils.ToastUtil;
import com.orhanobut.logger.Logger;


public class MyWebActivity extends BaseActivity {


    ImageView imgvReturn;
    TextView tvTitle;
    TextView tv_right;
    ProgressBar pb_loading;
    WebView mWebView;

    String url;
    String title;
    boolean isShare;

    public static void start(Context context, String url, String title, boolean isNeedShare) {
        Intent starter = new Intent(context, MyWebActivity.class);
        starter.putExtra("url", url);
        starter.putExtra("title", title);
        starter.putExtra("isShare", isNeedShare);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_web);

        imgvReturn = (ImageView) findViewById(R.id.imgv_return);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tv_right = (TextView) findViewById(R.id.tv_right);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        mWebView = (WebView) findViewById(R.id.webView);
        initData();
        initListener();


    }

    private void initListener() {
        imgvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(MyWebActivity.this, "Share");
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        isShare = intent.getBooleanExtra("isShare", false);
        tvTitle.setText(title);
        if (isShare) {
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText("Share");
        } else {
            tv_right.setVisibility(View.GONE);
        }
        initWebview();
    }

    private void initWebview() {

        WebSettings settings = mWebView.getSettings();
        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (null != mWebView) {
                    mWebView.loadUrl("javascript:isApp(2)");
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            //方法中的onProgressChanged就代表了其速度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                pb_loading.setVisibility(View.VISIBLE);
                pb_loading.setProgress(newProgress);
                if (newProgress == 100) {
                    pb_loading.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        Logger.e("--------------url---" + url);
        mWebView.loadUrl(url);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
