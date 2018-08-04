package com.uvce.uvceconnect;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class HomePage_Adapter extends RecyclerView.Adapter<HomePage_Adapter.MyViewHolder> {

    private List<Hompage_ListItem> homepagelist;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, content, timesignature;
        public ImageView image, logo;

        public MyViewHolder(View view)
        {
            super(view);
            name = view.findViewById(R.id.listitem_name);
            content = view.findViewById(R.id.listitem_content);
            timesignature = view.findViewById(R.id.listitem_timesignature);
            image = view.findViewById(R.id.listitem_image);
            logo = view.findViewById(R.id.listitem_logo);
        }

    }

    public HomePage_Adapter(List<Hompage_ListItem> homepagelist)
    {
        this.homepagelist = homepagelist;
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
        holder.content.setText(listitem.getContent());
        holder.timesignature.setText(listitem.getTimesignature());
        holder.image.setImageResource(R.drawable.icon_activities);
    }

    @Override
    public int getItemCount() {
        return homepagelist.size();
    }
}
