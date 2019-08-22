package com.uvce.uvceconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
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
    Button add,editclubs, addfile;
    int type;
    private final String PREFERENECE = "UVCE-prefereceFile";
    private SharedPreferences preference;
    String Admin_Name = "Admin_Name";
    private String Edit_FileName;
    private String Edit_FileLink;
    private String Edit_ImageLink;

    //a Uri object to store file path
    private Uri filePath;
    private Uri DocumentfilePath;

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
    private static final int FILE_SELECT_CODE = 0;
    TextView file_text;
    String organ_spin_select = "-100";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        storageReference = FirebaseStorage.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__add__content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainref = FirebaseDatabase.getInstance().getReference().child("Main_Page");
        preference = getSharedPreferences(PREFERENECE, MODE_PRIVATE);

        //name = getIntent().getStringExtra("Name");

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
        //link=(EditText) findViewById(R.id.link);
        file_text=(TextView) findViewById(R.id.file_text);
        //filename=(EditText) findViewById(R.id.filename);
        organization = (Spinner) findViewById(R.id.organisation_news_add);
        picture = (Button) findViewById(R.id.choose_pic_news);
        addfile = (Button) findViewById(R.id.add_file_button);
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
                    organization.setSelection(getSpinnerPosition(organ_spin_select));
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

        addfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentfile = new Intent();
                intentfile.setType("*/*");
                intentfile.setAction(Intent.ACTION_GET_CONTENT);
                try {
                    startActivityForResult(Intent.createChooser(intentfile, "Select File"), FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(getApplicationContext(), "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }
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

                        name = "Default_User";
                        ID = organization.getItemAtPosition(organization.getSelectedItemPosition()).toString();
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Content").setValue(details.getText().toString());
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Logo").setValue(organization_image);
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Name").setValue(organisation_name);
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Image").setValue("image/" + filePath.getLastPathSegment() + "_" + ID);
                        if(preference.contains(Admin_Name))
                            name = preference.getString(Admin_Name, "Default_User");
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Added_By").setValue(name);
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Type").setValue(type);

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
                        if(getIntent().getBooleanExtra("Edit", false) && DocumentfilePath==null) {
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("filename").setValue(Edit_FileName);
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("downloadurl").setValue(Edit_FileLink);
                        }else if(DocumentfilePath!=null) {
                            StorageReference fileref = storageReference.child("file/" + DocumentfilePath.getLastPathSegment().substring(DocumentfilePath.getLastPathSegment().lastIndexOf("/") + 1) + "_" + ID);
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("filename").setValue(DocumentfilePath.getLastPathSegment().substring(DocumentfilePath.getLastPathSegment().lastIndexOf("/") + 1));
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("downloadurl").setValue("file/" + DocumentfilePath.getLastPathSegment().substring(DocumentfilePath.getLastPathSegment().lastIndexOf("/") + 1) + "_" + ID);
                            fileref.putFile(DocumentfilePath)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //if the upload is successfull
                                            //hiding the progress dialog
                                            progressDialog.dismiss();

                                            //and displaying a success toast
                                            Toast.makeText(getApplicationContext(), "File Successfully Uploaded", Toast.LENGTH_LONG).show();


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            //if the upload is not successfull
                                            //hiding the progress dialog
                                            progressDialog.dismiss();

                                            //and displaying error message
                                            Toast.makeText(getApplicationContext(), "File Upload failed", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                            //calculating progress percentage
                                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                            //displaying percentage in progress dialog
                                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                                        }
                                    });
                        } else {
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("filename").setValue("-1");
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("downloadurl").setValue("-1");
                            Log.d("this","second");
                        }

                        riversRef.putFile(filePath)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //if the upload is successfull
                                        //hiding the progress dialog
                                        progressDialog.dismiss();

                                        //and displaying a success toast
                                        Toast.makeText(getApplicationContext(), "Image Successfully Uploaded", Toast.LENGTH_LONG).show();


                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        //if the upload is not successfull
                                        //hiding the progress dialog
                                        progressDialog.dismiss();

                                        //and displaying error message
                                        Toast.makeText(getApplicationContext(), "Image Upload failed", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        //calculating progress percentage
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                        //displaying percentage in progress dialog
                                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
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

                        name = "Default_User";
                        ID = organization.getItemAtPosition(organization.getSelectedItemPosition()).toString();
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Content").setValue(details.getText().toString());
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Logo").setValue(organization_image);
                        mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Name").setValue(organisation_name);

                        Log.d("this","third");
                        if(getIntent().getBooleanExtra("Edit", false))
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Image").setValue(Edit_ImageLink);
                        else
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("Image").setValue("");
                        if(preference.contains(Admin_Name))
                            name = preference.getString(Admin_Name, "Default_User");
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
                        if(getIntent().getBooleanExtra("Edit", false) && DocumentfilePath==null) {
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("filename").setValue(Edit_FileName);
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("downloadurl").setValue(Edit_FileLink);
                        }else if(DocumentfilePath!=null) {
                            StorageReference fileref = storageReference.child("file/" + DocumentfilePath.getLastPathSegment().substring(DocumentfilePath.getLastPathSegment().lastIndexOf("/") + 1) + "_" + ID);
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("filename").setValue(DocumentfilePath.getLastPathSegment().substring(DocumentfilePath.getLastPathSegment().lastIndexOf("/") + 1));
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("downloadurl").setValue("file/" + DocumentfilePath.getLastPathSegment().substring(DocumentfilePath.getLastPathSegment().lastIndexOf("/") + 1) + "_" + ID);
                            fileref.putFile(DocumentfilePath)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //if the upload is successfull
                                            //hiding the progress dialog
                                            progressDialog.dismiss();

                                            //and displaying a success toast
                                            Toast.makeText(getApplicationContext(), "File Successfully Uploaded", Toast.LENGTH_LONG).show();


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            //if the upload is not successfull
                                            //hiding the progress dialog
                                            progressDialog.dismiss();

                                            //and displaying error message
                                            Toast.makeText(getApplicationContext(), "File Upload failed", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                            //calculating progress percentage
                                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                            //displaying percentage in progress dialog
                                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                                        }
                                    });
                        } else {
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("filename").setValue("-1");
                            mainref.child(String.valueOf(Integer.parseInt(newpos) - 1)).child("link").child("downloadurl").setValue("-1");
                            Log.d("this","second");
                        }
                        Toast.makeText(getApplicationContext(), "Content Successfully Updated", Toast.LENGTH_LONG).show();

                    }

                    /*if(getIntent().getBooleanExtra("Edit", false))
                        finish();*/
                } else
                    Toast.makeText(getApplicationContext(), "Please Enter all the fields", Toast.LENGTH_SHORT).show();
            }
        });

        //Editing Feature
        if(getIntent().getBooleanExtra("Edit", false))
        {
            details.setText(getIntent().getStringExtra("Content"));
            organ_spin_select = getIntent().getStringExtra("Organ_Name");
            priority.setSelection(getIntent().getIntExtra("Type", 0)==1?0:1);
            file_text.setText("If no image or file is selected, the respective fields will be set to the previous one upon update.");
            Edit_FileName = getIntent().getStringExtra("FileName");
            Edit_FileLink = getIntent().getStringExtra("Link");
            Edit_ImageLink = getIntent().getStringExtra("Image");
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

        if(requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            DocumentfilePath = data.getData();
            try {

                file_text.setText("File Selected: " + DocumentfilePath.getLastPathSegment().toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    int getSpinnerPosition(String edit_organ)
    {
        if(edit_organ.equals("-100"))
            return 0;

        try {
            for (int i = 0; i < organ.size(); i++) {
                if (edit_organ.equals(organ.get(i)))
                    return i;
            }
        } catch(Exception e) {

        }
        return 0;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
