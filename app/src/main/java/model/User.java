package model;

/**
 * Created by Ted on 6/14/2017.
 * Creating the user model
 */

public class User {
    private String _username;
    private String _password;
    private boolean _isAdmin;

    /**
     *Constructor provided with entered information from user
     * @param username
     * @param password
     * @param admin
     */
    public User(String username, String password, boolean admin) {
        _username = username;
        _password = password;
        _isAdmin = admin;

    }

    //Getters and Setters
    public String get_username() {
        return _username;
    }
    public String get_password() {
        return _password;
    }
    public boolean get_isAdmin() {
        return _isAdmin;
    }
    public void set_username(String username) {
        _username = username;
    }
    public void set_password(String password) {
        _password = password;
    }
    public void set_isAdmin(boolean admin) {
        _isAdmin = admin;
    }

}
