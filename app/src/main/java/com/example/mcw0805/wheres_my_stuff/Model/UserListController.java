package com.example.mcw0805.wheres_my_stuff.Model;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jordan
 */

public class UserListController {
    /**
     * This method filters a List of all users by those that are banned
     * @param inArr an ArrayList of all the current users
     * @return an array list of users that are banned
     */
    public static List<User> filterByBanned(List<User> inArr) {
        List<User> outArr = new ArrayList<>();
        for (User u : inArr) {
            if (u.getIsBanned()) {
                outArr.add(u);
            }
        }
        return outArr;
    }

    /**
     * This method returns an array list of users that are not banned
     * @param inArr an ArrayList of all the current users
     * @return an array list of users that are not banned
     */
    public static List<User> filterByActive(List<User> inArr) {
        List<User> outArr = new ArrayList<>();
        for (User u : inArr) {
            if (!(u.getIsBanned())) {
                outArr.add(u);
            }
        }
        return outArr;
    }
}
