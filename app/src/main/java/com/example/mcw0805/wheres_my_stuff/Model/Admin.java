package com.example.mcw0805.wheres_my_stuff.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A class representing an Admin of the application.
 *
 * @author Ted Shang
 * @version 1.0
 */
public class Admin {
    private String _name;
    private String _email;
    private String _uid;

    /**
     * Instantiate an Admin object.
     *
     * @param name name of person
     * @param email email of person
     * @param uid uid of person
     */
    public Admin(String name, String email, String uid) {
        _name = name;
        _email = email;
        _uid = uid;
    }

    /**
     * Gets the name of the admin.
     *
     * @return name of admin
     */
    public String getName() {
        return _name;
    }

    /**
     * Gets the email of the admin.
     *
     * @return email of admin
     */
    public String getEmail() {
        return _email;
    }

    /**
     * Sets the name of the admin.
     *
     * @param name name of admin
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Sets the email of the admin
     * @param email of the admin
     */
    public void setEmail(String email) {
        _email = email;
    }


    /**
     * Writes the current admin to the database
     */
    public void writeToDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userRef = database.getReference("admin/" + this._uid + "/");
        DatabaseReference nameChild = userRef.child("name");
        nameChild.setValue(_name);
        DatabaseReference emailChild = userRef.child("email");
        emailChild.setValue(_email);
        DatabaseReference uidChild = userRef.child("uid");
        uidChild.setValue(_uid);
    }
}
