package com.example.mcw0805.wheres_my_stuff.Model;

/**
 * Created by Ted on 6/20/2017.
 */

public class Admin {
    private String _name;
    private String _email;
    private String _password;

    /**
     * Constructor
     * @param name name of person
     * @param email email of person
     * @param password password of person
     */
    public Admin(String name, String email, String password) {
        _name = name;
        _email = email;
        _password = password;
    }

    private String get_name() {return _name;}
    private String get_email() {return _email;}
    private String get_password() {return _password;}
    private void set_name(String name) {_name = name;}
    private void set_email(String email) {_email = email;}
    private void set_password(String password) {_password = password;}

}
