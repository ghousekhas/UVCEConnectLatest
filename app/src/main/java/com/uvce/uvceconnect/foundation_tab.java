package com.uvce.uvceconnect;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// mahith
public class foundation_tab extends Fragment {
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Club_Content/Foundation");
    TextView card1_content,card2_content,card3_content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_foundation_tab, container, false);
        Typeface mycustomfont = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/adobe_font.otf");
        //text view contents of foundation layout
         card1_content =(TextView)view.findViewById(R.id.card1_content_foundation);
         card2_content =(TextView)view.findViewById(R.id.card2_content_foundation);
         card3_content =(TextView)view.findViewById(R.id.card3_content_foundation);

        //load all contents
        preparePageData();
        //customfont for the textviews in each cardview
        card1_content.setTypeface(mycustomfont);
        card2_content.setTypeface(mycustomfont);
        card3_content.setTypeface(mycustomfont);
        return view;
    }
    void preparePageData() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //to give a next line every time a bb appears in the sentence
                //assuming no word starts with bb xD
                String temp1=dataSnapshot.child("card1").getValue().toString().replace("bb", "\n");
                String temp2=dataSnapshot.child("card2").getValue().toString().replace("bb", "\n");
                String temp3= dataSnapshot.child("card3").getValue().toString().replace("bb", "\n");

                card1_content.setText(temp1);
                card2_content.setText(temp2);
                card3_content.setText(temp3);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


