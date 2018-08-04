package com.uvce.uvceconnect;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
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
    }


    //This function handles Navigation Drawer onClick Listener
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id;
        id=item.getItemId();
        switch (id)
        {
            case R.id.menu_qp_and_syllabus:
                Toast.makeText(this,"Question Paper and Syllabus",Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_about_uvce:
                Intent intent = new Intent(this,about_uvce.class);
                startActivity(intent);
                Toast.makeText(this,"About UVCE",Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_activities:
                Toast.makeText(this,"Activities",Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_associations:
                Toast.makeText(this,"Associations",Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_fest:
                Toast.makeText(this,"Fests",Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_placement_office:
                Toast.makeText(this,"Placement Office",Toast.LENGTH_SHORT).show();
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
