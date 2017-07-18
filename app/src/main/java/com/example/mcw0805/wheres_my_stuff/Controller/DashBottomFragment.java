package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.mcw0805.wheres_my_stuff.R;

public class DashBottomFragment extends Fragment implements View.OnClickListener {

    /*
        Widgets
     */
    private TextView lostNearMe;
    private TextView foundNearMe;
    private TextView submittedItems;
    private TextView newLost;
    private TextView donateList;
    private TextView donate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.activity_dashboard_bottom, group, false);

        lostNearMe = (TextView) rootView.findViewById(R.id.lost_txt);
        foundNearMe = (TextView) rootView.findViewById(R.id.found_txt);
        submittedItems = (TextView) rootView.findViewById(R.id.reportFound_txt);
        newLost = (TextView) rootView.findViewById(R.id.reportLost_txt);
        donateList = (TextView) rootView.findViewById(R.id.donate_list);
        donate = (TextView) rootView.findViewById(R.id.donate_txt);


        lostNearMe.setOnClickListener(this);
        foundNearMe.setOnClickListener(this);
        submittedItems.setOnClickListener(this);
        newLost.setOnClickListener(this);
        donateList.setOnClickListener(this);
        donate.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onClick(View v) {
        if (v == newLost) {
            startActivity(new Intent(getActivity(), SubmitFormActivity.class));
        }

        if (v == lostNearMe) {
            Intent intent = new Intent(getActivity(), ItemListViewActivity.class);
            intent.putExtra("DashboardClickedListType", "LostItemListView");
            DashBottomFragment.this.startActivity(intent);
        }

        if (v == foundNearMe) {
            Intent intent = new Intent(getActivity(), ItemListViewActivity.class);
            intent.putExtra("DashboardClickedListType", "FoundItemListView");
            DashBottomFragment.this.startActivity(intent);

        }
        if (v == donate) {
            startActivity(new Intent(getActivity(), DonateItemFormActivity.class));
        }

        if (v == donateList) {
            Intent intent = new Intent(getActivity(), ItemListViewActivity.class);
            intent.putExtra("DashboardClickedListType", "helpOut");
            DashBottomFragment.this.startActivity(intent);
        }

        if (v == submittedItems) {
            startActivity(new Intent(getActivity(), MyListActivity.class));
        }
    }


}