package com.uvce.uvceconnect;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.io.Console;
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
            photoView=view.findViewById(R.id.imagesss);
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
        StorageReference storageReference=imagesreference.get(position);
        Log.e("this",Integer.toString(position));
        downloadDirect(storageReference,holder.photoView,0);


    }

    @Override
    public int getItemCount() {
        return imagesreference.size();
    }

    public void downloadDirect(StorageReference imageRef, ImageView imageView, int type) {
        //type : 0-Image, 1-Logo
        try {
            // Download directly from StorageReference using Glide
            // (See HomepageGlideModule for Loader registration)
            if(type==0) {
                GlideApp.with(context)
                        .load(imageRef)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            } else {
                GlideApp.with(context)
                        .load(imageRef)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            }
        }catch (Exception ex){
            Toast.makeText(activity, "Error in downloading image", Toast.LENGTH_SHORT).show();;
        }
    }
}
