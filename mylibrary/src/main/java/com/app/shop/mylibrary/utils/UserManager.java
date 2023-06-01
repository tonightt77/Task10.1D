package com.app.shop.mylibrary.utils;

import android.content.Context;

import com.app.shop.mylibrary.beans.UserBean;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UserManager {

    public static String getUserName(Context context) {
        return SharedPreferencesUtil.getData(context, "user", "name", "");

    }

    public static String getUserId(Context context) {
        String id = SharedPreferencesUtil.getData(context, "user", "user_id", "");
        Logger.e("-------id" + id);
        return id;

    }


    public static boolean isExistedUser(String id) {

        List<UserBean> list = DataSupport.findAll(UserBean.class);
        List<String> list_id = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                list_id.add(list.get(i).getUser_id());
            }
        }
        return list_id.contains(id);
    }

    public static boolean isExistedUser(String id, int type) {

        List<UserBean> list_all = DataSupport.findAll(UserBean.class);
        List<UserBean> list = new ArrayList<>();
        for (int i = 0; i < list_all.size(); i++) {
            if (list_all.get(i).getType() == type) {
                list.add(list_all.get(i));
            }
        }
        List<String> list_id = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                list_id.add(list.get(i).getUser_id());
            }
        }
        return list_id.contains(id);
    }

    public static UserBean getUser(String id) {
        List<UserBean> list = DataSupport.findAll(UserBean.class);

        UserBean bean = new UserBean();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUser_id().equals(id)) {
                bean = list.get(i);
            }
        }
        return bean;
    }

    public static boolean isOk(String name, String pwd) {
        List<UserBean> list = DataSupport.findAll(UserBean.class);
        String password = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUser_id().equals(name)) ;
            password = list.get(i).getPassword();
        }
        return password.equals(pwd);
    }

    public void getUser() {
        List<UserBean> list = DataSupport.findAll(UserBean.class);
    }
}
