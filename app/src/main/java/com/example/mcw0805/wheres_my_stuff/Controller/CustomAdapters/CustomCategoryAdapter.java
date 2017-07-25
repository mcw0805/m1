package com.example.mcw0805.wheres_my_stuff.Controller.CustomAdapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


/**
 * Custom ArrayAdapter for hiding a particular element in an array.
 *
 * @author Chaewon Min
 */
public class CustomCategoryAdapter<T> extends ArrayAdapter<T> {
    private int hidingItemIndex;

    public CustomCategoryAdapter(Context context, int textViewResourceId, T[] objects, int hidingItemIndex) {
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
