package com.uvce.uvceconnect;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImagesSelectionAdmin extends RecyclerView.Adapter<ImagesSelectionAdmin.ViewHolder> {


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.adminselectedimages,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Uri uri=imagelist.get(position);
        holder.imageView.setImageURI(uri);

    }

    @Override
    public int getItemCount() {
        return imagelist.size();
    }

    public List<Uri> imagelist;
    Activity activity;
    Context context;

    public ImagesSelectionAdmin(List<Uri> imagelist, Activity activity, Context context){
        this.context=context;
        this.activity=activity;
        this.imagelist=imagelist;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public ViewHolder(View v){
            super(v);
            imageView=(ImageView) v.findViewById(R.id.image);
        }
    }

}
