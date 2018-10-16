package com.uvce.uvceconnect;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    RecyclerView recyclerView;
    View headerview;
    TextView admin_shortcut;
    List<Hompage_ListItem> list = new ArrayList<>();
    HomePage_Adapter adapter;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Main_Page");
    AVLoadingIndicatorView load_animation;
    Activity activity;
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

        //Following lines are used to link navigation drawer
        mDrawerLayout=findViewById(R.id.navigation_drawer_main);
        mNavigationView=findViewById(R.id.navigation_view);

        mNavigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.open_navigation_drawer,R.string.close_navigation_drawer);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

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
    }

    //Function to populate the list (Dummy for now)
    void prepareHomePageData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot childsnapshot : dataSnapshot.getChildren()) {
                            Hompage_ListItem item = new Hompage_ListItem(childsnapshot.child("Logo").getValue().toString(), childsnapshot.child("Name").getValue().toString(), childsnapshot.child("Content").getValue().toString(), childsnapshot.child("Image").getValue().toString(), childsnapshot.child("Time_Signature").getValue().toString(), Integer.parseInt(childsnapshot.child("Type").getValue().toString()));
                            list.add(item);
                    }
                    adapter.notifyDataSetChanged();
                    load_animation.smoothToHide();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error in fetching details", Toast.LENGTH_SHORT).show();
                    list.clear();
                    databaseReference.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

            case R.id.menu_activities:
                count = 0;
                //intent = new Intent(this,activities.class);
                Toast.makeText(this,"Activities",Toast.LENGTH_SHORT).show();
                //startActivity(intent);
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
}
