package com.app.shop.mylibrary.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    /**
     obtain current date
     */
    //yyyy-MM-dd
    public static String getTodayData(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);//set date format
        String str = df.format(new Date());
        return str;
    }

}
