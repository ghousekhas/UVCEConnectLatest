package com.uvce.uvceconnect;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

//mahith
public class Adhamya extends AppCompatActivity {

    RecyclerView recyclerView;
    IeeeAdapter ieeeAdapter;
    List<String> clubs=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adhamya);

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //just copied code from ieee adapter, it's the same why define another adapter?

        recyclerView=findViewById(R.id.adhamyarecyclerview);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        ieeeAdapter=new IeeeAdapter(clubs,this,this);
        recyclerView.setAdapter(ieeeAdapter);
        populateTheGrid();

    }

    private void populateTheGrid(){
        clubs.add("spaceForLayoutThisIsn'taClubExactlyBut");
        clubs.add("spaceForLayoutThisIsn'taClubExactlyBut");
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("AdhamyaClubs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clubs.clear();
                ieeeAdapter.notifyDataSetChanged();
                for(DataSnapshot childsnapshot: dataSnapshot.getChildren()) {
                    clubs.add(childsnapshot.getKey());
                    ieeeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
           intent.putExtra("clubname","Adhamya");
           startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
