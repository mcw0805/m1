package com.example.mcw0805.wheres_my_stuff.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by jordan on 6/20/17.
 */
//6/22/17 Changed cateogry to string for testing purposes
//6/25/17 category is back to enum, temporarily made it Item not abstract

public class Item implements Parcelable {
    protected String name;
    protected String description;
    protected Date date;
    protected double longitude;
    protected double latitude;
    protected ItemCategory category;
    protected String uid;
    protected boolean isOpen;

    public Item(String name, String description, Date date, double longitude,
                double latitude, ItemCategory category, String uid, boolean isOpen) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.category = category;
        this.uid = uid;
        this.isOpen = isOpen;
    }

    public Item(String name, String description, Date date, double longitude,
                double latitude, ItemCategory category, String uid) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.category = category;
        this.uid = uid;
        this.isOpen = true;
    }

    protected Item(Parcel in) {
        name = in.readString();
        description = in.readString();
        date = new Date(in.readLong());
        latitude = in.readDouble();
        longitude = in.readDouble();
        category = ItemCategory.valueOf(in.readString().toString());
        uid = in.readString();
        isOpen = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(date.getTime());
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(category.getType());
        dest.writeString(uid);
        dest.writeByte((byte) (isOpen ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean getIsOpen()
    {
        return isOpen;
    }

    public void setIsOpen(boolean open) {
        this.isOpen = open;
    }

    public String getName() {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }


}
