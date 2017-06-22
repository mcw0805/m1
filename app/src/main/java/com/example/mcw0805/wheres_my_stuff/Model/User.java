package com.example.mcw0805.wheres_my_stuff.Model;

import java.util.LinkedList;

/**
 * Created by Ted on 6/14/2017.
 * Creating the user model
 */

public class User {
    private String _name;
    private String _email;
    private boolean _isLocked;
    private boolean _isBanned;
    private String _id;


    /**
     * Constructor provided with entered information from user
     *
     * @param name  name of user
     * @param email email of user
     */
    public User(String name, String email, String id) {
        _name = name;
        _email = email;
        _id = id;
        _isLocked = false;
        _isBanned = false;


    }

    public User(String name, String email, String id, boolean isLocked, boolean isBanned) {
        _name = name;
        _email = email;
        _id = id;
        _isLocked = isLocked;
        _isBanned = isBanned;

    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public boolean is_isLocked() {
        return _isLocked;
    }

    public void set_isLocked(boolean _isLocked) {
        this._isLocked = _isLocked;
    }

    public boolean is_isBanned() {
        return _isBanned;
    }

    public void set_isBanned(boolean _isBanned) {
        this._isBanned = _isBanned;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }


}
