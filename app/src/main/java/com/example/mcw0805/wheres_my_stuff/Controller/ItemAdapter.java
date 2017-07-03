package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.mcw0805.wheres_my_stuff.Model.FoundItem;
import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcw0805 on 7/3/17.
 */

public class ItemAdapter extends BaseAdapter implements Filterable {



    private final Context mContext;
    private final Object mLock = new Object();

    private List<Item> list;

    private ArrayList<Item> mOriginalVals;
    private List<Item> fList;

    private boolean mNotifyOnChange = true;


    public ItemAdapter(@NonNull Context context, List<Item> itemList) {
        this.list = itemList;
        //this.fList = this.list;
        this.mContext = context;
    }


    public void add(@Nullable Item object) {
        synchronized (mLock) {
            if (mOriginalVals != null) {
                mOriginalVals.add(object);
            } else {
                list.add(object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Item getItem(int pos) {
        return this.list.get(pos);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contextView, ViewGroup parent) {

        View v = View.inflate(mContext, R.layout.item_row_layout, null);

        TextView itemName = (TextView) v.findViewById(R.id.textView);
        itemName.setText(list.get(position).getName());

        v.setTag(list.get(position).getName());

        return v;

    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults results = new FilterResults();

                if (mOriginalVals == null) {
                    synchronized (mLock) {
                        mOriginalVals = new ArrayList<>(list);
                    }
                }

                if (constraint == null || constraint.length() == 0) {

                    final ArrayList<Item> itemArrayList;
                    synchronized (mLock) {
                        itemArrayList = new ArrayList<>(list);
                    }
                    results.values = itemArrayList;
                    results.count = itemArrayList.size();
                } else {
                    final ArrayList<Item> values;
                    synchronized (mLock) {
                        values = new ArrayList<>(list);
                    }

                    final int count = values.size();
                    final ArrayList<Item> newValues = new ArrayList<>();

                    for (int i = 0; i < count; i++) {
                        final Item val = values.get(i);
                        final String valText = val.getName();

                        if (valText.toLowerCase().contains(constraint.toString())) {
                            newValues.add(val);
                        }

                    }
                    results.values = newValues;
                    results.count = newValues.size();
                    Log.d("ADAPT", newValues.size() + "");


                }

//                if (constraint == null || constraint.length() == 0) {
//                    results.values = list;
//                    results.count = list.size();
//                } else {
//                    List<Item> filteredList = new ArrayList<>();
//
//                    for (int i = 0; i < list.size(); i++) {
//                        if (list.get(i).getName().toLowerCase().contains(constraint.toString())) {
//                            if (list.get(i) instanceof FoundItem) {
//                                FoundItem f = (FoundItem) list.get(i);
//                                filteredList.add(f);
//                            } else if (list.get(i) instanceof LostItem) {
//                                LostItem l = (LostItem) list.get(i);
//                                filteredList.add(l);
//                            }
//                        }
//                    }
//
//                    results.values = filteredList;
//                    Log.d("ADAPT", filteredList.size() + "");
//                    results.count = filteredList.size();
//                }

                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {



                list = (List<Item>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }

//                fList = (ArrayList<Item>) results.values;
//                notifyDataSetChanged();

            }
        };
    }

    public void refresh(List<Item> newList) {
        list = newList;
    }
}
