package com.uvce.uvceconnect;

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

}
