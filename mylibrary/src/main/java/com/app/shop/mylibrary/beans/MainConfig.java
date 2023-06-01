package com.app.shop.mylibrary.beans;

public class MainConfig {

    private String message;
    private String result;
    private DataBean data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }




    public static class DataBean {

        public String queryCustomTemplet;           

    }
}
