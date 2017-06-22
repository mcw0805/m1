package com.example.mcw0805.wheres_my_stuff.Model;

import java.util.Date;

/**
 * Created by jordan on 6/20/17.
 */
//6/22/17 Changed cateogry to string for testing purposes
public class DonationItem extends Item {
    private static int count;

    public DonationItem(String name, String description, Date date, double longitude,
                     double latitude, String category, User user, boolean status) {
        super(name, description, date, longitude, latitude, category, user, status);
        count++;
    }

    public int getCount() {
        return count;
    }
}
