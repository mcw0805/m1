package com.example.mcw0805.wheres_my_stuff.Model;

import android.os.Parcel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Map;

/**
 * Represents the model for the lost item.
 *
 * @author Jordan Taylor, Chaewon Min
 */
//6/22/17 Changed cateogry to string for testing purposes

public class LostItem extends Item {
    private static int count;
    private int reward;

    public LostItem(String name, String description, Date date, double longitude,
                    double latitude, ItemCategory category, String uid, int reward) {
        super(name, description, date, longitude, latitude, category, uid);
        this.reward = reward;
        count++;
    }


    public LostItem(String name, String description, Date date, double longitude,
                    double latitude, ItemCategory category, String uid, int reward, boolean isOpen) {
        super(name, description, date, longitude, latitude, category, uid, isOpen);
        this.reward = reward;
    }

    protected LostItem(Parcel in) {
        super(in);
        reward = in.readInt();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(reward);

    }

    public static final Creator<LostItem> CREATOR = new Creator<LostItem>() {
        @Override
        public LostItem createFromParcel(Parcel in) {
            return new LostItem(in);
        }

        @Override
        public LostItem[] newArray(int size) {
            return new LostItem[size];
        }
    };

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return getName() + " " + "UID: " + getUid();
    }

    /**
     * When the user wishes to post an item, this method is called,
     * and the necessary information is pushed into Firebase database.
     *
     */
    public void writeToDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference lostItemsRef = database.getReference("posts/lost-items/");
        String key = lostItemsRef.push().getKey();
        final DatabaseReference childRef = lostItemsRef.child(key);

        DatabaseReference dateChild = childRef.child("date-time");
        dateChild.setValue(getDate().getTime());

        DatabaseReference nameChild = childRef.child("name");
        nameChild.setValue(getName());

        DatabaseReference descriptionChild = childRef.child("description");
        descriptionChild.setValue(getDescription());

        DatabaseReference latitudeChild = childRef.child("latitude");
        latitudeChild.setValue(getLatitude());

        DatabaseReference longitudeChild = childRef.child("longitude");
        longitudeChild.setValue(getLongitude());

        DatabaseReference categoryChild = childRef.child("category");
        categoryChild.setValue(getCategory());

        DatabaseReference uidChild = childRef.child("uid");
        uidChild.setValue(getUid());

        DatabaseReference isOpenChild = childRef.child("isOpen");
        isOpenChild.setValue(getIsOpen());

        DatabaseReference rewardChild = childRef.child("reward");
        rewardChild.setValue(getReward());

    }

    /**
     * Creates a new LostItem object using the data from Firebase database.
     *
     * @param dataSnap Firebase DataSnapshot which we build the LostItem object from
     * @return LostItem built from the the data snapshot
     */
    public static LostItem buildLostItemObject(DataSnapshot dataSnap) {
        DataSnapshot name = dataSnap.child("name");
        DataSnapshot description = dataSnap.child("description");
        DataSnapshot latitude = dataSnap.child("latitude");
        DataSnapshot longitude = dataSnap.child("longitude");
        DataSnapshot isOpen = dataSnap.child("isOpen");
        DataSnapshot reward = dataSnap.child("reward");
        DataSnapshot uid = dataSnap.child("uid");
        DataSnapshot category = dataSnap.child("category");
        DataSnapshot date = dataSnap.child("date-time");

        String itemName = (String) name.getValue();
        String itemDesc = (String) description.getValue();
        //double itemLat = convertDouble(latitude.getValue());
        double itemLat = Double.parseDouble(String.valueOf(latitude.getValue()));
        //double itemLong = convertDouble(longitude.getValue());
        double itemLong = Double.parseDouble(String.valueOf(longitude.getValue()));
        boolean itemOpenStat = (Boolean) isOpen.getValue();
        int itemReward = Integer.parseInt(String.valueOf(reward.getValue()));
        //int itemReward = convertInteger(reward.getValue());
        String itemOwner = (String) uid.getValue();
        ItemCategory itemCat = ItemCategory.valueOf((String) category.getValue());
        Date dateTime = new Date((long) date.getValue());

        return new LostItem(itemName, itemDesc, dateTime, itemLong, itemLat, itemCat,
                itemOwner, itemReward, itemOpenStat);

    }

//
//    public static LostItem buildLostItem(DataSnapshot dataSnap) {
//        Map<String, Object> map = (Map<String, Object>) dataSnap.getValue();
//
//        String itemName = (String) map.get("name");
//        String itemDesc = (String) map.get("description");
//        double itemLat = (Double) Double.parseDouble(map.get("latitude").toString());
//        double itemLong = (Double) Double.parseDouble(map.get("longitude").toString());
//        boolean itemOpenStat = (Boolean) Boolean.parseBoolean(map.get("isOpen").toString());
//        int itemReward = (Integer) Integer.parseInt(map.get("reward").toString());
//        String itemOwner = (String) map.get("uid");
//        ItemCategory itemCat = ItemCategory.valueOf((String) map.get("category").toString());
//        Date dateTime = new Date((Long) Long.parseLong(map.get("date-time").toString()));
//
//        return new LostItem(itemName, itemDesc, dateTime, itemLong, itemLat, itemCat,
//                itemOwner, itemReward, itemOpenStat);
//
//    }

    private static double convertDouble(Object longValue) {
        double result; // return value

        if (longValue instanceof Long) { // Necessary due to the way Firebase stores data
            result = ((Long) longValue).doubleValue();
        } else if (longValue instanceof Double) {
            result = (double) longValue;
        } else {
            throw new IllegalArgumentException(
                    "Object passed in must be either a double or a long");
        }

        return result;
    }

    private static int convertInteger(Object longValue) {
        int result; // return value

        if (longValue instanceof Long) { // Necessary due to the way Firebase stores data
            result = ((Long) longValue).intValue();
        } else if (longValue instanceof Integer) {
            result = (int) longValue;
        } else {
            throw new IllegalArgumentException(
                    "Object passed in must be either a integer or a long");
        }

        return result;
    }

}
