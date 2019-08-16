package com.uvce.uvceconnect;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.multidex.MultiDex;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    RecyclerView recyclerView;
    View headerview;
    ImageView admin_shortcut;
    List<Homepage_ListItem> list = new ArrayList<>();
    HomePage_Adapter adapter;

    DatabaseReference databaseReference;
    AVLoadingIndicatorView load_animation;
    Activity activity;
    private final String PREFERENECE = "UVCE-prefereceFile";
    private SharedPreferences preference;
    final String askAgain = "Privacy";
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_main);
        //Loading Animation
        load_animation = findViewById(R.id.loading_animation);
        load_animation.smoothToShow();
        //Setting the custom toolbar as Action bar.
        //Any toolbar related code should be done after these lines
        mToolbar=findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);

        //Notifications
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Main_Page");

        //Following lines are used to link navigation drawer
        mDrawerLayout=findViewById(R.id.navigation_drawer_main);
        mNavigationView=findViewById(R.id.navigation_view);

        mNavigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.open_navigation_drawer,R.string.close_navigation_drawer);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Permissions
        if (Build.VERSION.SDK_INT >= 23) { // if android version >= 6.0
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // if permission was not granted initially, ask the user again.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }

        //Startup Registration (optional skippable)
        preference=getApplicationContext().getSharedPreferences("lol", Context.MODE_PRIVATE);
        if(preference.getString("registeredBefore","false").equals("false")){
            Intent intent=new Intent(this,Register.class);
            startActivity(intent);
            finish();
        }

        //Following Lines used to enable admin options
        headerview = mNavigationView.getHeaderView(0);
        admin_shortcut = headerview.findViewById(R.id.Admin_Shortcut);
        activity = this;
        admin_shortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                if(count == 5)
                {
                    count = 0;
                    Intent intent = new Intent(activity, Admin_Page.class);
                    startActivity(intent);
                }
                else if(count>5)
                    count = count%5;
            }
        });

        //Following Lines used to populate the recycler list
        recyclerView = findViewById(R.id.homepage_recyclerview);
        adapter = new HomePage_Adapter(list, this, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        prepareHomePageData();

        //Displaying the Privacy Policy
        preference = getSharedPreferences(PREFERENECE, MODE_PRIVATE);
        if (!(preference.contains(askAgain) && preference.getBoolean(askAgain, false))) {
            showPrivacyPolicy();
        }

        //Following Lines for the Floating Action button



    }

    //Function to populate the list
    void prepareHomePageData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    list.clear();
                    for (DataSnapshot childsnapshot : dataSnapshot.getChildren()) {
                            Homepage_ListItem item = new Homepage_ListItem(childsnapshot.child("Logo").getValue().toString(), childsnapshot.child("Name").getValue().toString(), childsnapshot.child("Content").getValue().toString(), childsnapshot.child("Image").getValue().toString(), childsnapshot.child("Time_Signature").getValue().toString(), Integer.parseInt(childsnapshot.child("Type").getValue().toString()), "", Integer.parseInt(childsnapshot.getKey().toString()),childsnapshot.child("link").child("downloadurl").getValue().toString(),childsnapshot.child("link").child("filename").getValue().toString());
                            list.add(item);
                    }
                    adapter.notifyDataSetChanged();
                    load_animation.smoothToHide();
                } catch (Exception e) {
                    //Toast.makeText(MainActivity.this, "Error in fetching details", Toast.LENGTH_SHORT).show();
                    list.clear();
                    databaseReference.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Function to Display Privacy Policy
    private void showPrivacyPolicy() {
        AlertDialog.Builder disclaimerDialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View view = inflater.inflate(R.layout.privacy_policy, null);
        final CheckBox dontShowAgain = (CheckBox) view.findViewById(R.id.checkBoxid_privacy);
        TextView privacy_text = view.findViewById(R.id.privacy_text);
        privacy_text.setMovementMethod(new ScrollingMovementMethod());

        disclaimerDialog.setView(view)
                .setTitle("Privacy Policy")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SharedPreferences.Editor editor = preference.edit();
                        editor.putBoolean(askAgain, dontShowAgain.isChecked());
                        editor.apply();
                    }
                })
                .setCancelable(false)
                .show();
    }


    //This function handles Navigation Drawer onClick Listener
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id;
        Intent intent;
        id=item.getItemId();
        switch (id)
        {

            case R.id.menu_qp_and_syllabus:
                count = 0;
                intent = new Intent(this,Academic.class);
                startActivity(intent);
                // Toast.makeText(this,"Question Papers and Syllabus",Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_about_uvce:
                count = 0;
                intent = new Intent(this,about_uvce.class);
                startActivity(intent);
               // Toast.makeText(this,"About UVCE",Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_adhamya:
                count = 0;
                intent = new Intent(this,Adhamya.class);
                //Toast.makeText(this,"Adhamya",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;

            case R.id.menu_ieee:
                count = 0;
                intent = new Intent(this,Ieee.class);
                //Toast.makeText(this,"IEEE",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;

            case R.id.menu_associations:
                count = 0;
                intent = new Intent(this,associations.class);
                startActivity(intent);
                //Toast.makeText(this,"Associations",Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_fest:
                count = 0;
                intent = new Intent(this,fests.class);
                startActivity(intent);
                //Toast.makeText(this,"Fests",Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_placement_office:
                count = 0;
                intent = new Intent(this,placements.class);
                startActivity(intent);
                //Toast.makeText(this,"Placement Office",Toast.LENGTH_SHORT).show();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //This function closes navigation drawer if back is pressed when it is open
    @Override
    public void onBackPressed() {

        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        prepareHomePageData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.club_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.info) {
            Intent intent=new Intent(this,ClubContent.class);
            intent.putExtra("clubname","Contact Us");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}
