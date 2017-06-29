package com.example.mcw0805.wheres_my_stuff.Model;
import java.util.ArrayList;
import java.util.Collections;
import com.example.mcw0805.wheres_my_stuff.Model.User;
/**
 * Created by Jordan on 6/28/2017.
 */

public class UserListController {
    /**
     * This method filters a List of all users by those that are banned
     * @param inArr an ArrayList of all the current users
     * @return an array list of users that are banned
     */
    public static ArrayList<User> filterByBanned(ArrayList<User> inArr) {
        ArrayList<User> outArr = new ArrayList<>();
        for (User u : inArr) {
            if (u.isBanned()) {
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
    public static ArrayList<User> filterByActive(ArrayList<User> inArr) {
        ArrayList<User> outArr = new ArrayList<>();
        for (User u : inArr) {
            if (!(u.isBanned())) {
                outArr.add(u);
            }
        }
        return outArr;
    }
//    public ArrayList<User> dateHiLow(){
//        ArrayList<User> outArr = unsorted.sort(); // need to write a sorting algo
//        for (User u)
//    }
//    public ArrayList<User> dateLowHi() {
//
//    }
//    private void sort() {
//
//    }
}
