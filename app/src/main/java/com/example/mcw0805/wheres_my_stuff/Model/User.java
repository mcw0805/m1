package com.example.mcw0805.wheres_my_stuff.Model;
import java.util.LinkedList;

/**
 * Created by Ted on 6/14/2017.
 * Creating the user model
 */

public class User {
    private String _name;

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public boolean is_isLocked() {
        return _isLocked;
    }

    public boolean is_isBanned() {
        return _isBanned;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    private String _email;
    private boolean _isLocked = false;
    private boolean _isBanned = false;
    private String _id;
    private static int foundCount;
    private static int lostCount;


    /**
     *Constructor provided with entered information from user
     * @param name name of user
     * @param email email of user
     */
    public User(String name, String email, String id) {
        _name = name;
        _email = email;
        _id = id;

    }
    //add method for new items

    //Getters and Setters
    public String get_name() {
        return _name;
    }
    public boolean get_isLocked() {return _isLocked;}
    public void set_isLocked(boolean k) {_isLocked = k;}
    public void set_name(String name) {
        _name = name;
    }
    public void set_isBanned(boolean bool) { _isBanned = bool;}

}
