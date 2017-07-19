package com.example.mcw0805.wheres_my_stuff.Model;

/**
 * Created by jordan on 6/20/17.
 */

public enum ItemCategory {
    KEEPSAKE ("KEEPSAKE"),
    HEIRLOOM ("HEIRLOOM"),
    MISC ("MISC");

    private final String itemCategory;

    ItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getType() {
        return itemCategory;
    }

    @Override
    public String toString() {
        return itemCategory;
    }



}
