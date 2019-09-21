package com.uvce.uvceconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ClubContent extends AppCompatActivity {
    String clubname,club="";

    TextView card1_content,card2_content,card3_content;
    RecyclerView recyclerView;
    Images_Adapter images_adapter;
    List<StorageReference> imagelist=new ArrayList<>();
    DatabaseReference imagereference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getSharedPreferences("settings",MODE_PRIVATE).getBoolean("dark",true)?R.style.AppTheme:R.style.LightTheme);
        setContentView(R.layout.activity_club_content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        clubname=getIntent().getStringExtra("clubname");
        getSupportActionBar().setTitle(clubname);

        recyclerView=findViewById(R.id.recyclerview);
        images_adapter=new Images_Adapter(imagelist,this,this);
        recyclerView.setOnFlingListener(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(images_adapter);
        SnapHelper snapHelper=new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new CirclePagerIndicatorDecoration());

        imagereference=FirebaseDatabase.getInstance().getReference("Club_Content").child(clubname).child("images");
        imagereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imagelist.clear();
                for(DataSnapshot childsnapshot:dataSnapshot.getChildren())
                    imagelist.add(FirebaseStorage.getInstance().getReference("clubs").child(childsnapshot.getValue().toString()));

                recyclerView.setVisibility(View.VISIBLE);
                images_adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Toast.makeText(getApplicationContext(),clubname,Toast.LENGTH_LONG).show();

        card1_content =(TextView)findViewById(R.id.card1_content);
        card2_content =(TextView)findViewById(R.id.card2_content);
        card3_content =(TextView)findViewById(R.id.card3_content);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Club_Content").child(clubname);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    String temp1 = dataSnapshot.child("card1").getValue().toString().replace("bb", "\n");
                    String temp2 = dataSnapshot.child("card2").getValue().toString().replace("bb", "\n");
                    String temp3 = dataSnapshot.child("card3").getValue().toString().replace("bb", "\n");

                    card1_content.setText(temp1);
                    card2_content.setText(temp2);
                    card3_content.setText(temp3);

                }catch (Exception e) {
                    Toast.makeText(ClubContent.this, "There seems to be a connectivity issue. Please try again.", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
