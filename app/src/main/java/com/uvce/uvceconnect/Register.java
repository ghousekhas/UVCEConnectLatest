package com.uvce.uvceconnect;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Register extends AppCompatActivity
{

    final FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference ref = firebaseDatabase.getReference("registered_users");
    Button regbtn;
    Spinner semspin;
    EditText regno1,email1,phone1,name1,year1;
    TextView skipRegistration;
    String regno,email,phone,name,year;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String registeredBefore="false";
    String brunch;
    String branch="";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        //back button
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);



         regbtn=(Button)findViewById(R.id.registerbutton);
         semspin = (Spinner) findViewById(R.id.spinner);
         regno1=(EditText) findViewById(R.id.regno);
         email1=(EditText) findViewById(R.id.email);
         phone1=(EditText) findViewById(R.id.phone);
         name1=(EditText) findViewById(R.id.name);
         skipRegistration=(TextView) findViewById(R.id.skipbutton);

         sharedPreferences=getApplicationContext().getSharedPreferences("lol",Context.MODE_PRIVATE);
         editor=sharedPreferences.edit();
         registeredBefore=sharedPreferences.getString("registeredBefore","false");


         regbtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 //downloadAllImages(email1.getText().toString());
                 register();
                 //uploadAllImages(email1.getText().toString());



             }
         });

         skipRegistration.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 skip();
             }
         });

    }

    public void skip(){
            editor.putString("registeredBefore", "true");
            editor.commit();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
    }

    public void registered(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }






    //This function creates a child and sends regis

    public int register(){
        regno=regno1.getText().toString();
        email=email1.getText().toString();
        phone=phone1.getText().toString();
        name=name1.getText().toString();
        year=semspin.getSelectedItem().toString();



        //Validate stuff and don't throw errors
        if(regno.isEmpty()||email.isEmpty()||phone.isEmpty()||name.isEmpty()){
            Toast.makeText(getApplicationContext(),R.string.fill_fields,Toast.LENGTH_LONG).show();
        }
        else if(phone.length()!=10||email.substring(email.length()-4).compareTo(".com")!=0||regno.length()!=10)
            Toast.makeText(getApplicationContext(),R.string.fields_properly,Toast.LENGTH_LONG).show();
        else{
            //Send details to the firebase child, add an on complete listener and sharedpreferences
            try {
                brunch = regno.substring(2, 7).toUpperCase();
            }
            catch(Exception e){
                Toast.makeText(getApplicationContext(),R.string.invalid_register,Toast.LENGTH_SHORT).show();
                return 0;
            }

            //Let's just guess the branch
            if(brunch.compareTo("GAEC9")==0)
                branch="CSE";
            else if(brunch.substring(0,4).compareTo("GAEI")==0)
                branch="ISE";
            else if(brunch.substring(0,4).compareTo("GAEM")==0)
                branch="ME";
            else if(brunch.compareTo("GAEE7")==0)
                branch="ECE";
            else if(brunch.compareTo("GAEE8")==0)
                branch="EEE";
            else if(brunch.compareTo("GAECV")==0)
                branch="CE";
            else if(brunch.substring(0,4).compareTo("GAEA")==0)
                branch="ARCH";
            else{
                Toast.makeText(getApplicationContext(),R.string.invalid_register,Toast.LENGTH_LONG).show();
                return 0;
            }
            editor.putString("studentBranch",branch);
            editor.putInt("studentYear",Integer.parseInt(year));
            editor.apply();
            editor.commit();

            FirebaseMessaging.getInstance().subscribeToTopic(branch).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        ref.child(regno).setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    editor.putString("registeredBefore", "true");
                                    editor.apply();
                                    editor.commit();
                                    registeredBefore = "true";
                                    ref.child(regno).child("email").setValue(email);
                                    ref.child(regno).child("year").setValue(year);
                                    ref.child(regno).child("branch").setValue(branch);
                                    ref.child(regno).child("DateOfRegistration").setValue(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                                    Toast.makeText(getApplicationContext(), "You've successfully registered in " + branch, Toast.LENGTH_LONG).show();
                                    registered();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Please try again with an active internet connection",Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Please try again with an active internet connection",Toast.LENGTH_LONG).show();
                    }
                }
            });



            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if(sharedPreferences.getString("registeredBefore","false").equals("false"))
                        Toast.makeText(getApplicationContext(),"Please try again when you're connected to the internet",Toast.LENGTH_LONG).show();
                }
            };
            new Handler().postDelayed(runnable,4000);


        }
        return 0;
    }





















    // code to upload images
    public void uploadAllImages(final String pretext){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        FirebaseStorage storageReference=FirebaseStorage.getInstance();
        Uri file=Uri.fromFile(new File("images/"+pretext));
        StorageReference organization_image=storageReference.getReference().child("logo/"+file.getLastPathSegment());
        organization_image.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(),"Upload complete",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Task failed just like you", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // code to download anything from storage
    public void downloadAllImages(final String pretext){

        FirebaseStorage storageReference=FirebaseStorage.getInstance();

        StorageReference organization_image = storageReference.getReference().child("logo/"+pretext);
        organization_image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                DownloadManager.Request request=new DownloadManager.Request(uri);
                request.setDescription("something").setTitle("some title").allowScanningByMediaScanner();
                request.setDestinationInExternalPublicDir("/UVCE-Connect",pretext);
                DownloadManager manager=(DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue((DownloadManager.Request)request);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"The file does not exist",Toast.LENGTH_LONG).show();
            }
        });


    }





}
