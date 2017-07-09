package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.example.mcw0805.wheres_my_stuff.Model.FoundItem;
import com.example.mcw0805.wheres_my_stuff.Model.ItemCategory;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.Model.User;
import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Map;

public class MyEditableItemActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView myItemName;
    private TextView myItemType;
    private TextView myItemDesc;
    private TextView myItemStat;
    private TextView myItemCat;
    private TextView myItemDate;
    private TextView myLostItemReward;

    private EditText itemNameEdit;
    private EditText itemDescEdit;
    private EditText lostItemRewardEdit;

    private Switch itemStatSwitch;

    private Spinner itemCatSpinner;

    private ToggleButton editItemToggleBtn;

    private ViewSwitcher nameViewSwitcher;
    private ViewSwitcher descViewSwitcher;
    private ViewSwitcher catViewSwitcher;
    private ViewSwitcher rewardViewSwitcher;

    private LinearLayout rewardLinLayout;

    private ArrayList<String> itemKeyList;

    /*
    Firebase authorization
 */
    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean isAuthListenerSet = false;
    private String currUserUID;

    private static final String TAG = "MyEditableItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_editable_item);

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

        currUser = mAuth.getCurrentUser();
        currUserUID = currUser.getUid();

        nameViewSwitcher = (ViewSwitcher) findViewById(R.id.my_item_name_viewSwitcher);
        descViewSwitcher = (ViewSwitcher) findViewById(R.id.my_item_description_viewSwitcher);
        catViewSwitcher = (ViewSwitcher) findViewById(R.id.my_item_category_viewSwitcher);
        rewardViewSwitcher = (ViewSwitcher) findViewById(R.id.my_item_reward_viewSwitcher);

        myItemName = (TextView) findViewById(R.id.my_item_name_text);
        myItemType = (TextView) findViewById(R.id.my_item_type_text);
        myItemDesc = (TextView) findViewById(R.id.my_item_description_text);
        myItemStat = (TextView) findViewById(R.id.my_item_stat_text);
        myItemCat = (TextView) findViewById(R.id.my_item_cat_text);
        myItemDate = (TextView) findViewById(R.id.my_item_post_date_text);

        myLostItemReward = (TextView) findViewById(R.id.my_item_reward_text);

        itemNameEdit = (EditText) findViewById(R.id.my_item_name_edit);
        itemDescEdit = (EditText) findViewById(R.id.my_item_description_edit);
        lostItemRewardEdit = (EditText) findViewById(R.id.my_item_reward_edit);


        itemStatSwitch = (Switch) findViewById(R.id.my_item_status_switch);
        itemStatSwitch.setVisibility(View.INVISIBLE);

        itemCatSpinner = (Spinner) findViewById(R.id.my_item_cat_spinner);
        ArrayAdapter<ItemCategory> category_Adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ItemCategory.values());
        category_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemCatSpinner.setAdapter(category_Adapter);

        editItemToggleBtn = (ToggleButton) findViewById(R.id.edit_item_ToggleBtn);
        editItemToggleBtn.setOnClickListener(this);

        rewardLinLayout = (LinearLayout) findViewById(R.id.reward_lin_layout);


        Intent intent = getIntent();
        LostItem selectedLostItem = null;
        FoundItem selectedFoundItem = null;
        if (intent.getParcelableExtra("selectedLostItem") != null
                && intent.getParcelableExtra("selectedLostItem") instanceof LostItem) {
            selectedLostItem = intent.getParcelableExtra("selectedLostItem");
        } else if (intent.getParcelableExtra("selectedFoundItem") != null
                && intent.getParcelableExtra("selectedFoundItem") instanceof FoundItem) {
            selectedFoundItem = intent.getParcelableExtra("selectedFoundItem");

        }

        if (selectedLostItem != null) {
            myItemName.setText(selectedLostItem.getName());
            myItemType.setText(selectedLostItem.getItemType().toString());
            myItemDesc.setText(selectedLostItem.getDescription());
            myItemCat.setText(selectedLostItem.getCategory().toString());
            myItemStat.setText(selectedLostItem.getStatusString());

            DateFormat df = new java.text.SimpleDateFormat("yyyy MMMM dd hh:mm aaa");
            myItemDate.setText(df.format(selectedLostItem.getDate()));
            myLostItemReward.setText("$" + selectedLostItem.getReward());

            itemStatSwitch.setChecked(selectedLostItem.getIsOpen());


        } else if (selectedFoundItem != null) {
            myItemName.setText(selectedFoundItem.getName());
            myItemType.setText(selectedFoundItem.getItemType().toString());
            myItemDesc.setText(selectedFoundItem.getDescription());
            myItemCat.setText(selectedFoundItem.getCategory().toString());
            myItemStat.setText(selectedFoundItem.getStatusString());
            itemStatSwitch.setChecked(selectedFoundItem.getIsOpen());
            DateFormat df = new java.text.SimpleDateFormat("yyyy MMMM dd hh:mm aaa");
            myItemDate.setText(df.format(selectedFoundItem.getDate()));
            rewardLinLayout.setVisibility(View.INVISIBLE);
        }

        editItemToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final String newItemName = itemNameEdit.getText().toString();
                final String newItemDesc = itemDescEdit.getText().toString();
                final String newItemCat = itemCatSpinner.getSelectedItem().toString();
                final boolean newStat = itemStatSwitch.isChecked();
                //final Integer newItemReward = Integer.parseInt(lostItemRewardEdit.getText().toString());

                if (isChecked) { //edit mode is on
                    nameViewSwitcher.showPrevious();
                    descViewSwitcher.showPrevious();
                    catViewSwitcher.showPrevious();
                    itemStatSwitch.setVisibility(View.VISIBLE);
                    itemCatSpinner.setSelection(ItemCategory.valueOf(myItemCat.getText().toString()).ordinal());

                } else {
                    nameViewSwitcher.showNext();
                    descViewSwitcher.showNext();
                    catViewSwitcher.showNext();
                    itemStatSwitch.setVisibility(View.INVISIBLE);
                }

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!isAuthListenerSet) {
            mAuth.addAuthStateListener(mAuthListener);
            isAuthListenerSet = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
            isAuthListenerSet = false;
        }
    }

    @Override
    public void onClick(View v) {

    }

    private void resetEditedLostItemFields(final String name, final String description,
                                   final String itemCat, final boolean stat) {

        final DatabaseReference lostItemRef = LostItem.getLostItemsRef();




    }
}
