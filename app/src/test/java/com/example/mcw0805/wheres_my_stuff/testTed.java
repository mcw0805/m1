package com.example.mcw0805.wheres_my_stuff;
import com.example.mcw0805.wheres_my_stuff.Controller.LogInActivity;
import com.example.mcw0805.wheres_my_stuff.Model.User;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by Ted on 7/18/2017.
 * Test the login feature with multiple users
 * Signature: login()
 * Preconditions: User has filled in valid fields and clicked login
 * Postconditions: User is either authenticated and led to the dashboard,
 * or alerted what has failed
 * Invariants: User will try to resolve an issue or progress with the app
 * Framing conditions: All validation is handled by Firebase
 */
public class testTed {
    LogInActivity login = new LogInActivity();

    @Test
    public void bannedSign() {
        User banned = Mockito.mock(User.class);
        Mockito.when(banned.getIsBanned()).thenReturn(true);
        Assert.assertEquals(true, login.testBan(banned));

    }
    @Test
    public void unbannedSign() {
        User unbanned = Mockito.mock(User.class);
        Mockito.when(unbanned.getIsBanned()).thenReturn(false);
        Assert.assertEquals(false, login.testBan(unbanned));

    }
    @Test
    public void lockedSign() {
        User locked = Mockito.mock(User.class);
        Mockito.when(locked.getIsLocked()).thenReturn(true);
        Assert.assertEquals(true, login.testLock(locked));

    }
    @Test
    public void unlockedSign() {
        User unlocked = Mockito.mock(User.class);
        Mockito.when(unlocked.getIsLocked()).thenReturn(false);
        Assert.assertEquals(false, login.testLock(unlocked));


    }
}
