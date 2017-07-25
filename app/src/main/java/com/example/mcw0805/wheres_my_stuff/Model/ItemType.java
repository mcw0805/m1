package com.example.mcw0805.wheres_my_stuff.Model;

/**
 *
 * This class represents the type of items.
 */
public enum ItemType {
    LOST("Lost"), FOUND("Found"), NEED("Need"), DONATION("Donation");

    private final String itemType;

    /**
     * constructor for itemtype
     * @param itemType itemtype of item
     */
    ItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * gets string of itemtype
     * @return string
     */
    public String getType() {
        return itemType;
    }

    @Override
    public String toString() {
        return itemType;
    }
}
