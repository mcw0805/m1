package com.example.mcw0805.wheres_my_stuff.Model;

/**
 * Created by Ted on 7/20/2017.
 */

public enum FilterDates {
    LAST30("Last 30 days"),
    LAST15("Last 15 days"),
    LASTWEEK("Last week"),
    LASTDAY("Last 24 hours");

    private final String filterDates;

    /**
     *Constructor for filter dates
     * @param filerDates string of the date
     */
    FilterDates(String filerDates) {
        this.filterDates = filerDates;
    }

    /**
     * returns the date in string form
     * @return string date
     */
    public String getDate() {
        return filterDates;
    }
    @Override
    public String toString() {
        return filterDates;
    }
}
