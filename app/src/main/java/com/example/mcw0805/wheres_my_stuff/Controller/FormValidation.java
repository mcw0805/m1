package com.example.mcw0805.wheres_my_stuff.Controller;

import android.text.TextUtils;
import android.widget.EditText;

import com.example.mcw0805.wheres_my_stuff.Model.ItemCategory;

/**
 * Created by mcw0805 on 6/29/17.
 */

public class FormValidation {

    public static boolean textEmpty(EditText... editTexts) {
        assert editTexts != null;

        for (EditText t : editTexts) {
            if (TextUtils.isEmpty(t.getText().toString())) {
                return true;
            }
        }

        return false;

    }

    public static boolean categoryNothingSelected(ItemCategory inputItemCategory) {
        return inputItemCategory == ItemCategory.NOTHING_SELECTED;
    }

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
