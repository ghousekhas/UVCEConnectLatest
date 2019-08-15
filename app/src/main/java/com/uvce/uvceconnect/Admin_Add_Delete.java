package com.uvce.uvceconnect;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class Admin_Add_Delete extends AppCompatActivity {

    List<Homepage_ListItem> list = new ArrayList<>();
    HomePage_Adapter adapter;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Main_Page");
    AVLoadingIndicatorView load_animation;
    RecyclerView recyclerView;
    ValueEventListener eventlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__add__delete);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Loading Animation
        load_animation = findViewById(R.id.loading_animation_admin);
        load_animation.smoothToShow();

        //Following Lines used to populate the recycler list
        recyclerView = findViewById(R.id.admin_recycler_view);
        adapter = new HomePage_Adapter(list, this, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        prepareHomePageData();


    }

    //Function to populate the list
    void prepareHomePageData() {
        databaseReference.addValueEventListener(eventlistener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    list.clear();
                    for (DataSnapshot childsnapshot : dataSnapshot.getChildren()) {
                        Homepage_ListItem item = new Homepage_ListItem(childsnapshot.child("Logo").getValue().toString(), childsnapshot.child("Name").getValue().toString(), childsnapshot.child("Content").getValue().toString(), childsnapshot.child("Image").getValue().toString(), childsnapshot.child("Time_Signature").getValue().toString(), Integer.parseInt(childsnapshot.child("Type").getValue().toString()), getIntent().getStringExtra("Name"), Integer.parseInt(childsnapshot.getKey().toString()),childsnapshot.child("link").child("downloadurl").getValue().toString(),childsnapshot.child("link").child("filename").getValue().toString());
                        list.add(item);
                    }
                    adapter.notifyDataSetChanged();
                    load_animation.smoothToHide();
                } catch (Exception e) {
                    //Toast.makeText(Admin_Add_Delete.this, "Error in fetching details", Toast.LENGTH_SHORT).show();
                    list.clear();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        databaseReference.removeEventListener(eventlistener);
    }
}
