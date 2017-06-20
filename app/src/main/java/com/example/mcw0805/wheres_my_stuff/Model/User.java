package com.example.mcw0805.wheres_my_stuff.Model;
import java.util.LinkedList;

/**
 * Created by Ted on 6/14/2017.
 * Creating the user model
 */

public class User {
    private String _name;
    private String _email;
    private String _password;
    private boolean _isLocked = false;
    private LinkedList<Item> itemList = new LinkedList<>();

    /**
     *Constructor provided with entered information from user
     * @param name name of user
     * @param password password of user
     * @param email email of user
     */
    public User(String name, String password, String email) {
        _name = name;
        _password = password;
        _email = email;

    }
    //add method for new items

    public void addItem(Item item) {
        itemList.add(item);
    }

    //Getters and Setters
    public String get_name() {
        return _name;
    }
    public String get_password() {
        return _password;
    }
    public boolean get_isLocked() {return _isLocked;}
    public LinkedList<Item> getItemList() {return itemList;}
    public void set_isLocked(boolean k) {_isLocked = k;}
    public void set_name(String name) {
        _name = name;
    }
    public void set_password(String password) {
        _password = password;
    }

}
