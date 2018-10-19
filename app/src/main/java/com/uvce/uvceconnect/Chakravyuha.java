package com.uvce.uvceconnect;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
// mahith
public class Chakravyuha extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chakravyuha);

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //customfont

        /*
        custom font
         */
        TextView card1_content =(TextView)findViewById(R.id.card1_content);
        TextView card2_content =(TextView)findViewById(R.id.card2_content);
        TextView card3_content =(TextView)findViewById(R.id.card3_content);
        //customfont
        Typeface mycustomfont = Typeface.createFromAsset(getAssets(),  "fonts/adobe_font.otf");

        //customfont for the textviews in each cardview
        card1_content.setTypeface(mycustomfont);
        card2_content.setTypeface(mycustomfont);
        card3_content.setTypeface(mycustomfont);

    }
}
