package com.example.mcw0805.wheres_my_stuff.Model;
import java.util.ArrayList;
import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.Model.ItemType;
import com.example.mcw0805.wheres_my_stuff.Model.ItemCategory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
