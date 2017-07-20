package com.example.mcw0805.wheres_my_stuff.Model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * A test for filtering Item objects by category.
 *
 * @author Chaewon Min
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Item.class)
public class ItemFilterTest {
    //String name, String description, long date, double longitude,
    //double latitude, ItemCategory category, String uid, boolean isOpen

    private List<Item> itemList = new ArrayList<>();

    private List<Item> misc = new ArrayList<>();
    private List<Item> keepsake = new ArrayList<>();
    private List<Item> heirloom = new ArrayList<>();



    @Before
    public void setUp() {
        Item item1 = PowerMockito.spy(new Item("i1", "d1", new Date().getTime(),
                -13.1, -13.2, ItemCategory.MISC, "id", true));
        Item item2 = PowerMockito.spy(new Item("i2", "d2", new Date().getTime(),
                -43.1, -13.2, ItemCategory.KEEPSAKE, "id", true));
        Item item3 = PowerMockito.spy(new Item("i3", "d3", new Date().getTime(),
                66.1, -13.2, ItemCategory.MISC, "id", true));
        Item item4 = PowerMockito.spy(new Item("i4", "d4", new Date().getTime(),
                23.1, -13.2, ItemCategory.MISC, "id", true));
        Item item5 = PowerMockito.spy(new Item("i5", "d5", new Date().getTime(),
                23.1, -13.2, ItemCategory.HEIRLOOM, "id", true));
        Item item6 = PowerMockito.spy(new Item("i6", "d6", new Date().getTime(),
                -23.1, -13.2, ItemCategory.KEEPSAKE, "id", true));
        Item item7 = PowerMockito.spy(new Item("i7", "d7", new Date().getTime(),
                83.1, -13.2, ItemCategory.MISC, "id", true));
        Item item8 = PowerMockito.spy(new Item("i8", "d8", new Date().getTime(),
                23.1, -13.2, ItemCategory.KEEPSAKE, "id", true));
        Item item9 = PowerMockito.spy(new Item("i9", "d9", new Date().getTime(),
                23.1, -13.2, ItemCategory.HEIRLOOM, "id", true));
        Item item10 = PowerMockito.spy(new Item("i10", "d10", new Date().getTime(),
                23.1, -13.2, ItemCategory.MISC, "id", true));

        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
        itemList.add(item4);
        itemList.add(item5);
        itemList.add(item6);
        itemList.add(item7);
        itemList.add(item8);
        itemList.add(item9);
        itemList.add(item10);

        misc.add(item1);
        misc.add(item3);
        misc.add(item4);
        misc.add(item7);
        misc.add(item10);

        keepsake.add(item2);
        keepsake.add(item6);
        keepsake.add(item8);

        heirloom.add(item5);
        heirloom.add(item9);


    }

    @Test
    public void testMock() throws Exception {
        Item a = PowerMockito.spy(new Item("i1", "d1", new Date().getTime(),
                13.1, -13.2, ItemCategory.MISC, "id", true));

        PowerMockito.when(a, "getName").thenReturn("i22");

        assertNotEquals("i1", a.getName());

        Mockito.verify(a);
    }

    @Test
    public void filterByCategoryMisc() {
        List<Item> miscListResult = Item.filterByCategory(itemList, ItemCategory.MISC);

        for (Item i : miscListResult) {
            assertTrue(i.getCategory() == ItemCategory.MISC);
        }

        assertEquals(misc.size(), miscListResult.size());
        assertEquals(misc, miscListResult);
        assertNotEquals(keepsake, miscListResult);
        assertNotEquals(heirloom, miscListResult);
    }

    @Test
    public void filterByCategoryHeirloom() {
        List<Item> heirloomListResult = Item.filterByCategory(itemList, ItemCategory.HEIRLOOM);

        for (Item i : heirloomListResult) {
            assertTrue(i.getCategory() == ItemCategory.HEIRLOOM);
        }

        assertEquals(heirloom.size(), heirloomListResult.size());
        assertEquals(heirloom, heirloomListResult);
        assertNotEquals(keepsake, heirloomListResult);
        assertNotEquals(misc, heirloomListResult);
    }

    @Test
    public void filterByKeepSake() {
        List<Item> keepsakeList = Item.filterByCategory(itemList, ItemCategory.KEEPSAKE);

        for (Item i : keepsakeList) {
            assertTrue(i.getCategory() == ItemCategory.KEEPSAKE);
        }

        assertEquals(keepsake.size(), keepsakeList.size());
        assertEquals(keepsake, keepsakeList);
        assertNotEquals(heirloom, keepsakeList);
        assertNotEquals(misc, keepsakeList);
    }

    @Test
    public void filterNothingSelected() {

//        TODO: FIX - was causing compiler errors
//        //should return all of the items if nothing is selected
//        List<Item> list = Item.filterByCategory(itemList, ItemCategory.NOTHING_SELECTED);
//        assertEquals(itemList.size(), list.size());
//        assertEquals(itemList, list);

    }


    @Test(expected = NullPointerException.class)
    public void testNullPointer() {
        List<Item> list = Item.filterByCategory(null, ItemCategory.KEEPSAKE);
    }

    @Test
    public void testEmptyList() {
        List<Item> list = Item.filterByCategory(new ArrayList<Item>(), ItemCategory.KEEPSAKE);
        assertEquals(0, list.size());
        assertNotEquals(100, list.size());
    }

}