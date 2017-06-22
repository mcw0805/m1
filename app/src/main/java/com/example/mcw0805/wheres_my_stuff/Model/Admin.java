package com.example.mcw0805.wheres_my_stuff.Model;

/**
 * Created by Ted on 6/20/2017.
 */

public class Admin {
    private String _name;
    private String _email;

    /**
     * Constructor
     * @param name name of person
     * @param email email of person
     */
    public Admin(String name, String email, String password) {
        _name = name;
        _email = email;
    }

    private String get_name() {return _name;}
    private String get_email() {return _email;}
    private void set_name(String name) {_name = name;}
    private void set_email(String email) {_email = email;}

}
