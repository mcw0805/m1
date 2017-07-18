package com.example.mcw0805.wheres_my_stuff.Model;

import android.os.Parcel;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Map;

import static com.google.android.gms.tasks.Tasks.whenAll;

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
     * Default no-arg constructor.
     */
    public LostItem() {
        super();
    }

    /**
     * Constructor for instantiating a Needed.
     * By default, it sets item open status to true.
     *
     * @param name        name of the item
     * @param description description of the item
     * @param date        date of the item
     * @param longitude   geographical longitude
     * @param latitude    geographical latitude
     * @param category    category of the item
     * @param uid         uid of the user posted
     * @param reward      reward for finding this lost item
     */
    public LostItem(String name, String description, long date, double longitude,
                    double latitude, ItemCategory category, String uid, int reward) {
        super(name, description, date, longitude, latitude, category, uid);
        this.reward = reward;
        count++;
    }

    /**
     * Constructor for instantiating a Needed.
     * By default, it sets item open status to true.
     *
     * @param name        name of the item
     * @param description description of the item
     * @param date        date of the item
     * @param longitude   geographical longitude
     * @param latitude    geographical latitude
     * @param category    category of the item
     * @param uid         uid of the user posted
     * @param reward      reward for finding this lost item
     * @param isOpen      status for the item
     */
    public LostItem(String name, String description, long date, double longitude,
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
     *
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

        String display = "(" + type.toString().toUpperCase() + ") "
                + getName() + "- Status: ";

        display += super.getStatusString();

        return display;
    }

    @Override
    public Task writeToDatabase(DatabaseReference childRef) {
        Task t = super.writeToDatabase(childRef);
        DatabaseReference rewardChild = childRef.child("reward");
        return whenAll(t, rewardChild.setValue(getReward()));
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
        DataSnapshot date = dataSnap.child("date");

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
        long dateTime = (long) date.getValue();

        return new LostItem(itemName, itemDesc, dateTime, itemLong, itemLat, itemCat,
                itemOwner, itemReward, itemOpenStat);

    }

    /**
     * Creates a string description of the object so that the google maps pin can put all the information down
     *
     * @return String description
     */
    public String description() {
        String end = "Lost Item: " + this.name + " \n Category: "
                + this.category + "\n Description: " + this.description + "\n Status: "
                + this.getStatusString() + "\n Reward: $" + this.reward;
        return end;
    }


}
