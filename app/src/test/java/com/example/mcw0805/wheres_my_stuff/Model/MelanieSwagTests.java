package com.example.mcw0805.wheres_my_stuff.Model;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Melanie Hall
 * Testing {@code filterByActive} method of {@code UserListController}
 */

public class MelanieSwagTests {

    /*
    * This test checks whether there's an empty list of Users.
    * This test uses two Lists: an input list of zero Users and
    * an expected list of zero Users.
    * As the method runs, no Users should be added to either lists.
    * The method should be able to check that both lists contain
    * zero Users and should return true.
     */
    @Test
    public void emptyArrayTest() {
        List<User> input = new ArrayList<>();
        List<User> expected = new ArrayList<>();
        assertExpectedResult(input, expected);
    }

    /*
    * This test checks for all active Users in a list of Users.
    * This test uses two Lists: an input list of two mock Users and
    * an expected list of two mock Users.
    * As the method runs, two mock Users should be added to both lists.
    * The method should be able to check that both lists contain
    * two mock Users and should return true.
     */
    @Test
    public void allActiveUsersTest() {
        List<User> input = new ArrayList<>();
        User user1 = Mockito.mock(User.class);
        User user2 = Mockito.mock(User.class);
        Collections.addAll(input, user1, user2);
        List<User> expected = new ArrayList<>();
        Collections.addAll(expected, user1, user2);
        assertExpectedResult(input, expected);
    }

    /*
    * This test checks a list of Users when there are some banned Users.
    * This test uses two Lists: an input list of two mock Users, one that
    * has been banned, and an expected list of one mock User.
    * As the method runs, two mock Users should be added to the input list, but one
    * is set to true as banned; one User is added to the expected list.
    * The method should be able to check that both lists contain
    * one active User and should return true.
     */
    @Test
    public void someActiveUsersTest() {
        List<User> input = new ArrayList<>();
        User user1 = Mockito.mock(User.class);
        User user2 = Mockito.mock(User.class);
        Mockito.when(user1.getIsBanned()).thenReturn(true);
        Collections.addAll(input, user1, user2);
        List<User> expected = new ArrayList<>();
        expected.add(user2);
        assertExpectedResult(input, expected);
    }

    /*
    * This test checks a list of Users when there are no active Users.
    * This test uses two Lists: an input list of two mock Users, both that
    * have been banned, and an empty ArrayList of Users.
    * As the method runs, two mock Users should be added to the input list, both
    * are set to true as banned.
    * The method should be able to check that both lists contain
    * no active Users and should return true.
     */
    @Test
    public void noActiveUsersTest() {
        List<User> input = new ArrayList<>();
        User user1 = Mockito.mock(User.class);
        User user2 = Mockito.mock(User.class);
        Mockito.when(user1.getIsBanned()).thenReturn(true);
        Mockito.when(user2.getIsBanned()).thenReturn(true);
        Collections.addAll(input, user1, user2);
        assertExpectedResult(input, new ArrayList<User>());
    }

    /*
    * A helper method for assertEquals().
    * @param input List of Users before running method
    * @param expected List of Users that's expected
     */
    private void assertExpectedResult(List<User> input, List<User> expected) {
        Assert.assertEquals(expected, UserListController.filterByActive(input));
    }
}
