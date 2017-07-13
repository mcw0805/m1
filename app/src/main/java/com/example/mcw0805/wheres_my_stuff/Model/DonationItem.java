package com.example.mcw0805.wheres_my_stuff.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by jordan on 6/20/17.
 */
//6/22/17 Changed cateogry to string for testing purposes
public class DonationItem extends Item2 {
    private static int count;

    private static final ItemType type = ItemType.NEED;

    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference donationRef = database.getReference("posts/donation-items/");
    private static final DatabaseReference childRef = donationRef.child(donationRef.push().getKey());


    public DonationItem() {
        super();
    }


    public DonationItem(String name, String description, long date, double longitude,
                     double latitude, ItemCategory category, String uid) {
        super(name, description, date, longitude, latitude, category, uid);
        count++;
    }

    public int getCount() {
        return count;
    }

    /**
     * Gets the database reference to the lost-items root in Firebase.
     *
     * @return lostItemsRef the Firebase reference to lost items
     */
    public static DatabaseReference getDonationRef() {
        return donationRef;
    }

    /**
     * Gets the database reference to the child root of the lost-items in Firebase.
     *
     * @return childRef the Firebase reference to child of lost items
     */
    public static DatabaseReference getChildRef() {
        return childRef;
    }

    protected DonationItem(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static final Creator<DonationItem> CREATOR = new Creator<DonationItem>() {
        @Override
        public DonationItem createFromParcel(Parcel in) {
            return new DonationItem(in);
        }

        @Override
        public DonationItem[] newArray(int size) {
            return new DonationItem[size];
        }
    };

    public ItemType getItemType() {
        return type;
    }

    @Override
    public String toString() {

        String display =  "(NEED) " + getName() + "-- Status: ";

        display += isOpen ? "OPEN" : "CLOSED" ;

        return display;
    }

    /**
     * Creates a string description of the object so that the google maps pin can put all the information down
     * @return String description
     */
    public String description() {
        String end = "Needed Item: " + this.name + " \n Category: "
                + this.category + "\n Description: " + this.description  + "\n Status: "
                + this.getStatusString();
        return end;
    }


    public static void getObjectListFromDB(DataSnapshot dataSnapshot, List<Item2> item2List) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Item2 item = new DonationItem();
            item.setName(ds.getValue(Item2.class).getName());
            item.setDescription(ds.getValue(Item2.class).getDescription());
            item.setIsOpen(ds.getValue(Item2.class).getIsOpen());
            item.setCategory(ds.getValue(Item2.class).getCategory());
            item.setLatitude(ds.getValue(Item2.class).getLatitude());
            item.setLongitude(ds.getValue(Item2.class).getLongitude());
            item.setDate(ds.getValue(Item2.class).getDate());
            item.setUid(ds.getValue(Item2.class).getUid());

            item2List.add(item);
        }


    }





}
