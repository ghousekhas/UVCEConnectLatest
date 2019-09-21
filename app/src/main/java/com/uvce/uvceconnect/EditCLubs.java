package com.uvce.uvceconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditCLubs extends AppCompatActivity {

    DatabaseReference databaseReference;
    Spinner spinner;
    EditText editText1,editText2,editText3;
    List<String> clubs=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    Button button;
    String selectedClub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getSharedPreferences("settings",MODE_PRIVATE).getBoolean("dark",true)?R.style.AppTheme:R.style.LightTheme);
        setContentView(R.layout.activity_edit_clubs);

        editText1=findViewById(R.id.text1);
        editText2=findViewById(R.id.text2);
        editText3=findViewById(R.id.text3);
        spinner=findViewById(R.id.spinner);
        button=findViewById(R.id.button);


        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,clubs);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        FirebaseDatabase.getInstance().getReference("Club_Content").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clubs.clear();
                for(DataSnapshot childsnapshot: dataSnapshot.getChildren()) {
                    clubs.add(childsnapshot.getKey());
                    Log.e("club", childsnapshot.getKey());
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedClub=clubs.get(position);
                loadpage();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePage();
            }
        });




    }
    public void loadpage(){
        FirebaseDatabase.getInstance().getReference("Club_Content").child(selectedClub).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                editText1.setText(dataSnapshot.child("card1").getValue().toString());
                editText2.setText(dataSnapshot.child("card2").getValue().toString());
                editText3.setText(dataSnapshot.child("card3").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void savePage(){
        databaseReference=FirebaseDatabase.getInstance().getReference("Club_Content").child(selectedClub);

        if(!(editText1.getText().toString().isEmpty()||editText2.getText().toString().isEmpty()||editText3.getText().toString().isEmpty())) {
            databaseReference.child("card1").setValue(editText1.getText().toString());
            databaseReference.child("card2").setValue(editText2.getText().toString());
            databaseReference.child("card3").setValue(editText3.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(EditCLubs.this,"Content updated successfully!",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditCLubs.this,"Please try again with an active internet connection",Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            Toast.makeText(this,"Try again with valid inputs!",Toast.LENGTH_LONG).show();
        }
    }
}
