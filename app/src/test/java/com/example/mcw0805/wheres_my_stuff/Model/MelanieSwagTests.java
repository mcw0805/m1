package com.example.mcw0805.wheres_my_stuff.Model;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Melanie Hall
 *         <p>
 *         Testing {@code filterByActive} method of {@code UserListController}
 */

public class MelanieSwagTests {

    @Test
    public void emptyArrayTest() {
        List<User> input = new ArrayList<>();
        List<User> expected = new ArrayList<>();
        assertExpectedResult(input, expected);
    }

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

    private void assertExpectedResult(List<User> input, List<User> expected) {
        Assert.assertEquals(expected, UserListController.filterByActive(input));
    }
}
