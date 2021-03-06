package com.example.mcw0805.wheres_my_stuff.Controller;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.example.mcw0805.wheres_my_stuff.Controller.CustomAdapters.CustomCategoryAdapter;
import com.example.mcw0805.wheres_my_stuff.Model.FoundItem;
import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.Model.ItemCategory;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.example.mcw0805.wheres_my_stuff.Model.NeededItem;
import com.example.mcw0805.wheres_my_stuff.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;

/**
 * Controller class for updating my uploaded items on the application.
 * <p>
 * Features include editing name, description, status, category, and reward.
 * One can also delete this chosen item.
 *
 * @author Chaewon Min
 */
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

    private Button deleteBtn;
    private Button uploadImgBtn;
    private Button takePicBtn;

    private ImageView uploadedImg;

    private ViewSwitcher nameViewSwitcher;
    private ViewSwitcher descViewSwitcher;
    private ViewSwitcher catViewSwitcher;
    private ViewSwitcher rewardViewSwitcher;

    private LinearLayout rewardLinLayout;

    private ProgressDialog progressDialog;

    private String itemKey;
    private Item selected;


    /*
        Firebase authorization
    */
    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean isAuthListenerSet = false;

    private DatabaseReference itemsRef;
    private final DatabaseReference imgRef = FirebaseDatabase.getInstance().getReference("pics");

    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_INTENT = 2;

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

        itemCatSpinner.setAdapter(new CustomCategoryAdapter(
                this, android.R.layout.simple_spinner_item, ItemCategory.values(), 0));

        editItemToggleBtn = (ToggleButton) findViewById(R.id.edit_item_ToggleBtn);
        editItemToggleBtn.setOnClickListener(this);

        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(this);
        deleteBtn.setVisibility(View.GONE);

        uploadImgBtn = (Button) findViewById(R.id.uploadImgBtn);
        uploadImgBtn.setOnClickListener(this);
        uploadImgBtn.setVisibility(View.GONE);

        takePicBtn = (Button) findViewById(R.id.takePicBtn);
        takePicBtn.setOnClickListener(this);
        takePicBtn.setVisibility(View.GONE);

        uploadedImg = (ImageView) findViewById(R.id.uploadedImg);

        rewardLinLayout = (LinearLayout) findViewById(R.id.reward_lin_layout);

        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();

        selected = intent.getParcelableExtra("selected");
        itemKey = intent.getStringExtra("userItemPushKey");
        loadImage(itemKey);

        if (selected instanceof LostItem) {
            itemsRef = LostItem.getLostItemsRef().child(itemKey);

        } else if (selected instanceof FoundItem) {
            itemsRef = FoundItem.getFoundItemsRef().child(itemKey);

        } else if (selected instanceof NeededItem) {
            itemsRef = ((NeededItem) selected).getNeededItemsRef().child(itemKey);

        } else {
            itemsRef = FirebaseDatabase.getInstance().getReference("posts/donation-items/").child(itemKey);
        }


        if (selected != null) {
            myItemName.setText(selected.getName());
            myItemDesc.setText(selected.getDescription());
            myItemCat.setText(selected.getCategory().toString());
            myItemStat.setText(selected.getStatusString());

            DateFormat df = new java.text.SimpleDateFormat("yyyy MMMM dd hh:mm aaa");
            myItemDate.setText(df.format(selected.getDate()));
            itemStatSwitch.setChecked(selected.getIsOpen());

            if (selected instanceof LostItem) {
                myLostItemReward.setText("$" + ((LostItem) selected).getReward());
                myItemType.setText(((LostItem) selected).getItemType().toString());

                lostItemRewardEdit.setText(String.valueOf(((LostItem) selected).getReward()));
            } else if (selected instanceof FoundItem) {
                rewardLinLayout.setVisibility(View.INVISIBLE);
                myItemType.setText(((FoundItem) selected).getItemType().toString());
            } else {
                rewardLinLayout.setVisibility(View.INVISIBLE);
                myItemType.setText(selected.getType().toString());
            }

            itemNameEdit.setText(selected.getName());
            itemDescEdit.setText(selected.getDescription());
            itemCatSpinner.setSelection(selected.getCategory().ordinal());

        }

        editItemToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final String newItemName = itemNameEdit.getText().toString();
                final String newItemDesc = itemDescEdit.getText().toString();
                final String newItemCat = itemCatSpinner.getSelectedItem().toString();
                final boolean newStat = itemStatSwitch.isChecked();
                final String newItemReward = lostItemRewardEdit.getText()
                        .toString(); //Integer.parseInt(lostItemRewardEdit.getText().toString());

                if (isChecked) { //edit mode is on
                    nameViewSwitcher.showPrevious();
                    descViewSwitcher.showPrevious();
                    catViewSwitcher.showPrevious();
                    itemStatSwitch.setVisibility(View.VISIBLE);
                    itemCatSpinner.setSelection(ItemCategory.valueOf(myItemCat.getText().toString()).ordinal());
                    deleteBtn.setVisibility(View.VISIBLE);
                    uploadImgBtn.setVisibility(View.VISIBLE);
                    takePicBtn.setVisibility(View.VISIBLE);

                    if (selected instanceof LostItem) {
                        rewardViewSwitcher.showPrevious();
                    }


                } else {
                    nameViewSwitcher.showNext();
                    descViewSwitcher.showNext();
                    catViewSwitcher.showNext();
                    itemStatSwitch.setVisibility(View.INVISIBLE);
                    deleteBtn.setVisibility(View.GONE);
                    uploadImgBtn.setVisibility(View.GONE);
                    takePicBtn.setVisibility(View.GONE);

                    if (selected instanceof LostItem) {
                        rewardViewSwitcher.showNext();
                    }

                    if (!myItemName.equals(newItemName)) {
                        resetFields(newItemName, "name");
                        myItemName.setText(newItemName);

                    }

                    if (!myItemDesc.equals(newItemDesc)) {
                        resetFields(newItemDesc, "description");
                        myItemDesc.setText(newItemDesc);
                    }

                    if (!myItemCat.equals(newItemCat)) {
                        resetFields(newItemCat, "category");
                        myItemCat.setText(newItemCat);
                    }

                    if (!myLostItemReward.equals(newItemReward)) {
                        resetFields(newItemReward, "reward");
                        myLostItemReward.setText("$" + newItemReward);
                    }

                    if (newStat != selected.getIsOpen()) {
                        resetFields(String.valueOf(newStat), "isOpen");
                        selected.setIsOpen(newStat);
                        myItemStat.setText(selected.getStatusString());
                        itemStatSwitch.setChecked(selected.getIsOpen());

                    }
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

        if (v == deleteBtn) {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MyEditableItemActivity.this);
            dialogBuilder.setMessage("Are you sure you want to permanently remove your item?");
            dialogBuilder.setCancelable(true);

            dialogBuilder.setPositiveButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            dialogBuilder.setNegativeButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            delete();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    final Intent mainIntent = new Intent(getApplicationContext(), MyListActivity.class);
                                    mainIntent.putExtra("deleted", "item deleted");
                                    progressDialog.dismiss();
                                    MyEditableItemActivity.this.startActivity(mainIntent);
                                    MyEditableItemActivity.this.finish();
                                }
                            }, 1500);
                        }
                    });

            AlertDialog alert11 = dialogBuilder.create();
            alert11.show();

        }

        if (v == uploadImgBtn) {
            Intent uploadIntent = new Intent(Intent.ACTION_GET_CONTENT);
            uploadIntent.setType("image/*");
            startActivityForResult(uploadIntent, GALLERY_INTENT);
        }

        if (v == takePicBtn) {
            Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicIntent, CAMERA_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT  && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                //Setting image to ImageView
                final int THUMBNAIL_SIZE = 256;

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                imgRef.child(itemKey).setValue(imageString);

                //decode base64 string to image
                imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                uploadedImg.setImageBitmap(decodedImage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST  && resultCode == RESULT_OK) {
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                imgRef.child(itemKey).setValue(imageString);

                //decode base64 string to image
                imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                uploadedImg.setImageBitmap(decodedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }

    /**
     * Deletes the item from the database.
     */
    private void delete() {
        progressDialog.setMessage("Please wait until your item is removed...");
        progressDialog.show();

        try {
            itemsRef.removeValue();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.w(TAG, "One of the fields is null, so nothing can be retrieved from the DB.");
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MyEditableItemActivity.this);
            dialogBuilder.setMessage("Sorry, the selected item could not be deleted. Please try refreshing.");
            dialogBuilder.setCancelable(true);

            dialogBuilder.setNegativeButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            startActivity(new Intent(getApplicationContext(), MyListActivity.class));
                            finish();
                        }
                    });

            AlertDialog alert11 = dialogBuilder.create();
            alert11.show();
        }
    }

    /**
     * Resets the item values
     *
     * @param changeTo  what to change it to
     * @param fieldName what to change
     */
    private void resetFields(final String changeTo, final String fieldName) {
        final DatabaseReference fieldRef = itemsRef.child(fieldName);

        if (fieldName.equals("isOpen")) {
            fieldRef.setValue(Boolean.parseBoolean(changeTo));
            return;
        }

        if (selected instanceof LostItem && fieldName.equals("reward")) {
            fieldRef.setValue(Integer.parseInt(changeTo));
            return;
        }

        if (itemKey != null && !fieldName.equals("isOpen")) {
            fieldRef.setValue(changeTo);

        }

    }

    /**
     * Loads the image of the item from Firebase.
     *
     * @param itemKey item push key in the database
     */
    private void loadImage(String itemKey) {
        imgRef.child(itemKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Bitmap bm = convertStringtoBitmap(dataSnapshot.getValue().toString());
                    uploadedImg.setImageBitmap(bm);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.i(TAG, "No Image to upload");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    /**
     * Given an encoded string of an image, it converts it to a Bitmap object.
     *
     * @param encodedStr encoded string of an image
     * @return Bitmap form of the image
     */
    private Bitmap convertStringtoBitmap(String encodedStr) {
        try {
            byte[] encodeByte = Base64.decode(encodedStr, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


}
