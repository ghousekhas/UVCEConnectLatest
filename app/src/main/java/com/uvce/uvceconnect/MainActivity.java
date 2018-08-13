package com.uvce.uvceconnect;

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
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    RecyclerView recyclerView;
    List<Hompage_ListItem> list = new ArrayList<>();
    HomePage_Adapter adapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_main);

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

        //Following Lines used to populate the recycler list
        recyclerView = findViewById(R.id.homepage_recyclerview);
        adapter = new HomePage_Adapter(list, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        prepareHomePageData();
    }

    //Function to populate the list (Dummy for now)
    void prepareHomePageData() {
        for(int i=0; i<20;i++) {
            Hompage_ListItem item = new Hompage_ListItem("", Integer.toString(i), Integer.toString(i+1), "", Integer.toString(i+2), i%2);
            list.add(item);
            adapter.notifyDataSetChanged();
        }
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
                Toast.makeText(this,"Question Paper and Syllabus",Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_about_uvce:
                intent = new Intent(this,about_uvce.class);
                startActivity(intent);
               // Toast.makeText(this,"About UVCE",Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_activities:
                //intent = new Intent(this,activities.class);
                Toast.makeText(this,"Activities",Toast.LENGTH_SHORT).show();
                //startActivity(intent);
                break;

            case R.id.menu_associations:
                intent = new Intent(this,associations.class);
                startActivity(intent);
                //Toast.makeText(this,"Associations",Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_fest:
                intent = new Intent(this,fests.class);
                startActivity(intent);
                //Toast.makeText(this,"Fests",Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_placement_office:
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
}
