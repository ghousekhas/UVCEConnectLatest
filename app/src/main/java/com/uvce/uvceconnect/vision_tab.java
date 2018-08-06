package com.uvce.uvceconnect;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class vision_tab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_vision_tab, container, false);
        Typeface mycustomfont = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/adobe_font.otf");
        //text view contents of vision layout
        TextView card1_content =(TextView)view.findViewById(R.id.card1_content_vision);
        TextView card2_content =(TextView)view.findViewById(R.id.card2_content_vision);
        TextView card3_content =(TextView)view.findViewById(R.id.card3_content_vision);
        TextView card4_content =(TextView)view.findViewById(R.id.card4_content_vision);
        //customfont for the textviews in each cardview
        card1_content.setTypeface(mycustomfont);
        card2_content.setTypeface(mycustomfont);
        card3_content.setTypeface(mycustomfont);
        card4_content.setTypeface(mycustomfont);

        return view;
    }

}
