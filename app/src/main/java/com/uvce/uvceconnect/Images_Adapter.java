package com.uvce.uvceconnect;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Images_Adapter extends RecyclerView.Adapter<Images_Adapter.MyViewHolder> {
    List<StorageReference> imagesreference;
    private Activity activity;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private PhotoView photoView;
        public MyViewHolder(View view){
            super(view);
            photoView=view.findViewById(R.id.imagefortherecyclerviewinsiderecyc);
        }
    }

    public Images_Adapter(List<StorageReference> imagesreference,Activity activity,Context context){
        this.imagesreference=imagesreference;
        this.activity=activity;
        this.context=context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.imagesview,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final StorageReference storageReference=imagesreference.get(position);
        GlideApp.with(context).load(storageReference).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(holder.photoView);


    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
