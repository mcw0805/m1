package com.example.mcw0805.wheres_my_stuff.Model;
import java.util.ArrayList;
import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.Model.ItemType;
import com.example.mcw0805.wheres_my_stuff.Model.ItemCategory;
/**
 * This class is used to filter items by Type and Catefory
 * Created by Jordan on 6/28/2017.
 */

public class ItemListContoller {

    /**
     * This method flters an ArrayList of Items by category (MISC, KEEPSAKE, HEIRLOOM)
     * @param inArr ArrayList of Items to be filtered
     * @param cat the ItemCategory to filter by
     * @return an ArrayList contaning the items in the given category
     */
    public static ArrayList<Item> filterByCategory(ArrayList<Item> inArr, ItemCategory cat) {
        ArrayList<Item> outArr = new ArrayList<>();
        for (Item i : inArr) {
            if (i.getCategory() == cat) {
                outArr.add(i);
            }
        }
        return outArr;
    }

}
