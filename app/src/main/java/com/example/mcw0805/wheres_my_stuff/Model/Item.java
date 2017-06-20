package com.example.mcw0805.wheres_my_stuff.Model;
import java.util.Date;
/**
 * Created by jordan on 6/20/17.
 */

public abstract class Item {
    protected String name, description;
    protected Date date;
    protected double longitude, latitude;
    protected ItemCategory category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
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

    public Item(String name, String description, Date date, double longitude, double latitude, ItemCategory category) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.category = category;
    }
}
