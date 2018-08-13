package com.uvce.uvceconnect;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class HomePage_Adapter extends RecyclerView.Adapter<HomePage_Adapter.MyViewHolder> {

    private List<Hompage_ListItem> homepagelist;
    public Typeface mycustomfont;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, content, timesignature;
        public ImageView image, logo;
        public CardView card;

        public MyViewHolder(View view)
        {
            super(view);
            name = view.findViewById(R.id.listitem_name);
            content = view.findViewById(R.id.listitem_content);
            timesignature = view.findViewById(R.id.listitem_timesignature);
            image = view.findViewById(R.id.listitem_image);
            logo = view.findViewById(R.id.listitem_logo);
            card = view.findViewById(R.id.listitem_card);
        }

    }

    public HomePage_Adapter(List<Hompage_ListItem> homepagelist, Activity activity)
    {
        this.homepagelist = homepagelist;
        this.mycustomfont = Typeface.createFromAsset(activity.getAssets(),  "fonts/adobe_font.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homepage_listitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Hompage_ListItem listitem = homepagelist.get(position);
        holder.logo.setImageResource(R.drawable.common_google_signin_btn_icon_dark_focused);
        holder.name.setText(listitem.getName());
        holder.name.setTypeface(mycustomfont);
        holder.content.setText(listitem.getContent());
        holder.content.setTypeface(mycustomfont);
        holder.timesignature.setText(listitem.getTimesignature());
        holder.timesignature.setTypeface(mycustomfont);
        holder.image.setImageResource(R.drawable.icon_activities);
        if(listitem.getType()==1) {
            holder.card.setCardBackgroundColor(Color.parseColor("#1565C0"));
            holder.name.setTextColor(Color.parseColor("White"));
            holder.content.setTextColor(Color.parseColor("White"));
            holder.timesignature.setTextColor(Color.parseColor("White"));
        }
        else {
            holder.card.setCardBackgroundColor(Color.parseColor("White"));
            holder.name.setTextColor(Color.parseColor("Black"));
            holder.content.setTextColor(Color.parseColor("Black"));
            holder.timesignature.setTextColor(Color.parseColor("#8b8b8b"));
        }
    }

    @Override
    public int getItemCount() {
        return homepagelist.size();
    }
}
