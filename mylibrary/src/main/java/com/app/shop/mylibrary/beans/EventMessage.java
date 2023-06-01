package com.app.shop.mylibrary.beans;


public class EventMessage {

    private Object mObject;

    private int messageType;

    private int Num;

    public EventMessage(int type) {
        messageType = type;
    }

    public EventMessage(int type, Object object) {
        mObject = object;
        messageType = type;
    }

    public EventMessage(int type, Object object, int num) {
        mObject = object;
        messageType = type;
        Num = num;
    }

    public Object getmObject() {
        return mObject;
    }

    public int getMessageType() {
        return messageType;
    }

    public int getNum() {
        return Num;
    }


    public static final int LOGIN = 1;// login
    public static final int DEL_words = 41;

    public static final int Click = 5;
    public static final int Click_1 = 51;

    public static final int ADD = 6;// add
    public static final int refresh = 7;


}
