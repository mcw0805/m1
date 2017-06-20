package com.example.mcw0805.wheres_my_stuff.Model;

import java.util.Date;

/**
 * Created by jordan on 6/20/17.
 */

public class LostItem extends Item {
    private static count;
    private int reward;
    public LostItem(String name, String description, Date date, double longitude, double latitude, ItemCategory category, int reward) {
        super(name, description, date, longitude, latitude, category);
        this.reward = reward;
        count++;
    }
}
