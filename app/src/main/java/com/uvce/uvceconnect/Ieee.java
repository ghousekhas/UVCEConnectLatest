package com.uvce.uvceconnect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

//mahith
public class Ieee extends AppCompatActivity {

    GridLayout gridLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ieee);

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView wie = (ImageView)findViewById(R.id.wie);
        ImageView cs = (ImageView)findViewById(R.id.cs);
        ImageView avishkar = (ImageView)findViewById(R.id.avishkar);
        ImageView robotics = (ImageView)findViewById(R.id.robotics);
        ImageView design = (ImageView)findViewById(R.id.design);
        ImageView art = (ImageView)findViewById(R.id.art);
        ImageView varchas = (ImageView)findViewById(R.id.varchas);
        ImageView vidyuth = (ImageView)findViewById(R.id.vidyuth);
        ImageView tstar = (ImageView)findViewById(R.id.tstar);
        ImageView literary = (ImageView)findViewById(R.id.literary);

        gridLayout = (GridLayout)findViewById(R.id.gridLayout);

        StorageReference wieStorage  = FirebaseStorage.getInstance().getReference().child("logo/wie.jpg");
        StorageReference csStorage  = FirebaseStorage.getInstance().getReference().child("logo/cs.jpg");
        StorageReference avishkarStorage  = FirebaseStorage.getInstance().getReference().child("logo/avishkar.jpg");
        StorageReference roboticsStorage  = FirebaseStorage.getInstance().getReference().child("logo/robotics.jpg");
        StorageReference designStorage  = FirebaseStorage.getInstance().getReference().child("logo/design.jpg");
        StorageReference artStorage  = FirebaseStorage.getInstance().getReference().child("logo/art.jpg");
        StorageReference varchasStorage  = FirebaseStorage.getInstance().getReference().child("logo/Varchas.png");
        StorageReference vidyuthStorage  = FirebaseStorage.getInstance().getReference().child("logo/vidyuth.jpg");
        StorageReference tstarStorage  = FirebaseStorage.getInstance().getReference().child("logo/tstar.jpg");
        StorageReference literaryStorage  = FirebaseStorage.getInstance().getReference().child("logo/Literary.jpg");


        Log.d("image=",(wieStorage).toString());
        Log.d("image=",(csStorage).toString());
        Log.d("image=",(avishkarStorage).toString());
        // Load the image using Glide
        Glide.with(this )
                .load(wieStorage)
                .into(wie );
        Glide.with(this )
                .load(csStorage)
                .into(cs);
        Glide.with(this )
                .load(avishkarStorage)
                .into(avishkar);
        Glide.with(this )
                .load(roboticsStorage)
                .into(robotics);
        Glide.with(this )
                .load(designStorage)
                .into(design );
        Glide.with(this )
                .load(artStorage)
                .into(art );
        Glide.with(this )
                .load(varchasStorage)
                .into(varchas);
        Glide.with(this )
                .load(vidyuthStorage)
                .into(vidyuth );
        Glide.with(this )
                .load(tstarStorage)
                .into(tstar );
        Glide.with(this )
                .load(literaryStorage)
                .into(literary );



        setSingleEvent(gridLayout);

    }

    private void setSingleEvent(GridLayout gridLayout) {

        for(int i =0;i< gridLayout.getChildCount();i++)
        {
            CardView cardGrid = (CardView)gridLayout.getChildAt(i);
            final int selection = i;
            Log.e("grid","item no."+i);
            cardGrid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("grid","item clicked."+selection);
                    //  Toast.makeText(Adhamya.this,"itemClick!"+selection,Toast.LENGTH_SHORT).show();
                    if(selection == 0){
                        startActivity(new Intent(Ieee.this,Wie.class));
                    }
                    else if(selection == 1){
                        startActivity(new Intent(Ieee.this,Cs.class));
                    }
                    else if(selection == 2){
                        startActivity(new Intent(Ieee.this,Avishkar.class));
                    }
                    else if(selection == 3){
                        startActivity(new Intent(Ieee.this,Robotics.class));
                    }
                    else if(selection == 4){
                        startActivity(new Intent(Ieee.this,Design.class));
                    }
                    else if(selection == 5){
                        startActivity(new Intent(Ieee.this,Art.class));
                    }
                    else if(selection == 6){
                        startActivity(new Intent(Ieee.this,Varchas.class));
                    }
                    else if(selection == 7){
                        startActivity(new Intent(Ieee.this,Vidyuth.class));
                    }
                    else if(selection == 8){
                        startActivity(new Intent(Ieee.this,Tstar.class));
                    }
                    else if(selection == 9){
                        startActivity(new Intent(Ieee.this,Literary.class));
                    }
                    else
                        Toast.makeText(Ieee.this,"Please Select a Correct Field!",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
