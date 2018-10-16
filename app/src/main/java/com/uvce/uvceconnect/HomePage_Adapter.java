package com.uvce.uvceconnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

public class HomePage_Adapter extends RecyclerView.Adapter<HomePage_Adapter.MyViewHolder> {

    private List<Hompage_ListItem> homepagelist;
    private Typeface mycustomfont;
    private Activity activity;
    private Context context;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Main_Page");
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

    public HomePage_Adapter(List<Hompage_ListItem> homepagelist, Activity activity, Context context)
    {
        this.homepagelist = homepagelist;
        this.mycustomfont = Typeface.createFromAsset(activity.getAssets(),  "fonts/adobe_font.otf");
        this.activity = activity;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homepage_listitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Hompage_ListItem listitem = homepagelist.get(position);
        if(!listitem.getLogo().isEmpty()) {
            StorageReference logoref = FirebaseStorage.getInstance().getReference().child(listitem.getLogo());
            if (!activity.isDestroyed()) {
               downloadDirect(logoref, holder.logo, 1);
            }
        } else
            holder.logo.setImageBitmap(null);
        holder.name.setText(listitem.getName());
        holder.name.setTypeface(mycustomfont);
        holder.content.setText(listitem.getContent());
        holder.content.setTypeface(mycustomfont);
        holder.timesignature.setText(listitem.getTimesignature());
        holder.timesignature.setTypeface(mycustomfont);
        holder.image.setMinimumWidth(holder.card.getWidth());
        if(!listitem.getImage().isEmpty()) {
            holder.image.setVisibility(View.VISIBLE);
            final StorageReference imageref = FirebaseStorage.getInstance().getReference().child(listitem.getImage());
            if (!activity.isDestroyed()) {
                downloadDirect(imageref, holder.image, 0);
            }
            holder.image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    builder.setTitle("Download Image");
                    builder.setMessage("Do you want to download this image?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    try {
                                        File directory = new File(Environment.getExternalStorageDirectory() + "/UVCE-Connect");

                                        if (!directory.exists()) {
                                            directory.mkdirs();
                                        }


                                        DownloadManager.Request request = new DownloadManager.Request(uri);
                                        String fileName = listitem.getImage();
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

                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setIcon(R.drawable.ic_wb_cloudy_black_24dp);
                    builder.show();


                    return true;

                }
            });
        } else
             holder.image.setVisibility(View.GONE);
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

        //For Admin Page
        if(!listitem.getAdmin().isEmpty())
        {
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    builder.setTitle("Edit or Delete");
                    builder.setMessage("Select the appropriate option").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ref.child(Integer.toString(listitem.getKey())).removeValue();
                            dialog.dismiss();
                            notifyDataSetChanged();
                        }
                    }).setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(activity, Admin_Add_Content.class);
                            intent.putExtra("Edit", true);
                            intent.putExtra("Organ_Name", listitem.getName());
                            intent.putExtra("Logo", listitem.getLogo());
                            intent.putExtra("Image", listitem.getImage());
                            intent.putExtra("Content", listitem.getContent());
                            intent.putExtra("Type", listitem.getType());
                            intent.putExtra("Time", listitem.getTimesignature());
                            intent.putExtra("Key", listitem.getKey());
                            activity.startActivity(intent);
                            activity.finish();
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return homepagelist.size();
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
