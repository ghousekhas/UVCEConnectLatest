package com.uvce.uvceconnect;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// mahith
public class about_uvce extends AppCompatActivity {
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Club_Content/About UVCE");
    TextView card1_content,card2_content,card3_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_uvce);

         card1_content =(TextView)findViewById(R.id.card1_content);
         card2_content =(TextView)findViewById(R.id.card2_content);
         card3_content =(TextView)findViewById(R.id.card3_content);

        //load all contents
        preparePageData();

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //customfont


        //customfont for the textviews in each cardview


    }
    void preparePageData() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try{

                //to give a next line every time a bb appears in the sentence
                //assuming no word starts with bb xD
                String temp1=dataSnapshot.child("card1").getValue().toString().replace("bb", "\n");
                String temp2=dataSnapshot.child("card2").getValue().toString().replace("bb", "\n");
                String temp3= dataSnapshot.child("card3").getValue().toString().replace("bb", "\n");

                card1_content.setText(temp1);
                card2_content.setText(temp2);
                card3_content.setText(temp3);

                } catch(Exception e) {
                    Toast.makeText(about_uvce.this, "There seems to be a connectivity issue. Please try again.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}