package com.uvce.uvceconnect;

import android.graphics.Typeface;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;

import android.widget.TextView;
// mahith
public class associations extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associations);
        //toobar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // tab activity
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            TextView card1_content,card2_content,card3_content,card4_content;
            //customfont
            Typeface mycustomfont = Typeface.createFromAsset(getAssets(),  "fonts/adobe_font.otf");

            switch(position)
            {
                case 0:
                    alumini_tab tab1 = new alumini_tab();
                    return tab1;

                case 1:
                    vision_tab tab2 = new vision_tab();
                    return tab2;

                case 2:
                    foundation_tab tab3 = new foundation_tab();
                    return tab3;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            //number of tabs
            return 3;
        }


        }
}
