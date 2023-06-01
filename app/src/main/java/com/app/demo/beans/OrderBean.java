package com.app.demo.beans;


import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

public class OrderBean extends DataSupport implements Parcelable {

    private int id;

    private String o_id; // order id
    private String user_id; // user id
    private String user_name;
    private String title;
    private String content;
    private String img;
    private String user_to;
    private String location;
    private String time;
    private String goodsType;
    private String vehicleType;
    private String weight;
    private String wight;
    private String length;
    private String height;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getO_id() {
        return o_id;
    }

    public void setO_id(String o_id) {
        this.o_id = o_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUser_to() {
        return user_to;
    }

    public void setUser_to(String user_to) {
        this.user_to = user_to;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWight() {
        return wight;
    }

    public void setWight(String wight) {
        this.wight = wight;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public OrderBean(int id, String o_id, String user_id, String user_name, String title, String content,
                     String img, String user_to, String location, String time, String goodsType, String vehicleType,
                     String weight, String wight, String length, String height) {
        this.id = id;
        this.o_id = o_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.title = title;
        this.content = content;
        this.img = img;
        this.user_to = user_to;
        this.location = location;
        this.time = time;
        this.goodsType = goodsType;
        this.vehicleType = vehicleType;
        this.weight = weight;
        this.wight = wight;
        this.length = length;
        this.height = height;
    }

    // Construct objects by reading data from Parcel
    protected OrderBean(Parcel in) {
        id = in.readInt();
        o_id = in.readString();
        user_id = in.readString();
        user_name = in.readString();
        title = in.readString();
        content = in.readString();
        img = in.readString();
        user_to = in.readString();
        location = in.readString();
        time = in.readString();
        goodsType = in.readString();
        vehicleType = in.readString();
        weight = in.readString();
        wight = in.readString();
        length = in.readString();
        height = in.readString();
    }

    // Implement the writeToParcel method of the Parcelable interface to write the state of the object into Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(o_id);
        dest.writeString(user_id);
        dest.writeString(user_name);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(img);
        dest.writeString(user_to);
        dest.writeString(location);
        dest.writeString(time);
        dest.writeString(goodsType);
        dest.writeString(vehicleType);
        dest.writeString(weight);
        dest.writeString(wight);
        dest.writeString(length);
        dest.writeString(height);
    }

    // Must implement the Parcelable.Creator interface and create a constant called CREATOOR to represent the Parcelable.Creator
    public static final Creator<OrderBean> CREATOR = new Creator<OrderBean>() {

        // Create objects by reading Parcel
        @Override
        public OrderBean createFromParcel(Parcel in) {
            return new OrderBean(in);
        }

        // Create an array of specified length
        @Override
        public OrderBean[] newArray(int size) {
            return new OrderBean[size];
        }
    };

    // Returns the content descriptor for the current object instance
    @Override
    public int describeContents() {
        return 0;
    }
}
