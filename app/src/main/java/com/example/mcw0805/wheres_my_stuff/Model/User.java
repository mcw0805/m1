package com.example.mcw0805.wheres_my_stuff.Model;

/**
 * Created by Ted on 6/14/2017.
 * Creating the user model
 */

public class User {
    private String name;
    private String email;
    private boolean isLocked;
    private boolean isBanned;
    private String uid;


    /**
     * Constructor provided with entered information from user
     *
     * @param name  name of user
     * @param email email of user
     */
    public User(String name, String email, String id) {
        this.name = name;
        this.email = email;
        this.uid = id;
        this.isLocked = false;
        this.isBanned = false;


    }

    public User(String name, String email, String id, boolean isLocked, boolean isBanned) {
        this.name = name;
        this.email = email;
        this.uid = id;
        this.isLocked = isLocked;
        this.isBanned = isBanned;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        this.isLocked = locked;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        this.isBanned = banned;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}
