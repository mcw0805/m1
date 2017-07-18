package com.example.mcw0805.wheres_my_stuff.Model;

import android.os.Parcel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

/**
 * Class representing the FoundItem.
 */
public class FoundItem extends Item {

    private static final ItemType type = ItemType.FOUND;

    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference foundItemsRef = database.getReference("posts/found-items/");
    private static final DatabaseReference childRef = foundItemsRef.child(foundItemsRef.push().getKey());

    /**
     * Default no-arg constructor.
     */
    public FoundItem() {
        super();
    }

    /**
     * Constructor for instantiating a FoundItem
     *
     * @param name name of the item
     * @param description description of the item
     * @param date date of the item
     * @param longitude geographical longitude
     * @param latitude
     * @param category
     * @param uid
     */
    public FoundItem(String name, String description, long date, double longitude,
                     double latitude, ItemCategory category, String uid) {
        super(name, description, date, longitude, latitude, category, uid);
    }

    public FoundItem(String name, String description, long date, double longitude,
                     double latitude, ItemCategory category, String uid, boolean isOpen) {
        super(name, description, date, longitude, latitude, category, uid, isOpen);
    }

    protected FoundItem(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static final Creator<FoundItem> CREATOR = new Creator<FoundItem>() {
        @Override
        public FoundItem createFromParcel(Parcel in) {
            return new FoundItem(in);
        }

        @Override
        public FoundItem[] newArray(int size) {
            return new FoundItem[size];
        }
    };

    /**
     * Gets the item type of the FoundItem.
     *
     * @return type of the FoundItem
     */
    public ItemType getItemType() {
        return type;
    }

    /**
     * Gets the  database reference for FoundItem in Firebase.
     *
     * @return found-item reference in the database
     */
    public static DatabaseReference getFoundItemsRef() {
        return foundItemsRef;
    }

    /**
     * Gets the child reference under the found-item root.
     *
     * @return child reference under the found-item root
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

    /**
     * Creates a new FoundItem object using the data from Firebase database.
     *
     * @param dataSnap Firebase DataSnapshot which we build the FoundItem object from
     * @return FoundItem built from the the data snapshot
     */
    public static FoundItem buildFoundItemObject(DataSnapshot dataSnap) {
        DataSnapshot name = dataSnap.child("name");
        DataSnapshot description = dataSnap.child("description");
        DataSnapshot latitude = dataSnap.child("latitude");
        DataSnapshot longitude = dataSnap.child("longitude");
        DataSnapshot isOpen = dataSnap.child("isOpen");
        DataSnapshot uid = dataSnap.child("uid");
        DataSnapshot category = dataSnap.child("category");
        DataSnapshot date = dataSnap.child("date");

        String itemName = (String) name.getValue();
        String itemDesc = (String) description.getValue();
        double itemLat = Double.parseDouble(String.valueOf(latitude.getValue()));
        //double itemLat = convertDouble(latitude.getValue());
        double itemLong = Double.parseDouble(String.valueOf(longitude.getValue()));
        boolean itemOpenStat = (Boolean) isOpen.getValue();
        String itemOwner = (String) uid.getValue();
        ItemCategory itemCat = ItemCategory.valueOf((String) category.getValue());
        long dateTime = (long) date.getValue();

        return new FoundItem(itemName, itemDesc, dateTime, itemLong, itemLat, itemCat,
                itemOwner, itemOpenStat);

    }

    /**
     * Creates a string description of the object so that the google maps pin can put
     * all the information down.
     *
     * @return String description
     */
    public String description() {
        String end = "Found Item: " + this.name + " \n Category: "
                + this.category + "\n Description: " + this.description + "\n Status: "
                + this.getStatusString();
        return end;
    }
}
