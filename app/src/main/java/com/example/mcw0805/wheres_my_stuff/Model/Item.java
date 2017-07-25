package com.example.mcw0805.wheres_my_stuff.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.tasks.Tasks.whenAll;

/**
 * A class representing the Item object for this application.
 */
public class Item implements Parcelable {
    protected String name;
    protected String description;
    protected long date;
    protected double longitude;
    protected double latitude;
    protected ItemCategory category;
    protected String uid;
    protected boolean isOpen;
    protected static final ItemType type = ItemType.DONATION;

    /**
     * Default no-arg constructor.
     */
    public Item() {
    }

    /**
     * Constructor for Item
     * @param name name of item
     * @param description description of item
     * @param date posted of item
     * @param longitude location
     * @param latitude location
     * @param category category of item
     * @param uid uid of user
     * @param isOpen status of item
     */
    public Item(String name, String description, long date, double longitude,
                double latitude, ItemCategory category, String uid, boolean isOpen) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.category = category;
        this.uid = uid;
        this.isOpen = isOpen;
    }

    /**
     * Constructor for Item
     * @param name name of item
     * @param description description of item
     * @param date posted of item
     * @param longitude location
     * @param latitude location
     * @param category category of item
     * @param uid uid of user
     */
    public Item(String name, String description, long date, double longitude,
                double latitude, ItemCategory category, String uid) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.uid = uid;
        this.isOpen = true;
    }

    /**
     * Creating an Item object from the Parcel
     *
     * @param in Parcel object
     */
    protected Item(Parcel in) {
        name = in.readString();
        description = in.readString();
        date = in.readLong();
        latitude = in.readDouble();
        longitude = in.readDouble();
        category = ItemCategory.valueOf(in.readString().toString());
        uid = in.readString();
        isOpen = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(date);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(category.getType());
        dest.writeString(uid);
        dest.writeByte((byte) (isOpen ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    /**
     * Gets the UID for the owner of this Item.
     *
     * @return uid of the user
     */
    public String getUid() {
        return uid;
    }

    /**
     * Sets the UID for the owner of this item.
     *
     * @param uid uid of the user
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Gets the item status of the Item.
     * Either it is available to view or case closed.
     *
     * @return status of the item
     */
    public boolean getIsOpen() {
        return isOpen;
    }

    /**
     * Sets the status of the Item.
     *
     * @param open new status of the item, where
     *             true = open and false = resolved
     */
    public void setIsOpen(boolean open) {
        this.isOpen = open;
    }

    /**
     * Gets the name of the Item.
     *
     * @return name of the Item
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the item.
     *
     * @param name new name of the item
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the item.
     *
     * @return description string of the item
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the item.
     *
     * @param description new description string of the item
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the date-time of the Item.
     *
     * @return date-time of the posted date of the Item
     */
    public long getDate() {
        return date;
    }

    /**
     * Sets the date-time of the Item.
     *
     * @param date new date-time of the Item
     */
    public void setDate(long date) {
        this.date = date;
    }

    /**
     * Gets the geographical longitude of the Item.
     *
     * @return longitude of hte Item.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the new geographical longitude of the Item.
     *
     * @param longitude new latitude of the Item
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the geographical latitude of the Item.
     *
     * @return latitude of hte Item.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the new geographical latitude of the Item.
     *
     * @param latitude new latitude of the Item
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the category for the Item.
     *
     * @return the category of the Item
     */
    public ItemCategory getCategory() {
        return category;
    }

    /**
     * Sets the category of the Item.
     *
     * @param category new category of the Item
     */
    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    /**
     * Gets the default type for the Item object.
     *
     * @return type of the Item
     */
    public ItemType getType() {
        return type;
    }

    /**
     * Gets the status of the Item and puts it in literal string.
     *
     * @return status of the item, either open or resolved.
     */
    public String getStatusString() {
        return isOpen ? "OPEN" : "RESOLVED";
    }

    /**
     * writes the item to the database
     * @param childRef reference of where item is
     * @return completed task
     */
    public Task writeToDatabase(DatabaseReference childRef) {
        // add an array of tasks
        DatabaseReference dateChild = childRef.child("date");
        DatabaseReference nameChild = childRef.child("name");
        DatabaseReference descriptionChild = childRef.child("description");
        DatabaseReference latitudeChild = childRef.child("latitude");
        DatabaseReference longitudeChild = childRef.child("longitude");
        DatabaseReference categoryChild = childRef.child("category");
        DatabaseReference uidChild = childRef.child("uid");
        DatabaseReference isOpenChild = childRef.child("isOpen");


        // task thing
        return whenAll(dateChild.setValue(date),
                nameChild.setValue(getName()),
                isOpenChild.setValue(getIsOpen()),
                uidChild.setValue(getUid()),
                categoryChild.setValue(getCategory()),
                descriptionChild.setValue(getDescription()),
                latitudeChild.setValue(getLatitude()),
                longitudeChild.setValue(getLongitude())
        );
    }

    /**
     * Fills in a list of items for all users.
     * <p>
     * This method is called for different types of
     * items stored separately in the database.
     *
     * @param dataSnapshot datasnapshot of the item reference
     * @param itemList     list of items to be populated
     * @param type         type of the item
     * @param itemUserMap  map mapping the position of the item
     *                     in the list to the push key in the database
     */
    public static void getObjectListFromDB(DataSnapshot dataSnapshot, List<Item> itemList,
                                           ItemType type, Map<Integer, String> itemUserMap) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Log.i("KEY", ds.getKey());

            String pushKey = ds.getKey().toString();
            String[] parts = pushKey.split("--");
            String itemUser = parts[0];

            Item item = ItemFactory.makeItem(type);
            item.setName(ds.getValue(Item.class).getName());
            item.setDescription(ds.getValue(Item.class).getDescription());
            item.setIsOpen(ds.getValue(Item.class).getIsOpen());
            item.setCategory(ds.getValue(Item.class).getCategory());
            item.setLatitude(ds.getValue(Item.class).getLatitude());
            item.setLongitude(ds.getValue(Item.class).getLongitude());
            item.setDate(ds.getValue(Item.class).getDate());
            item.setUid(ds.getValue(Item.class).getUid());

            if (item instanceof LostItem) {
                ((LostItem) item).setReward(ds.getValue(LostItem.class).getReward());
            }

            itemUserMap.put(itemList.size(), itemUser);
            itemList.add(item);

        }

    }

    /**
     * Fills in a list of items for a particular user.
     *
     * @param dataSnapshot datasnapshot of the item reference
     * @param itemList     list of items to be populated
     * @param type         type of the item
     * @param uid          uid of the item owner
     * @param itemUserMap  map mapping the position of the item
     *                     in the list to the push key in the database
     */
    public static void getUserObjectList(DataSnapshot dataSnapshot, List<Item> itemList,
                                         ItemType type, String uid, Map<Integer, String> itemUserMap) {

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Item item = ItemFactory.makeItem(type);

            String pushKey = ds.getKey().toString();
            String[] parts = pushKey.split("--");
            String itemUser = parts[0];

            if (itemUser.equals(uid)) {
                item.setName(ds.getValue(Item.class).getName());
                item.setDescription(ds.getValue(Item.class).getDescription());
                item.setIsOpen(ds.getValue(Item.class).getIsOpen());
                item.setCategory(ds.getValue(Item.class).getCategory());
                item.setLatitude(ds.getValue(Item.class).getLatitude());
                item.setLongitude(ds.getValue(Item.class).getLongitude());
                item.setDate(ds.getValue(Item.class).getDate());
                item.setUid(ds.getValue(Item.class).getUid());

                if (item instanceof LostItem) {
                    ((LostItem) item).setReward(ds.getValue(LostItem.class).getReward());
                }
                itemUserMap.put(itemList.size(), pushKey);
                itemList.add(item);
            }
        }

    }
    @Override
    public String toString() {

        String display = "(" + ItemType.DONATION.toString().toUpperCase() + ") "
                + getName() + "- Status: ";

        display += getStatusString();

        return display;
    }

    /**
     * Filters the list of items based on the chosen category.
     *@param itemObjectList list of objects
     * @param cat category of the item
     * @return filtered list containing the specified category
     */
    public static List<Item> filterByCategory(List<Item> itemObjectList, ItemCategory cat) {

        //precondition: all of the param cannot be null
        assert (itemObjectList != null);
        assert (cat != null);

        //invariant: if cat is ItemCategory.NOTHING_SELECTED, it will
        //always return the original result
        if (cat == ItemCategory.NOTHING_SELECTED) {
            return itemObjectList;
        }

        //frame condition: if matching category is found,
        // then the size of filteredItemList will be increased by 1.
        List<Item> filteredItemList = new ArrayList<>();
        for (Item li : itemObjectList) {
            if (li.getCategory() == cat) {
                filteredItemList.add(li);
            }
        }

        //postcondition: filteredItemList.size() >= 0
        //filteredItemList.size() = 0 if itemObjectList (original) did not contain the
        //specified category
        //otherwise, it means there has been a match, so filteredItemList.size() > 0
        return filteredItemList;

    }

    /**
     * Filters the list of items by date
     * @param itemObjectList original item list
     * @param range range of time to sort
     * @return new filtered list
     */
    public static List<Item> filterbyDate(List<Item> itemObjectList, long range) {
        List<Item> filteredItemList = new ArrayList<>();
        for (Item li: itemObjectList) {
            if (li.getDate() - range > 0) {
                filteredItemList.add(li);
            }
        }
        return filteredItemList;
    }

    /**
     * Filters the list of items based on the chosen type.
     *@param itemObjectList list of objects
     * @param type type of the item: Lost, Found, Needed, Donation
     * @return list containing the specified type
     */
    public static List<Item> filterByType(List<Item> itemObjectList, ItemType type) {

        List<Item> filteredItemList = new ArrayList<>();
        for (Item fi : itemObjectList) {
            if (fi.getType() == type) {
                filteredItemList.add(fi);
            }
        }

        return filteredItemList;

    }

    /**
     * Filters the list of items based on the status (open/resolved).
     *@param itemObjectList list of objects
     * @param stat stat status of the item, OPEN or RESOLVED
     * @return list containing the specified type
     */
    public static List<Item> filterByStatus(List<Item> itemObjectList, String stat) {
        List<Item> filteredItemList = new ArrayList<>();

        if (stat.isEmpty()) {
            return itemObjectList;
        }

        for (Item fi : itemObjectList) {
            if (fi.getStatusString().equalsIgnoreCase(stat)) {
                filteredItemList.add(fi);
            }
        }

        return filteredItemList;
    }


    /**
     * Inner factory class that generates different types of Item objects.
     */
    public static class ItemFactory {
        /**
         * Creates the item
         * @param type type of item
         * @return built item
         */
        public static Item makeItem(ItemType type) {
            switch (type) {
                case LOST:
                    return new LostItem();
                case FOUND:
                    return new FoundItem();
                case NEED:
                    return new NeededItem();
                case DONATION:
                    return new Item();
                default:
                    throw new IllegalArgumentException("Error in type");
            }
        }
    }


}
