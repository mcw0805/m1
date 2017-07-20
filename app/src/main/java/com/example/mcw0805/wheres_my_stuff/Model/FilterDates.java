package com.example.mcw0805.wheres_my_stuff.Model;

/**
 * Created by Ted on 7/20/2017.
 */

public enum FilterDates {
    LAST30("LAST 30 days"),
    LAST15("Last 15 days"),
    LASTWEEK("Last week"),
    LASTDAY("Last 24 hours");

    private final String filterDates;

    FilterDates(String filerDates) {
        this.filterDates = filerDates;
    }
    public String getDate() {
        return filterDates;
    }
    @Override
    public String toString() {
        return filterDates;
    }
}
