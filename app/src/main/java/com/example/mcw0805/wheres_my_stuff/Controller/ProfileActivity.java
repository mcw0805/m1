package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener  {

    /*
      Instance variables for log out button/allowing user to log out.
    */
    private Button signOutProfileButton;
    private ToggleButton editToggleBtn;
    private EditText nicknameEdit;
    private TextView nicknameTextView;
    private ViewSwitcher nicknameViewSwitcher;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean isAuthListenerSet = false;
    private static final String TAG = "ProfileActivity";

    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        signOutProfileButton = (Button) findViewById(R.id.profile_logout_btn);
        editToggleBtn = (ToggleButton) findViewById(R.id.editToggleBtn);

        nicknameEdit = (EditText) findViewById(R.id.nicknameEdit);
        nicknameTextView = (TextView) findViewById(R.id.nicknameText);
        nicknameEdit.setText(nicknameTextView.getText().toString());

        nickname = nicknameTextView.getText().toString();

        nicknameViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcherNickname);

//        CompoundButton.OnCheckedChangeListener listener =
//                new CompoundButton.OnCheckedChangeListener() {
//
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                    }
//                };

        signOutProfileButton.setOnClickListener(this);
        //editToggleBtn.setOnCheckedChangeListener(listener);




        editToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String newNickname = nicknameEdit.getText().toString();
                if (isChecked) { //edit is clicked (i.e. button is "on")
                        nicknameViewSwitcher.showPrevious(); //shows EditText

                        nicknameTextView.setText(newNickname);

                } else { //back to TextView
                    nicknameViewSwitcher.showNext();
                    nicknameTextView.setText(newNickname);

                }
            }
        });


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

    /**
     * Finds user and starts application.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (!isAuthListenerSet) {
            FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
            isAuthListenerSet = true;
        }
    }

    /**
     * Stops application and returns to login screen.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
            isAuthListenerSet = false;
        }
    }

    /**
     * onClick event that checks if user signs out.
     * @param view checks if user signs out
     */
    @Override
    public void onClick(View view) {
        if (view == signOutProfileButton) {
            signOut();
            Toast.makeText(getApplicationContext(),
                    "Successfully signed out.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }


    }

    private void makeEditable() {
        if (nicknameViewSwitcher.getCurrentView() != nicknameTextView) {
            nicknameViewSwitcher.showPrevious();
        }

    }



    /**
     * Method that signs a user out.
     */
    private void signOut() {
        mAuth.signOut();
        Log.d(TAG, "signed out");
    }
}
