package com.example.mcw0805.wheres_my_stuff;

import com.example.mcw0805.wheres_my_stuff.Model.FoundItem;
import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.Model.ItemType;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.Model.NeededItem;
import com.example.mcw0805.wheres_my_stuff.Controller.RegistrationActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.repackaged.cglib.proxy.Factory;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

/**
 * Created by Jordan on 7/25/2017.
 */

@PrepareForTest({FirebaseDatabase.class, FirebaseApp.class, LostItem.class, Item.class,Item.ItemFactory.class, FoundItem.class, NeededItem.class})
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
public class JordanTest {
//    @RunWith(PowerMockRunner.class)
//    @PrepareForTest({ FirebaseDatabase.class})
//    private DatabaseReference mockedDatabaseReference;
    @Before
    public void before() {
        PowerMockito.mockStatic(Item.class);
        FirebaseDatabase mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFirebaseDatabase);
    }
    @Test
    public void makeLostItem() {
        LostItem lost = Mockito.mock(LostItem.class);
        Item.ItemFactory factory = Mockito.mock(Item.ItemFactory.class);
        when(factory.makeItem(ItemType.LOST)).thenReturn(lost);
        assertEquals(true, factory.makeItem(ItemType.LOST) instanceof LostItem);
    }
    @Test
    public void makeFoundItem() {
        FoundItem found = Mockito.mock(FoundItem.class);
        Item.ItemFactory factory = Mockito.mock(Item.ItemFactory.class);
        when(factory.makeItem(ItemType.FOUND)).thenReturn(found);
        assertEquals(true, factory.makeItem(ItemType.FOUND) instanceof FoundItem);
    }
    @Test
    public void makeNeededItem() {
        NeededItem need = Mockito.mock(NeededItem.class);
        Item.ItemFactory factory = Mockito.mock(Item.ItemFactory.class);
        when(factory.makeItem(ItemType.NEED)).thenReturn(need);
        assertEquals(true, factory.makeItem(ItemType.NEED) instanceof NeededItem);
    }
    @Test
    public void makeDonationItem() {
        Item donation = Mockito.mock(Item.class);
        Item.ItemFactory factory = PowerMockito.mock(Item.ItemFactory.class);
        when(factory.makeItem(ItemType.DONATION)).thenReturn(donation);
        assertEquals(true, factory.makeItem(ItemType.DONATION) instanceof Item);
    }
}
