package com.example.mcw0805.wheres_my_stuff.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
/**
 * This class is used to filter items by Type and Category.
 *
 * Created by Jordan on 6/28/2017.
 */

public abstract class ItemListContoller {
    protected static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    protected static final DatabaseReference foundRef = database.getReference("posts/found-items/");
    protected static final DatabaseReference lostRef = database.getReference("posts/lost-items/");
    /**
     * This method flters an ArrayList of Items by category (MISC, KEEPSAKE, HEIRLOOM)
     * @return an ArrayList contaning the items in the given category
     */
    public static ArrayList<Item> getAll() {
        return null;
    }

    public abstract ArrayList<Item> filterByMisc();
    public abstract ArrayList<Item> filterByKeepsake();
    public abstract ArrayList<Item> filterByHeirloom();
    public abstract ArrayList<Item> sortByDateHiLow();
    public abstract ArrayList<Item> sortByDateLowHi();
    public abstract ArrayList<Item> sortByName();
//    public static ArrayList<Item> filterByCategory() {
//        Query q =
//        ArrayList<Item> outArr = new ArrayList<>();
//        for (Item i : inArr) {
//            if (i.getCategory() == cat) {
//                outArr.add(i);
//            }
//        }
//        return outArr;
//    }

}
