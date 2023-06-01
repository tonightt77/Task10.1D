package com.app.shop.mylibrary;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;


public class MyApplication extends Application {

    private List<Activity> activityList = new LinkedList<Activity>();
    private static volatile MyApplication instance;

    private MyApplication() {

    }

    public static MyApplication getInstance() {
        if (null == instance) {
            synchronized (MyApplication.class) {
                if (null == instance) {
                    instance = new MyApplication();
                }
            }
        }
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();



    }

    public void addActivity(Activity aty) {
        activityList.add(aty);
    }


    // exit activity
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }


    public void deleteActivity(Activity aty) {
        activityList.remove(aty);
    }
}
