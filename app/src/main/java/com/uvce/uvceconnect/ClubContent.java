package com.uvce.uvceconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClubContent extends AppCompatActivity {
    String clubname,club="";

    TextView card1_content,card2_content,card3_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        clubname=getIntent().getStringExtra("clubname");
        getSupportActionBar().setTitle(clubname);



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
