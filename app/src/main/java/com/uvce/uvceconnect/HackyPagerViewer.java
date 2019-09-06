package com.uvce.uvceconnect;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class HackyPagerViewer extends ViewPager {
    public HackyPagerViewer(@NonNull Context context) {
        super(context);
    }

    public HackyPagerViewer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            super.onInterceptTouchEvent(ev);
        }
        catch (IllegalArgumentException e){
            return false;
        }

        return true;

    }


}
