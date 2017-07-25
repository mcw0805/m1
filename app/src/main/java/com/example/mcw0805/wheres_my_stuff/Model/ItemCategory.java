package com.example.mcw0805.wheres_my_stuff.Model;

/**
 * Enum representing the categories of the item.
 */
public enum ItemCategory {
    NOTHING_SELECTED(""),
    KEEPSAKE("KEEPSAKE"),
    HEIRLOOM("HEIRLOOM"),
    MISC("MISC");

    private final String itemCategory;

    /**
     * constructor for enum
     * @param itemCategory string of item category
     */
    ItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    /**
     * returns the type of item
     * @return string
     */
    public String getType() {
        return itemCategory;
    }

    @Override
    public String toString() {
        return itemCategory;
    }

}
