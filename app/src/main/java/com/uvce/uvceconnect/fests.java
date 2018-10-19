package com.uvce.uvceconnect;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.w3c.dom.Text;
// mahith
public class fests extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fests);

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView cultural_fest = (TextView)findViewById(R.id.cultural_fest);
        TextView uvce_jaathre = (TextView)findViewById(R.id.uvce_jaathre);
        TextView content_uvce_jaathre = (TextView)findViewById(R.id.content_uvce_jaathre);
        TextView fiesta = (TextView)findViewById(R.id.fiesta);
        TextView content_fiesta = (TextView)findViewById(R.id.content_fiesta);
        TextView civista = (TextView)findViewById(R.id.civista);
        TextView content_civista = (TextView)findViewById(R.id.content_civista);
        TextView esportivo = (TextView)findViewById(R.id.esportivo);
        TextView content_esportivo = (TextView)findViewById(R.id.content_esportivo);
        TextView milagro = (TextView)findViewById(R.id.milagro);
        TextView content_milagro = (TextView)findViewById(R.id.content_milagro);
        TextView technical_fest = (TextView)findViewById(R.id.technical_fest);
        TextView kagada = (TextView)findViewById(R.id.kagada);
        TextView content_kagada = (TextView)findViewById(R.id.content_kagada);
        TextView inspiron = (TextView)findViewById(R.id.inspiron);
        TextView content_inspiron = (TextView)findViewById(R.id.content_inspiron);
        TextView impetus = (TextView)findViewById(R.id.impetus);
        TextView content_impetus = (TextView)findViewById(R.id.content_impetus);

        //customfont
        Typeface mycustomfont = Typeface.createFromAsset(getAssets(),  "fonts/adobe_font.otf");
        cultural_fest.setTypeface(mycustomfont);
        technical_fest.setTypeface(mycustomfont);
        uvce_jaathre.setTypeface(mycustomfont);
        content_uvce_jaathre.setTypeface(mycustomfont);
        fiesta.setTypeface(mycustomfont);
        content_fiesta.setTypeface(mycustomfont);
        civista.setTypeface(mycustomfont);
        content_civista.setTypeface(mycustomfont);
        esportivo.setTypeface(mycustomfont);
        content_esportivo.setTypeface(mycustomfont);
        milagro.setTypeface(mycustomfont);
        content_milagro.setTypeface(mycustomfont);
        kagada.setTypeface(mycustomfont);
        content_kagada.setTypeface(mycustomfont);
        inspiron.setTypeface(mycustomfont);
        content_inspiron.setTypeface(mycustomfont);
        impetus.setTypeface(mycustomfont);
        content_impetus.setTypeface(mycustomfont);
    }
}
