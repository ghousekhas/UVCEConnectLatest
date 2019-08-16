package com.uvce.uvceconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Admin_Add_Content extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 234;
    EditText details,filename,link;
    Spinner organization;
    Button picture;
    ImageView pictureshow;
    Button add,editclubs;
    int type;

    //a Uri object to store file path
    private Uri filePath;

    //firebase storage reference
    private StorageReference storageReference;
    private DatabaseReference mainref;
    private String name;
    private Spinner priority;
    public static String newpos = "0";
    private static String newnewpos;
    private String ID;
    List<String> organ=new ArrayList<>();
    private String organization_image;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, hh:mm a");
    Button edit_add;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        storageReference = FirebaseStorage.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__add__content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainref = FirebaseDatabase.getInstance().getReference().child("Main_Page");
        name = getIntent().getStringExtra("Name");

        priority = findViewById(R.id.priority);
        Query query = mainref.orderByKey().limitToFirst(1);



        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        newpos = snapshot.getKey();
                    }

                } catch (Exception e) {
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        editclubs=(Button) findViewById(R.id.editclubs);
        details = (EditText) findViewById(R.id.news_details_add);
        link=(EditText) findViewById(R.id.link);
        filename=(EditText) findViewById(R.id.filename);
        organization = (Spinner) findViewById(R.id.organisation_news_add);
        picture = (Button) findViewById(R.id.choose_pic_news);
        pictureshow = (ImageView) findViewById(R.id.news_imageview);
        add = (Button) findViewById(R.id.news_add_button);
        edit_add = findViewById(R.id.edit_delete_button);
        edit_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Admin_Add_Delete.class);
                intent.putExtra("Name",getIntent().getStringExtra("Name"));
                startActivity(intent);
            }
        });

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, organ);
        FirebaseDatabase.getInstance().getReference("Club_Content").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    organ.clear();
                    organ.add("Administration Office");
                    organ.add("Principal's Office");
                    organ.add("UVCE Select");
                    for (DataSnapshot childsnapshot : dataSnapshot.getChildren()) {
                        String temp = childsnapshot.getKey();
                        if (temp.equals("Chethana Club"))
                            temp = "ಚೇತನ Club";
                        organ.add(temp);
                        Log.e("club", childsnapshot.getKey());
                    }
                    arrayAdapter.notifyDataSetChanged();
                } catch (Exception e) {}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        organization.setAdapter(arrayAdapter);

        editclubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Admin_Add_Content.this,EditCLubs.class);
                startActivity(intent);
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentfile = new Intent();
                intentfile.setType("image/*");
                intentfile.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentfile, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!details.getText().toString().trim().equals("")) {


                    String organisation_name = organization.getItemAtPosition
                            (organization.getSelectedItemPosition()).toString();
                    if(!organisation_name.equals("ಚೇತನ Club"))
                        organization_image = "logo/" + organisation_name + ".jpg";
                    else
                        organization_image = "logo/Chethana Club.jpg";

                    type = priority.getSelectedItem().toString().equals("Priority")?1:0;

                    if(getIntent().getBooleanExtra("Edit", false)) {
                        newpos = Integer.toString(getIntent().getIntExtra("Key", Integer.parseInt(newpos)));
                        newpos = Integer.toString(Integer.parseInt(newpos)+1);
                    }

                    if (filePath != null) {
                        //displaying a progress dialog while upload is going on
                        final ProgressDialog progressDialog = new ProgressDialog(Admin_Add_Content.this);
                        progressDialog.setTitle("Updating");
                        progressDialog.setCancelable(false);
                        progressDialog.show();


                        ID = organization.getItemAtPosition(organization.getSelectedItemPosition()).toString();
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Content").setValue(details.getText().toString());
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Logo").setValue(organization_image);
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Name").setValue(organisation_name);
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Image").setValue("image/" + filePath.getLastPathSegment() + "_" + ID);
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Added_By").setValue(name);
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Type").setValue(type);
                        if(filename.getText().toString().trim().isEmpty()||link.getText().toString().trim().isEmpty()) {
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("filename").setValue("-1");
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("downloadurl").setValue("-1");
                            Log.d("this","first");
                        }
                        else{
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("filename").setValue(filename.getText().toString());
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("downloadurl").setValue(link.getText().toString());
                            Log.d("this","second");
                        }
                        Log.d("this","third");
                        Date date = new Date();
                        if(getIntent().getBooleanExtra("Edit", false)) {
                            if(getIntent().getStringExtra("Time").contains("(Edited)"))
                                mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Time_Signature").setValue(getIntent().getStringExtra("Time"));
                            else
                                mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Time_Signature").setValue(getIntent().getStringExtra("Time") + " (Edited)");
                        }
                        else
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Time_Signature").setValue(dateFormat.format(date));
                        StorageReference riversRef = storageReference.child("image/" + filePath.getLastPathSegment() + "_" + ID);
                        riversRef.putFile(filePath)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //if the upload is successfull
                                        //hiding the progress dialog
                                        progressDialog.dismiss();

                                        //and displaying a success toast
                                        Toast.makeText(getApplicationContext(), "Content Successfully Updated", Toast.LENGTH_LONG).show();


                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        //if the upload is not successfull
                                        //hiding the progress dialog
                                        progressDialog.dismiss();

                                        //and displaying error message
                                        Toast.makeText(getApplicationContext(), "Update failed", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        //calculating progress percentage
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                        //displaying percentage in progress dialog
                                        progressDialog.setMessage("Updated " + ((int) progress) + "%...");
                                    }
                                });


                    }
                    //if there is not any file
                    else {
                        //displaying a progress dialog while upload is going on
                        final ProgressDialog progressDialog = new ProgressDialog(Admin_Add_Content.this);
                        progressDialog.setTitle("Updating");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        ID = organization.getItemAtPosition(organization.getSelectedItemPosition()).toString();
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Content").setValue(details.getText().toString());
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Logo").setValue(organization_image);
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Name").setValue(organisation_name);
                        if(filename.getText().toString().trim().isEmpty()||link.getText().toString().trim().isEmpty()) {
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("filename").setValue("-1");
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("downloadurl").setValue("-1");
                            Log.d("this","first");
                        }
                        else{
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("filename").setValue(filename.getText().toString());
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("downloadurl").setValue(link.getText().toString());
                            Log.d("this","second");
                        }
                        Log.d("this","third");
                        if(getIntent().getBooleanExtra("Edit", false))
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Image").setValue(getIntent().getStringExtra("Image"));
                        else
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Image").setValue("");
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Added_By").setValue(name);
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Type").setValue(type);
                        Date date = new Date();
                        if(getIntent().getBooleanExtra("Edit", false)){
                            if(getIntent().getStringExtra("Time").contains("(Edited)"))
                                mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Time_Signature").setValue(getIntent().getStringExtra("Time"));
                            else
                                mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Time_Signature").setValue(getIntent().getStringExtra("Time") + " (Edited)");
                        }
                        else
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Time_Signature").setValue(dateFormat.format(date));
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Content Successfully Updated", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Admin_Add_Content.this,MainActivity.class));
                        finish();
                    }

                    if(getIntent().getBooleanExtra("Edit", false))
                        finish();
                } else
                    Toast.makeText(getApplicationContext(), "Please Enter all the fields", Toast.LENGTH_SHORT).show();
            }
        });

        //Editing Feature
        if(getIntent().getBooleanExtra("Edit", false))
        {
            details.setText(getIntent().getStringExtra("Content"));
            organization.setSelection(getSpinnerPosition(getIntent().getStringExtra("Organ_Name")));
            priority.setSelection(getIntent().getIntExtra("Type", 0)==1?0:1);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                pictureshow.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    int getSpinnerPosition(String organ)
    {
        int id=0;
        switch (organ)
        {
            case "Administration Office":
                id = 0;
                break;

            case "Placement Office":
                id  =1;
                break;

            case "Principal's Office":
                id = 2;
                break;

            case "IEEE":
               id  = 4;
                break;

            case "Thatva":
                id  = 6;
                break;

            case "g2c2":
                id = 7;
                break;

            case "sae":
                id  = 8;
                break;

            case "Vinimaya":
                id  = 9;
                break;

            case "Chakravyuha":
                id  =10;
                break;

            case "ಚೇತನ":
                id  = 11;
                break;

            case "Vision UVCE":
                id  = 3;
                break;

            case "UVCE Foundation":
                id = 12;
                break;

            case "UVCE Select":
                id  = 13;
                break;

            case "E-Cell UVCE":
                id  = 5;
                break;
            default:
                id=0;
                break;
        }
        return id;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
