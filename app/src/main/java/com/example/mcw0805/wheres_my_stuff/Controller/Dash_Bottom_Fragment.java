package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Chianne on 7/9/17.
 */


public class Dash_Bottom_Fragment extends Fragment implements View.OnClickListener {

    private TextView welcome;
    private TextView lostNearMe;
    private TextView foundNearMe;
    private TextView submitted_items;
    private TextView newLost;
    private TextView donate_list;
    private TextView donate;
    private TextView profile_page;
    private Button logout_dash;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean isAuthListenerSet = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_dashboard_bottom, group, false);

        lostNearMe = (TextView) rootView.findViewById(R.id.lost_txt);
        foundNearMe = (TextView) rootView.findViewById(R.id.found_txt);
        submitted_items = (TextView) rootView.findViewById(R.id.reportFound_txt);
        newLost = (TextView) rootView.findViewById(R.id.reportLost_txt);
        donate_list = (TextView) rootView.findViewById(R.id.donate_list);
        donate = (TextView) rootView.findViewById(R.id.donate_txt);
        profile_page = (TextView) rootView.findViewById(R.id.profile_txt);
        welcome = (TextView) rootView.findViewById(R.id.welcome);
        logout_dash = (Button) rootView.findViewById(R.id.logout_button);

        lostNearMe.setOnClickListener(this);
        foundNearMe.setOnClickListener(this);
        submitted_items.setOnClickListener(this);
        newLost.setOnClickListener(this);
        donate_list.setOnClickListener(this);
        donate.setOnClickListener(this);
        profile_page.setOnClickListener(this);
        logout_dash.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();


        //Firebase authorization
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Dashbot", "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d("Dashbot", "onAuthStateChanged:signed_out");
                }
            }
        };

        return rootView;
    }


    @Override
    public void onClick(View v) {
        if (v.equals(newLost)) {
            Intent intent = new Intent(getActivity(), SubmitFormActivity.class);
            Dash_Bottom_Fragment.this.startActivity(intent);
        } else if (v.equals(lostNearMe)) {
            Intent intent = new Intent(getActivity(), ItemListViewActivity.class);
            intent.putExtra("DashboardClikedListType", "LostItemListView");
            Dash_Bottom_Fragment.this.startActivity(intent);
        } else if (v.equals(foundNearMe)) {
            Intent intent = new Intent(getActivity(), ItemListViewActivity.class);
            intent.putExtra("DashboardClikedListType", "FoundItemListView");
            Dash_Bottom_Fragment.this.startActivity(intent);
        } else if (v.equals(profile_page)) {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            Dash_Bottom_Fragment.this.startActivity(intent);
        }  else if (v.equals(donate)) {
            Intent intent = new Intent(getActivity(), DonateItemFormActivity.class);
            Dash_Bottom_Fragment.this.startActivity(intent);
        } else if (v.equals(logout_dash)) {
            signOut();
            Toast.makeText(getActivity(),
                    "Successfully signed out.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();

        } else if (v.equals(submitted_items)) {
            Intent intent = new Intent(getActivity(), MyListActivity.class);
            Dash_Bottom_Fragment.this.startActivity(intent);
        }
    }

    /**
     * Method that signs a user out.
     */
    private void signOut() {
        mAuth.signOut();
        Log.d("Dashboard nav", "signed out");
    }
}