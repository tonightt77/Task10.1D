
package com.app.shop.mylibrary.utils;

public class StringUtil {
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equalsIgnoreCase(input)) {
            return true;
        }
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static String getContent(String str) {

        if (isEmpty(str)) {
            return "--";
        } else {
            return str;
        }
    }

    public static String onToTwo(int num) {
        if (num < 10) {
            return "0" + num;
        }
        return num + "";
    }


}
