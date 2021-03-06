package com.uvce.uvceconnect;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_Page extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin_Access");
    EditText username;
    EditText password;
    Button sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Admin Login
        username = findViewById(R.id.input_username);
        password = findViewById(R.id.input_password);
        sign_in = findViewById(R.id.sign_in_button);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = databaseReference.child(username.getText().toString());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if(dataSnapshot.exists())
                            {
                                if(password.getText().toString().equals(dataSnapshot.child("Password").getValue().toString()))
                                {
                                    Intent intent = new Intent(getApplicationContext(), Admin_Add_Content.class);
                                    intent.putExtra("Name", dataSnapshot.child("Name").getValue().toString());
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                    Toast.makeText(Admin_Page.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(Admin_Page.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();

                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin_Access");
                        } catch (Exception e) {
                            Toast.makeText(Admin_Page.this, "Error in Signing In", Toast.LENGTH_SHORT).show();
                        }

                        databaseReference.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

}
