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
import android.view.Menu;
import android.view.MenuItem;
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
public class Adhamya extends AppCompatActivity {

    GridLayout gridLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adhamya);

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addPics();

        gridLayout = (GridLayout)findViewById(R.id.gridLayout);


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
                        startActivity(new Intent(Adhamya.this,Chethana.class));
                    }
                    else if(selection == 1){
                        startActivity(new Intent(Adhamya.this,Tatva.class));
                    }
                    else if(selection == 2){
                        startActivity(new Intent(Adhamya.this,Chakravyuha.class));
                    }
                    else if(selection == 3){
                        startActivity(new Intent(Adhamya.this,G2c2.class));
                    }
                    else if(selection == 4){
                        startActivity(new Intent(Adhamya.this,Vinimaya.class));
                    }
                    else if(selection == 5){
                        startActivity(new Intent(Adhamya.this,Ecell.class));
                    }
                    else if(selection == 6){
                        startActivity(new Intent(Adhamya.this,Sae.class));
                    }
                    else if(selection == 7){
                        startActivity(new Intent(Adhamya.this,Sports.class));
                    }
                    else if(selection == 8){
                        startActivity(new Intent(Adhamya.this,dance.class));
                    }
                    else if(selection == 9){
                        startActivity(new Intent(Adhamya.this,Music.class));
                    }
                    else
                        Toast.makeText(Adhamya.this,"Please Select a Correct Field!",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    void addPics(){
        ImageView chethana = (ImageView)findViewById(R.id.chethana);
        ImageView tatva = (ImageView)findViewById(R.id.tatva);
        ImageView chakravyuha = (ImageView)findViewById(R.id.chakravyuha);
        ImageView g2c2 = (ImageView)findViewById(R.id.g2c2);
        ImageView vinimaya = (ImageView)findViewById(R.id.vinimaya);
        ImageView ecell = (ImageView)findViewById(R.id.ecell);
        ImageView sae = (ImageView)findViewById(R.id.sae);
        ImageView sports = (ImageView)findViewById(R.id.sports);
        ImageView music = (ImageView)findViewById(R.id.music);
        ImageView dance = (ImageView)findViewById(R.id.dance);

        StorageReference chethanaStorage  = FirebaseStorage.getInstance().getReference().child("logo/chethana.jpg");
        StorageReference tatvaStorage  = FirebaseStorage.getInstance().getReference().child("logo/tatva.jpg");
        StorageReference chakravyuhaStorage  = FirebaseStorage.getInstance().getReference().child("logo/chakravyuha.jpg");
        StorageReference g2c2Storage  = FirebaseStorage.getInstance().getReference().child("logo/G2C2.jpg");
        StorageReference vinimayaStorage  = FirebaseStorage.getInstance().getReference().child("logo/Vinimaya.jpg");
        StorageReference ecellStorage  = FirebaseStorage.getInstance().getReference().child("logo/E-Cell UVCE.jpg");
        StorageReference saeStorage  = FirebaseStorage.getInstance().getReference().child("logo/SAE.jpg");
        StorageReference sportsStorage  = FirebaseStorage.getInstance().getReference().child("logo/sports.jpg");
        StorageReference musicStorage  = FirebaseStorage.getInstance().getReference().child("logo/music.jpg");
        StorageReference danceStorage  = FirebaseStorage.getInstance().getReference().child("logo/dance.png");


        Log.d("image=",(chethanaStorage).toString());
        Log.d("image=",(vinimayaStorage).toString());
        Log.d("image=",(chakravyuhaStorage).toString());
        // Load the image using Glide
        Glide.with(this )
                .load(chethanaStorage)
                .into(chethana );
        Glide.with(this )
                .load(tatvaStorage)
                .into(tatva );
        Glide.with(this )
                .load(chakravyuhaStorage)
                .into(chakravyuha );
        Glide.with(this )
                .load(g2c2Storage)
                .into(g2c2 );
        Glide.with(this )
                .load(vinimayaStorage)
                .into(vinimaya );
        Glide.with(this )
                .load(ecellStorage)
                .into(ecell );
        Glide.with(this )
                .load(saeStorage)
                .into(sae );
        Glide.with(this )
                .load(sportsStorage)
                .into(sports );
        Glide.with(this )
                .load(musicStorage)
                .into(music );
        Glide.with(this )
                .load(danceStorage)
                .into(dance );



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.club_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.info) {
//            Toast.makeText(this,"Yeah you just clicked me!",Toast.LENGTH_SHORT).show();
             startActivity(new Intent(this,Content_Adhamya.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
