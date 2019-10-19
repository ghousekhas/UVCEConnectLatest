package com.uvce.uvceconnect;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

public class HackyDrawerView extends DrawerLayout {
    public  HackyDrawerView(@NonNull Context context){
        super(context);
    }

    public HackyDrawerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HackyDrawerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (Exception e) {
            //uncomment if you really want to see these errors
            //e.printStackTrace();
            return true;
        }

    }
}
