package com.example.mcw0805.wheres_my_stuff.Controller;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mcw0805.wheres_my_stuff.R;

public class dashboard_tabs extends FragmentActivity {

    private static final String TAG = "dashboard_tabs";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    public View onCreate(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dashboard_tabs,container, false);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        getSupportFragmentManager().beginTransaction().add(R.id.container, new ItemListViewFragment(),
                "Lost Items Near Me").commit();


        //Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new ItemListViewFragment(), "Lost Items Near Me");
        adapter.addFragment(new FoundItemListFragment(), "Found Items Near Me");
        adapter.addFragment(new SubmitFormFragment(), "Submit New Item Form");
        adapter.addFragment(new DonateItemFormFragment(), "Donate Items");
        //adapter.addFragment(new DonationListFragment(), "List of Donations");
        adapter.addFragment(new MyListFragment(), "My Submitted Items");
        adapter.addFragment(new ProfileFragment(), "Profile");
        viewPager.setAdapter(adapter);
    }
}
