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
     * @param id id of the user
     */
    public User(String name, String email, String id) {
        this.name = name;
        this.email = email;
        this.uid = id;
        this.isLocked = false;
        this.isBanned = false;


    }

    /**
     * Constructor provided with entered info from user
     * @param name name of user
     * @param email email of user
     * @param id uid of user
     * @param isLocked locked status of user
     * @param isBanned banned status of user
     */

    public User(String name, String email, String id, boolean isLocked, boolean isBanned) {
        this.name = name;
        this.email = email;
        this.uid = id;
        this.isLocked = isLocked;
        this.isBanned = isBanned;

    }

    /**
     * gets name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name of the user
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gets the email of the user
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * sets the email of the user
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * gets the locked status of the yser
     * @return bool true or false
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * sets the locked status of the user
     * @param locked
     */
    public void setLocked(boolean locked) {
        this.isLocked = locked;
    }

    /**
     * gets the banned status of the user
     * @return bool t/f
     */
    public boolean isBanned() {
        return isBanned;
    }

    /**
     * sets the banned status of the user
     * @param banned
     */
    public void setBanned(boolean banned) {
        this.isBanned = banned;
    }

    /**
     * gets the uid of the user
     * @return uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * sets the uid of the user
     * @param uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }


}
