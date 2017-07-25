package com.example.mcw0805.wheres_my_stuff.Controller;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * A class dedicated to ensure that certain fields are valid in a form before submission.
 *
 * @author Chaewon Min
 */

public class FormValidation {

    /**
     * Checks whether user-inputs in EditText widgets are empty.
     *
     * @param editTexts array of EditText fields that the app should check for
     * @return whether the any of the EditText widget contains empty string
     */
    public static boolean textEmpty(EditText... editTexts) {
        assert editTexts != null;

        for (EditText t : editTexts) {
            if (TextUtils.isEmpty(t.getText().toString())) {
                return true;
            }
        }

        return false;

    }

    /**
     * Checks whether user-input in an EditText widget is empty.
     *
     * @param editText array of EditText fields that the app should check for
     * @return whether the EditText contains any string
     */
    public static boolean textEmpty(EditText editText) {
        assert editText != null;

        return TextUtils.isEmpty(editText.getText().toString());
    }


    /**
     * Checks whether the input in an EditText is an integer.
     *
     * @param text the user-input in the EditText widget
     * @return whether the input is an integer
     */
    public static boolean isValidInteger(EditText text) {
        assert text != null;

        boolean validInteger = true;
        try {
            Integer.parseInt(text.getText().toString());
        } catch (NumberFormatException n) {
            validInteger = false;
        }
        return validInteger;
    }

    /**
     * Checks if field is too long
     * @param text text to check
     * @param maxLength the maxLength
     * @return boolean true or false
     */
    public static boolean fieldTooLong(EditText text, int maxLength) {
        assert text != null;

        boolean tooLong = false;

        if (text.length() > maxLength) {
            tooLong = true;
        }

        return tooLong;
    }

    /**
     * Checks if the zipcode address is valid
     * @param zip zipcode to check
     * @return boolean if valid or not
     */
    public static boolean isValidZipCode(String zip) {
        boolean validZipCode;
        String zipCodePattern = "\\d{5}(-\\d{4})?";

        validZipCode = zip.matches(zipCodePattern);

        return validZipCode;

    }

    /**
     * checks if location is valid
     * @param latitude latitude of location
     * @param longitude longitude of location
     * @return boolean if the location is valid or not
     */
    public static boolean isValidLocation(double latitude, double longitude) {
        final int maxLatitude = 90;
        final int maxLongitude = 180;
        boolean isValid = true;

        if (Math.abs(latitude) > maxLatitude) {
            isValid = false;
        }

        if (Math.abs(longitude) > maxLongitude) {
            isValid = false;
        }
        return isValid;
    }


}
