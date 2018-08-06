package com.uvce.uvceconnect;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class foundation_tab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_foundation_tab, container, false);
        Typeface mycustomfont = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/adobe_font.otf");
        //text view contents of foundation layout
        TextView card1_content =(TextView)view.findViewById(R.id.card1_content_foundation);
        TextView card2_content =(TextView)view.findViewById(R.id.card2_content_foundation);
        TextView card3_content =(TextView)view.findViewById(R.id.card3_content_foundation);
        //customfont for the textviews in each cardview
        card1_content.setTypeface(mycustomfont);
        card2_content.setTypeface(mycustomfont);
        card3_content.setTypeface(mycustomfont);
        return view;
    }
}


