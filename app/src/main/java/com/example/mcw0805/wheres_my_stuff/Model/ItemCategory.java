package com.example.mcw0805.wheres_my_stuff.Model;

/**
 * Enum representing the categories of the item.
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
