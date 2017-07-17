package com.example.mcw0805.wheres_my_stuff.Model;

import android.os.Parcel;
import android.provider.ContactsContract;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import static com.google.android.gms.tasks.Tasks.whenAll;

/**
 * Created by jordan on 6/20/17.
 * @authors Jordan, Ted
 */
//6/22/17 Changed cateogry to string for testing purposes
public class NeededItem extends Item {
    private static int count;
    private static final ItemType type = ItemType.NEED;

    //Firebase db Refs
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference neededItemsRef = database.getReference("posts/needed-items/");
    private static final DatabaseReference childRef = neededItemsRef.child(neededItemsRef.push().getKey());


    /**
     * Default no-arg constructor.
     */
    public NeededItem() {super();}

    public NeededItem(String name, String description, long date, double longitude,
                      double latitude, ItemCategory category, String uid) {
        super(name, description, date, longitude, latitude, category, uid);
        count++;
    }
    public NeededItem(String name, String description, long date, double longitude,
                      double latitude, ItemCategory category, String uid, boolean isOpen) {
        super(name, description, date, longitude, latitude, category, uid, isOpen);
        count++;
    }
    protected NeededItem(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static final Creator<NeededItem> CREATOR = new Creator<NeededItem>() {
        @Override
        public NeededItem createFromParcel(Parcel in) {
            return new NeededItem(in);
        }

        @Override
        public NeededItem[] newArray(int size) {
            return new NeededItem[size];
        }
    };


    public int getCount() {
        return count;
    }
    public ItemType getItemType() { return type;}
    public static DatabaseReference getNeededItemsRef() {return neededItemsRef;}
    public static DatabaseReference getChildRef() {return childRef;}

    @Override
    public String toString() {

        String display =  "(" + type.toString().toUpperCase() + ") "
                + getName() + "- Status: ";

        display += super.getStatusString();

        return display;
    }

    public static NeededItem buildNeededItemObject(DataSnapshot dataSnap) {
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
        double itemLong = Double.parseDouble(String.valueOf(longitude.getValue()));
        boolean itemOpenStat = (Boolean) isOpen.getValue();
        String itemOwner = (String) uid.getValue();
        ItemCategory itemCat = ItemCategory.valueOf((String) category.getValue());
        long dateTime = (long) date.getValue();

        return new NeededItem(itemName, itemDesc, dateTime, itemLong, itemLat, itemCat,
                itemOwner, itemOpenStat);
    }
    /**
     * Creates a string description of the object so that the google maps pin can put
     * all the information down.
     *
     * @return String description
     */
    public String description() {
        String end = "Needed Item: " + this.name + " \n Category: "
                + this.category + "\n Description: " + this.description + "\n Status: "
                + this.getStatusString();
        return end;
    }

}
