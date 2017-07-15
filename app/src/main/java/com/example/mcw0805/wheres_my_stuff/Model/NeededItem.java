package com.example.mcw0805.wheres_my_stuff.Model;

import java.util.Date;

/**
 * Created by jordan on 6/20/17.
 */
//6/22/17 Changed cateogry to string for testing purposes
public class NeededItem extends Item {
    private static int count;

    public NeededItem(String name, String description, long date, double longitude,
                      double latitude, ItemCategory category, String uid) {
        super(name, description, date, longitude, latitude, category, uid);
        count++;
    }

    public int getCount() {
        return count;
    }
}
