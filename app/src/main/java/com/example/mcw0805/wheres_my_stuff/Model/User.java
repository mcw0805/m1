package com.example.mcw0805.wheres_my_stuff.Model;
import java.util.LinkedList;

/**
 * Creating the user model
 * @Author Ted Shang
 * @Version 1.2
 */

public class User {
    private String _name;
    private String _email;
    private boolean _isLocked = false;
    private boolean _isBanned = false;
    private LinkedList<Item> itemList = new LinkedList<>();

    /**
     *Constructor provided with entered information from user
     * @param name name of user
     * @param email email of user
     */
    public User(String name, String email) {
        _name = name;
        _email = email;

    }
    //add method for new items

    /**
     * Adds an item to the user's list
     * @param item
     */
    public void addItem(Item item) {
        itemList.add(item);
    }

    //Getters and Setters

    /**
     * Gets the name of the user
     * @return name of user
     */
    public String get_name() {
        return _name;
    }

    /**
     * gets the locked status of the user
     * @return boolean representing the status
     */
    public boolean get_isLocked() {return _isLocked;}

    /**
     * Gets the itemlist of the user
     * @return the LinkedList of items
     */
    public LinkedList<Item> getItemList() {return itemList;}

    /**
     * Sets the locked status of the user
     * @param k boolean true or false
     */
    public void set_isLocked(boolean k) {_isLocked = k;}

    /**
     * Set the name of the user
     * @param name of the user
     */
    public void set_name(String name) {
        _name = name;
    }

    /**
     * Sets the banened or not status of the user
     * @param boolean true or false
     */
    public void set_isBanned(boolean bool) { _isBanned = bool;}

}
