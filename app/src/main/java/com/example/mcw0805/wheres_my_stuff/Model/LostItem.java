package com.example.mcw0805.wheres_my_stuff.Model;

import java.util.Date;

/**
 * Created by jordan on 6/20/17.
 */
//6/22/17 Changed cateogry to string for testing purposes

public class LostItem extends Item {
    private static int count;
    private int reward;
    public LostItem(String name, String description, Date date, double longitude,
                    double latitude, String category, User user, boolean status, int reward) {
        super(name, description, date, longitude, latitude, category, user, status);
        this.reward = reward;
        count++;
    }
    public int getReward() {
        return reward;
    }
    public void setReward(int reward) {
        this.reward = reward;
    }
    public int getCount() {
        return count;
    }
}
