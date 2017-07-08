package com.example.mcw0805.wheres_my_stuff.Model;

import android.os.Parcel;
import android.util.Log;

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

    /*
        Instance data specific to LostItem
     */
    private static int count;
    private int reward;
    private static final ItemType type = ItemType.LOST;

    //Firebase database references
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference lostItemsRef = database.getReference("posts/lost-items/");
    private static final DatabaseReference childRef = lostItemsRef.child(lostItemsRef.push().getKey());


    /**
     * Creates the LostItem object and increments the count.
     */
    public LostItem(String name, String description, Date date, double longitude,
                    double latitude, ItemCategory category, String uid, int reward) {
        super(name, description, date, longitude, latitude, category, uid);
        this.reward = reward;
        count++;
    }

    /**
     * Creates the LostItem object. Used for building objects from the database
     */
    public LostItem(String name, String description, Date date, double longitude,
                    double latitude, ItemCategory category, String uid, int reward, boolean isOpen) {
        super(name, description, date, longitude, latitude, category, uid, isOpen);
        this.reward = reward;
    }

    /**
     * Creates the LostItem object from a Parcel.
     *
     * @param in parcel to create the LostItem from
     */
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

    /**
     * Gets the reward amount that would be given for finding the lost item.
     *
     * @return reward of the LostItem
     */
    public int getReward() {
        return reward;
    }

    /**
     * Sets the reward amount that would be given for finding the lost item.
     * @param reward amount for the LostItem
     */
    public void setReward(int reward) {
        this.reward = reward;
    }

    /**
     * Gets the total count of LostItem
     *
     * @return count of the LostItem
     */
    public int getCount() {
        return count;
    }

    /**
     * Gets the ItemType of this object, which is permanently set to be LOST.
     *
     * @return type of the item
     */
    public ItemType getItemType() {
        return type;
    }



    /**
     * Gets the database reference to the lost-items root in Firebase.
     *
     * @return lostItemsRef the Firebase reference to lost items
     */
    public static DatabaseReference getLostItemsRef() {
        return lostItemsRef;
    }

    /**
     * Gets the database reference to the child root of the lost-items in Firebase.
     *
     * @return childRef the Firebase reference to child of lost items
     */
    public static DatabaseReference getChildRef() {
        return childRef;
    }


    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void writeToDatabase(DatabaseReference childRef) {
        super.writeToDatabase(childRef);

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
//
//    private static double convertDouble(Object longValue) {
//        double result; // return value
//
//        if (longValue instanceof Long) { // Necessary due to the way Firebase stores data
//            result = ((Long) longValue).doubleValue();
//        } else if (longValue instanceof Double) {
//            result = (double) longValue;
//        } else {
//            throw new IllegalArgumentException(
//                    "Object passed in must be either a double or a long");
//        }
//
//        return result;
//    }
//
//    private static int convertInteger(Object longValue) {
//        int result; // return value
//
//        if (longValue instanceof Long) { // Necessary due to the way Firebase stores data
//            result = ((Long) longValue).intValue();
//        } else if (longValue instanceof Integer) {
//            result = (int) longValue;
//        } else {
//            throw new IllegalArgumentException(
//                    "Object passed in must be either a integer or a long");
//        }
//
//        return result;
//    }

}
