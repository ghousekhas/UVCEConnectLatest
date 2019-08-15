package com.uvce.uvceconnect;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class AssosiationsFragment extends Fragment {

    String assosiation;
    DatabaseReference databaseReference;
    TextView cardView1,cardView2,cardView3;


    public AssosiationsFragment() {
        // Required empty public constructor
    }

    public AssosiationsFragment(String assosiation){
        this.assosiation=assosiation;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_assosiations, container, false);

        databaseReference= FirebaseDatabase.getInstance().getReference("Club_Content").child(assosiation);
        cardView1 = v.findViewById(R.id.text1);
        cardView2=v.findViewById(R.id.text2);
        cardView3=v.findViewById(R.id.text3);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try{
                cardView1.setText(dataSnapshot.child("card1").getValue().toString());
                cardView2.setText(dataSnapshot.child("card2").getValue().toString());
                cardView3.setText(dataSnapshot.child("card3").getValue().toString());

                } catch(Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;





    }

}
