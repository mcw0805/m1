package com.example.mcw0805.wheres_my_stuff.Model;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Ted on 6/14/2017.
 * Creating the user model
 */

public class User implements Parcelable {
    private String name;
    private String email;
    private boolean isLocked;
    private boolean isBanned;
    private String uid;
    private int itemCount = 0;
    private int lockAttempts = 0;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference userRef = database.getReference("users/" + this.uid);

    public User() {

    }


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

    public User(String name, String email, String id, boolean isLocked, boolean isBanned, int itemCount, int lockAttempts) {
        this.name = name;
        this.email = email;
        this.uid = id;
        this.isLocked = isLocked;
        this.isBanned = isBanned;
        this.itemCount = itemCount;
        this.lockAttempts = lockAttempts;

    }

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        isLocked = in.readByte() != 0;
        isBanned = in.readByte() != 0;
        uid = in.readString();
        itemCount = in.readInt();
        lockAttempts = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeByte((byte) (isLocked ? 1 : 0));
        dest.writeByte((byte) (isBanned ? 1 : 0));
        dest.writeString(uid);
        dest.writeInt(itemCount);
        dest.writeInt(lockAttempts);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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
    public boolean getIsLocked() {
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
    public boolean getIsBanned() {
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
    public void setItemCount(int k) {
        itemCount = k;
    }
    public int getItemCount() {
        return itemCount;
    }
    public int getLockAttempts() {
        return lockAttempts;
    }
    public void setLockAttempts(int k) {
        lockAttempts = k;
    }
    public void addLockAttempts() {
        lockAttempts++;
    }

    /**
     * Writes the user to the database
     */
    public void writeToDatabase() {

        DatabaseReference nameChild = userRef.child("name");
        nameChild.setValue(getName());
        DatabaseReference emailChild = userRef.child("email");
        emailChild.setValue(getEmail());
        DatabaseReference lockedChild = userRef.child("locked");
        lockedChild.setValue(getIsLocked());
        DatabaseReference bannedChild = userRef.child("banned");
        bannedChild.setValue(getIsBanned());
        DatabaseReference uidChild = userRef.child("uid");
        uidChild.setValue(getUid());
        DatabaseReference itemCountChild = userRef.child("itemCount");
        itemCountChild.setValue(getItemCount());
        DatabaseReference lockAttemptsChild = userRef.child("lockAttempts");
        lockAttemptsChild.setValue(getLockAttempts());

    }

    public static User buildUserObject(DataSnapshot dataSnap) {
        DataSnapshot name = dataSnap.child("name");
        DataSnapshot email = dataSnap.child("email");
        DataSnapshot locked = dataSnap.child("locked");
        DataSnapshot banned = dataSnap.child("banned");
        DataSnapshot uid = dataSnap.child("uid");
        DataSnapshot itemCount = dataSnap.child("itemCount");
        DataSnapshot lockAttempts = dataSnap.child("lockAttempts");

        String _name = (String) name.getValue();
        String _email = (String) email.getValue();
        boolean _isLocked = (Boolean) locked.getValue();
        boolean _isBanned =  (Boolean) banned.getValue();
        String _uid = (String) uid.getValue();
        int _itemCount = Integer.parseInt(String.valueOf(itemCount.getValue()));
        int _lockAttempts = Integer.parseInt(String.valueOf(lockAttempts.getValue()));

        return new User(_name, _email, _uid, _isLocked, _isBanned, _itemCount, _lockAttempts );
    }

    /**
     * increments user's count when the user creates a new item.
     */
    public void addCount() {
        this.itemCount++;
    }

}
