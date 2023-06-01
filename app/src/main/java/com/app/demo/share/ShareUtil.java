package com.app.demo.share;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.umeng.socialize.bean.SHARE_MEDIA;

public class ShareUtil {
    private static SHARE_MEDIA[] shareDisplaylist;

    public static void share(final Activity activity,
                              String msg,
                              String title,
                              String url,
                              String thumb,
                              int shareResultType,
                              I_OnShareCallBack i_onShareCallBack) {

        shareDisplaylist = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE};

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //ask permission
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                ActivityCompat.requestPermissions(activity, mPermissionList, 123);
            } else {
                shareReal(activity, msg, title, url, thumb, shareDisplaylist, shareResultType, i_onShareCallBack);
            }
        }
    }

    private static void shareReal(final Activity activity,
                                  final String msg,   // Description
                                  final String title, // Title
                                  String url,   // Url
                                  String thumb,   // Thumb
                                  SHARE_MEDIA[] displaylist,
                                  final int callBackType,
                                  final I_OnShareCallBack i_onShareCallBack
    ) {

        // customize share
        new ShareWindowView.Builder()
                .setShareMsg(msg)
                .setShareTitle(title)
                .setShareUrl(url)
                .setThumb(thumb)
                .setShareIsBlock(callBackType)
                .setShareActivity(activity)
                .setData(displaylist)
                .setShareIonShareCallBack(i_onShareCallBack)
                .build().show(activity.getFragmentManager());
    }


}
