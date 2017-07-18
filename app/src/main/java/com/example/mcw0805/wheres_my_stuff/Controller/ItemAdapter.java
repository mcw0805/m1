package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mcw0805 on 7/3/17.
 */

public class ItemAdapter extends ArrayAdapter<Item> {

    private List<Item> items;
    private List<Item> originalItems = new ArrayList<>();
    private List<Item> filtered;
    private ItemFilter itemFilter;
    private final Object mLock = new Object();
    private int len;

    /**
     * Constructor for item adapter
     * @param context context for the item adapter
     * @param resource layout resources
     * @param newItems items added to list
     */
    public ItemAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Item> newItems) {
        super(context, resource, newItems);
        this.items = newItems;
        filtered = items;
        cloneItems(newItems);
    }

    /**
     * clones the items to a different list
     * @param itemList list to be cloned
     */
    protected void cloneItems(List<Item> itemList) {
        for (Iterator iterator = itemList.iterator(); iterator
                .hasNext();) {
            Item i = (Item) iterator.next();
            originalItems.add(i);
        }
    }

    @Override
    public int getCount() {
        synchronized (mLock) {
            return items != null ? items.size() : 0;
        }
    }

    @Override
    public Item getItem(int item) {
        Item i = null;
        synchronized (mLock) {
            i = items != null ? items.get(item) : null;

        }

        return i;
    }

    @Override
    public View getView(int position, View contextView, ViewGroup parent) {
        View v = contextView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_row_layout, null);
        }

        Item i = null;
        synchronized (mLock) {
            i = items.get(position);
        }

        if (i != null) {
            TextView itemName = (TextView) v.findViewById(R.id.textView);
            if (itemName != null) {
                itemName.setText(i.toString());
            }

        }

        return v;

    }

    @Override
    public Filter getFilter() {
        if (itemFilter == null) {
            itemFilter = new ItemFilter();
        }

        return itemFilter;
    }

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final FilterResults results = new FilterResults();

            if (len != 0 && len > constraint.length()) {
                items.clear();
                items.addAll(originalItems);
                len = constraint.length();

            }

            len = constraint.length();

            if (originalItems == null) {
                synchronized (this) {
                    originalItems = new ArrayList<>(items);
                }
            }

            if (constraint == null || constraint.length() == 0) {

                synchronized (mLock) {

                    results.values = originalItems;
                    results.count = originalItems.size();
                }

            } else {
                final ArrayList<Item> values;
                final String prefixString = constraint.toString().toLowerCase();

                synchronized (mLock) {
                    values = new ArrayList<>(items);
                }

                final int count = values.size();
                final ArrayList<Item> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final Item val = values.get(i);
                    final String valText = val.getName();

                    if (valText.toLowerCase().startsWith(prefixString)) {
                        newValues.add(val);
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            synchronized (mLock) {
                filtered = (ArrayList<Item>) results.values;
                final ArrayList<Item> localItems = (ArrayList<Item>) results.values;
                notifyDataSetChanged();
                clear();
                //Add the items back in
                for (Iterator iterator = localItems.iterator(); iterator
                        .hasNext();) {
                    Item i = (Item) iterator.next();
                    add(i);
                }

            }
            //end synchronized
        }
    }


}