package com.uvce.uvceconnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.io.Console;
import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Images_Adapter extends RecyclerView.Adapter<Images_Adapter.MyViewHolder> {
    List<StorageReference> imagesreference;
    private Activity activity;
    private Context context;
    View v;

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
        final StorageReference storageReference=imagesreference.get(position);
        Log.e("this",Integer.toString(position));
        downloadDirect(storageReference,holder.photoView,0);
        holder.photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                v= activity.getLayoutInflater().inflate(R.layout.edittextdialog,null,false);
                EditText editText=v.findViewById(R.id.text);
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle("Download Image");
                builder.setMessage("Do you want to save this image?");
                builder.setView(v);
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!editText.getText().toString().isEmpty()) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            try {
                                                File directory = new File(Environment.getExternalStorageDirectory() + "/UVCE-Connect");

                                                if (!directory.exists()) {
                                                    directory.mkdirs();
                                                }


                                                DownloadManager.Request request = new DownloadManager.Request(uri);
                                                String fileName = editText.getText().toString();
                                                request.setDescription("Image")
                                                        .setTitle(fileName)
                                                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                                        .setDestinationInExternalPublicDir("/UVCE-Connect/Images", fileName + ".jpg")
                                                        .allowScanningByMediaScanner();

                                                DownloadManager manager = (DownloadManager)
                                                        context.getSystemService(Context.DOWNLOAD_SERVICE);
                                                manager.enqueue(request);
                                                Toast.makeText(context, "Downloading.....", Toast.LENGTH_SHORT).show();

                                            } catch (Exception e) {
                                                Toast.makeText(context, "Permission not granted to read External storage. Please grant permission and try again.", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                } else {
                                    Toast.makeText(context, "Please specify a filename", Toast.LENGTH_LONG).show();
                                }

                            }
                        }).show();
                return  true;
            }
        });


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
