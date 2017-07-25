package com.example.mcw0805.wheres_my_stuff.Controller.CustomAdapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mcw0805.wheres_my_stuff.Model.ItemCategory;

/**
 * Custom ArrayAdapter for displaying the ItemCategory enum.
 *
 * Hides the first value, which represents an empty string.
 *
 * @author Chaewon Min
 */
public class CustomCategoryAdapter extends ArrayAdapter<ItemCategory> {
    private int hidingItemIndex;

    public CustomCategoryAdapter(Context context, int textViewResourceId, ItemCategory[] objects, int hidingItemIndex) {
        super(context, textViewResourceId, objects);

        this.hidingItemIndex = hidingItemIndex;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = null;
        if (position == hidingItemIndex) {
            TextView tv = new TextView(getContext());
            tv.setVisibility(View.GONE);
            v = tv;
        } else {
            v = super.getDropDownView(position, null, parent);
        }
        return v;
    }
}
