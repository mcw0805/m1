package com.example.mcw0805.wheres_my_stuff.Model;

/**
 * Created by Melanie on 6/20/17.
 *
 * This class represents the type of items.
 */

public enum ItemType {
    LOST("Lost"), FOUND ("Found"), NEED ("Need");

    private final String itemType;

    ItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getType() {
        return itemType;
    }

    @Override
    public String toString() {
        return itemType;
    }
}
