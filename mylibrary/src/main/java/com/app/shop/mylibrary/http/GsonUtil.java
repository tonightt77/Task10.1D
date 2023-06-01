package com.app.shop.mylibrary.http;

import android.app.Activity;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class GsonUtil {
    private static Gson gson;

    // is legal json
    public static boolean isRightJson(Activity activity,String t){
        boolean isRight =false;
        try {
            JSONObject jo = new JSONObject(t);
            if (jo.getString("state").equals("true"))
                isRight =true;
            else
                isRight =false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isRight;
    }

    public static Gson getInstance(){
        if(null==gson){
            return new Gson();
        }
        return gson;
    }
}
