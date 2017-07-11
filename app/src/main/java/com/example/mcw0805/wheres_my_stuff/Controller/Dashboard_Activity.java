package com.example.mcw0805.wheres_my_stuff.Controller;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.mcw0805.wheres_my_stuff.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Dashboard_Activity extends AppCompatActivity {

    private static final String TAG = "dashboard_tabs";

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    //private GoogleMap mMap;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        //Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
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

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng gt = new LatLng(33.7773728, -84.3981109);
//        mMap.addMarker(new MarkerOptions().position(gt).title("Marker at Georgia Tech"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(gt));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gt, 17));
//    }
}
