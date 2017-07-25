package com.example.mcw0805.wheres_my_stuff;
import com.example.mcw0805.wheres_my_stuff.Controller.FormValidation;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by Chianne on 7/18/17.
 * @author Chianne
 */
public class ChiannesTest {
    private double lat;
    private double lon;
    @Before
    public void setUp() {
    }

    /*
    * This test checks the latitude and longitude at valid locations.
    * This test uses two doubles: the latitude and the longitude. Both doubles are valid.
    * As the method runs, the values should stay the same. The method should be able
    * to tell that the doubles are valid and it should return true.
     */
    @Test
    public void testValidLocation() {
        lat = 1;
        lon = 1;
        assertEquals(true, FormValidation.isValidLocation(lat, lon));
    }

    /*
    * This test checks the latitude and longitude at invalid locations.
    * This test uses two doubles: the latitude and the longitude. The double used
    * for the latitude is invalid, while the double used for the longitude is valid.
    * As the method runs, the values should stay the same. The method should be able
    * to tell that the latitude is invalid and it should return false.
     */
    @Test
    public void testInvalidLocation() {
        lat = 100;
        lon = 1;
        assertEquals(false, FormValidation.isValidLocation(lat, lon));
    }
}
