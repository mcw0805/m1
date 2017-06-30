package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.google.firebase.auth.FirebaseAuth;

import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.auth.FirebaseUser;

/**
 * Controller for showing a user's profile page.
 *
 * @author Melanie Hall
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    /*
        Widgets for the profile activity.
    */
    private ToggleButton editToggleButton;
    private EditText nicknameEdit;
    private TextView nicknameTextView;
    private EditText introductionEdit;
    private TextView introductionTextView;
    private ViewSwitcher nicknameViewSwitcher;
    private ViewSwitcher introductionViewSwitcher;

    /*
        Firebase authorization
     */
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean isAuthListenerSet = false;

    private static final String TAG = "ProfileActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //instantiate buttons
        editToggleButton = (ToggleButton) findViewById(R.id.editToggleBtn);

        //instantiate all the fields related to editing/viewing the nickname of the user
        nicknameEdit = (EditText) findViewById(R.id.nicknameEdit);
        nicknameTextView = (TextView) findViewById(R.id.nicknameText);
        nicknameEdit.setText(nicknameTextView.getText().toString());

        nicknameViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcherNickname);

        //instantiate all the fields related to editing/viewing the intro/bio of the user
        introductionEdit = (EditText) findViewById(R.id.introductionEdit);
        introductionTextView = (TextView) findViewById(R.id.introductionText);

        introductionViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcherIntroduction);

        //listeners for the buttons
        editToggleButton.setOnClickListener(this);

        editToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String newNickname = nicknameEdit.getText().toString();
                String newIntro = introductionEdit.getText().toString();

                if (isChecked) { //edit is clicked (i.e. button is "on")
                    nicknameViewSwitcher.showPrevious(); //shows EditText
                    nicknameTextView.setText(newNickname);

                    introductionViewSwitcher.showPrevious();
                    introductionTextView.setText(newIntro);

                } else { //back to TextView
                    nicknameViewSwitcher.showNext();
                    nicknameTextView.setText(newNickname);

                    introductionViewSwitcher.showNext();
                    introductionTextView.setText(newIntro);
                }
            }
        });


        //Firebase authorization
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!isAuthListenerSet) {
            FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
            isAuthListenerSet = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
            isAuthListenerSet = false;
        }
    }


    @Override
    public void onClick(View view) {

        //hides keyboard when done editing
        if (view == editToggleButton) {
            if (!editToggleButton.isChecked()) {
                hideSoftKeyboard(view);
            }
        }

    }

    /**
     * Hides the soft keyboard.
     *
     * Called when EditText fields are not in focus.
     *
     * @param v view of the current focus
     */
    private void hideSoftKeyboard(View v){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


}
