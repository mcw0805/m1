package com.example.mcw0805.wheres_my_stuff.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mcw0805.wheres_my_stuff.Model.FoundItem;
import com.example.mcw0805.wheres_my_stuff.Model.Item;
import com.example.mcw0805.wheres_my_stuff.Model.ItemType;
import com.example.mcw0805.wheres_my_stuff.Model.LostItem;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.mcw0805.wheres_my_stuff.R;

/**
 * The main dashboard of the application when a user logs in.
 * Displays the map and the panel to navigate to other pages.
 */
public class Dashboard extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean isAuthListenerSet = false;
    private ChildEventListener lostListen;
    private ChildEventListener foundListen;
//    //View stuff
//    Scene bottom;
//    Scene top;
//    View mSceneRootTop;
//    View mSceneRootBottom;
//    ViewDragHelper mDragHelper;
//    //animation stuff
//    //Fade mFade;
//    //LayoutInflater inflater;

    private final String TAG = "Dashboard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_master);

        //embeds the fragment onto this activity
        FragmentManager mgr = getSupportFragmentManager();
        FragmentTransaction transaction = mgr.beginTransaction();
        DashBottomFragment frag = new DashBottomFragment();
        transaction.add(R.id.dash_bottom, frag);
        transaction.commit();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAuth = FirebaseAuth.getInstance();


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


        // Create the scene root (VIEWS) for the scenes in this app
        //mSceneRootTop = findViewById(R.id.scene_root_top);
        //mSceneRootBottom = findViewById(R.id.scene_root_bottom);

        // Create the scenes
        //top = Scene.getSceneForLayout((ViewGroup) mSceneRootTop, R.layout.activity_dashboard_top, this);
        //bottom = Scene.getSceneForLayout((ViewGroup)mSceneRootBottom, R.layout.activity_dashboard_bottom, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vertical_ellipse_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            Dashboard.this.startActivity(intent);
        } else if (id == R.id.action_log_out) {
            signOut();
            Toast.makeText(getApplicationContext(),
                    "Successfully signed out.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Dashboard.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private SlidingUpPanelLayout.PanelSlideListener onSlideListener() {
        return new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
            }

            @Override
            public void onPanelCollapsed(View view) {
            }

            @Override
            public void onPanelExpanded(View view) {
            }

            @Override
            public void onPanelAnchored(View view) {
            }

            @Override
            public void onPanelHidden(View view) {
            }
        };
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
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
            isAuthListenerSet = false;
        }
    }

    /*
    * navigates the user to the next page depending on which item they select
     */


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * Method that signs a user out.
     */
    private void signOut() {
        mAuth.signOut();
        Log.d(TAG, "signed out");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Pull data from database
        final DatabaseReference foundItems = FoundItem.getFoundItemsRef();

        foundItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                placeItemOnMap(dataSnapshot, ItemType.FOUND);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final DatabaseReference lostItems = LostItem.getLostItemsRef();
        lostItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                placeItemOnMap(dataSnapshot, ItemType.LOST);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //adds all found items
//        foundListen = foundItems.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                FoundItem f = null;
//                try {
//                    f = FoundItem.buildFoundItemObject(dataSnapshot);
//                } catch (NullPointerException e) {
//                    Log.d(TAG, "NullPointerException is caught.");
//                    e.printStackTrace();
//                }
//                LatLng lItem = new LatLng(f.getLatitude(), f.getLongitude());
//                String display = "Found Item:";
//                mMap.addMarker(new MarkerOptions().position(lItem).title(f.description())
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//                Log.d(TAG, "added found item");
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//        //adds all lost items
//        lostListen = lostItems.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                LostItem l = null;
//                try {
//                    l = LostItem.buildLostItemObject(dataSnapshot);
//                } catch (NullPointerException e) {
//                    Log.d(TAG, "NullPointerException is caught.");
//                    e.printStackTrace();
//                }
//                LatLng lItem = new LatLng(l.getLatitude(), l.getLongitude());
//                mMap.addMarker(new MarkerOptions().position(lItem).title(l.description()));
//                Log.d(TAG, "added lost item");
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        //formats the info window when clicking a pin
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Context mContext = getApplicationContext();

                LinearLayout info = new LinearLayout(mContext);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(mContext);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(mContext);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        LatLng gt = new LatLng(33.7773728, -84.3981109);
        mMap.addMarker(new MarkerOptions().position(gt).title("Marker at Georgia Tech"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gt));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gt, 17));
    }

    /**
     * Reads the database and places the markers on the proper location on the map.
     *
     * @param snapshot of the items in the database
     * @param type type of the item
     */
    private void placeItemOnMap(DataSnapshot snapshot, ItemType type) {
        for (DataSnapshot ds : snapshot.getChildren()) {
            Item item = Item.ItemFactory.makeItem(type);
            item.setName(ds.getValue(Item.class).getName());
            item.setDescription(ds.getValue(Item.class).getDescription());
            item.setIsOpen(ds.getValue(Item.class).getIsOpen());
            item.setCategory(ds.getValue(Item.class).getCategory());
            item.setLatitude(ds.getValue(Item.class).getLatitude());
            item.setLongitude(ds.getValue(Item.class).getLongitude());
            item.setDate(ds.getValue(Item.class).getDate());
            item.setUid(ds.getValue(Item.class).getUid());

            LatLng latLng = new LatLng(item.getLatitude(), item.getLongitude());
            mMap.addMarker(getMarkerOptions(latLng, item, type));
            Log.d(TAG, "added item");
        }

    }

    /**
     * Creates a marker at a particular location and sets the colors based on the item type.
     *
     * @param latLng latitude/longitude
     * @param item item that is being placed on the map
     * @param type type of the item
     * @return
     */
    private MarkerOptions getMarkerOptions(LatLng latLng, Item item, ItemType type) {
        switch (type) {
            case LOST:
                return new MarkerOptions().position(latLng).title(((LostItem) item).description());
            case FOUND:
                return new MarkerOptions().position(latLng).title(((FoundItem) item).description())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            case NEED:
                return new MarkerOptions().position(latLng).title(((FoundItem) item).description())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            default:
                return new MarkerOptions().position(new LatLng(0, 0)).title("UNKNOWN")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

        }


    }
}
