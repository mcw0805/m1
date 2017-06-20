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
    public Item(String name, String description, Date date, double longitude, double latitude, ItemCategory category) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.category = category;
    }
}
