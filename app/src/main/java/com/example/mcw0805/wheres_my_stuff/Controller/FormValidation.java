package com.example.mcw0805.wheres_my_stuff.Controller;

import android.text.TextUtils;
import android.widget.EditText;

import com.example.mcw0805.wheres_my_stuff.Model.ItemCategory;

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
     * Checks whether an ItemCategory is selected.
     * <p>
     * If it is KEEPSAKE, HEIRLOOM, or MISC, it should return true. Otherwise, false.
     *
     * @param inputItemCategory category of an item
     * @return whether a valid item category was selected
     */
    public static boolean categoryNothingSelected(ItemCategory inputItemCategory) {
        return inputItemCategory == ItemCategory.NOTHING_SELECTED;
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

    public static boolean fieldTooLong(EditText text, int maxLength) {
        assert text != null;

        boolean tooLong = false;

        if (text.length() > maxLength) {
            tooLong = true;
        }

        return tooLong;
    }

    public static boolean isValidZipCode(String zip) {
        boolean validZipCode = false;
        String zipCodePattern = "\\d{5}(-\\d{4})?";

        validZipCode = zip.matches(zipCodePattern);

        return validZipCode;

    }

    public static boolean isValidLocation(double latitude, double longitude) {
        final int MAX_LATITUDE = 90;
        final int MAX_LONGITUDE = 180;
        boolean isValid = true;

        if (Math.abs(latitude) > MAX_LATITUDE) {
            isValid = false;
        }

        if (Math.abs(longitude) > MAX_LONGITUDE) {
            isValid = false;
        }
        return isValid;
    }


}
