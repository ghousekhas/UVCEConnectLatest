package com.uvce.uvceconnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.SnapHelper;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomePage_Adapter extends RecyclerView.Adapter<HomePage_Adapter.MyViewHolder> {

    private List<Homepage_ListItem> homepagelist;
    private Typeface mycustomfont;
    private Activity activity;
    private Context context;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Main_Page");
    private FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
    private StorageReference photoRef;
    private StorageReference FileRef;
    private String docid = "", fileid = "";
    StorageReference fileref;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, content, timesignature,link;
        public ImageView image, logo;
        public CardView card;
        public RecyclerView recyclerView;

        //public HackyPagerViewer pagerViewer;


        public MyViewHolder(View view)
        {
            super(view);
            name = view.findViewById(R.id.listitem_name);
            content = view.findViewById(R.id.listitem_content);
            timesignature = view.findViewById(R.id.listitem_timesignature);
            image = view.findViewById(R.id.listitem_image);
            logo = view.findViewById(R.id.listitem_logo);
            card = view.findViewById(R.id.listitem_card);
            link= view.findViewById(R.id.linktodownload);
            recyclerView=view.findViewById(R.id.recyclerview_for_images);
        }

    }

    public HomePage_Adapter(List<Homepage_ListItem> homepagelist, Activity activity, Context context)
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
        final Homepage_ListItem listitem = homepagelist.get(position);
        if(!listitem.getLogo().isEmpty()) {
            StorageReference logoref = FirebaseStorage.getInstance().getReference().child(listitem.getLogo());
            if (!activity.isDestroyed()) {
               downloadDirect(logoref, holder.logo, 1);
            }
        } else
            holder.logo.setImageBitmap(null);
        holder.name.setText(listitem.getName());
        holder.content.setText(listitem.getContent());
        holder.timesignature.setText(listitem.getTimesignature());
        holder.image.setMinimumWidth(holder.card.getWidth());

        final List<StorageReference> imagelist=new ArrayList<>();
        final Images_Adapter images_adapter;
        DatabaseReference databaseReference;


        images_adapter=new Images_Adapter(imagelist,activity,context);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
        SnapHelper snapHelper=new PagerSnapHelper();
        holder.recyclerView.setLayoutManager(manager);
        holder.recyclerView.setAdapter(images_adapter);
        holder.recyclerView.setOnFlingListener(null);
        holder.recyclerView.addItemDecoration(new CirclePagerIndicatorDecoration());
        snapHelper.attachToRecyclerView(holder.recyclerView);

        /*RecyclerView.LayoutParams recyclerlayoutparams= holder.recyclerView.getLayoutParams();
        recyclerlayoutparams.height= somebody's gotta do this somwday, gonna make it 35% of parent!*/

        Log.e("key",Integer.toString(listitem.getKey()));


        databaseReference=FirebaseDatabase.getInstance().getReference().child("TestDatabase").child("Main_Page").child(Integer.toString(listitem.getKey())).child("images");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imagelist.clear();
                for(DataSnapshot childsnapshot: dataSnapshot.getChildren())
                    imagelist.add(FirebaseStorage.getInstance().getReference().child(childsnapshot.getValue().toString()));
                images_adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        if(!listitem.getLink().equals("-1")){
            holder.link.setVisibility(View.VISIBLE);
            holder.link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    docid = listitem.getLink();
                    fileid = listitem.getFilename();
                    fileref = FirebaseStorage.getInstance().getReference().child(listitem.getLink());
                    startdownload();

                }
            });
        } else {
            holder.link.setVisibility(View.GONE);
        }


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
        }
        else
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
                    builder.setMessage("Select the appropriate option.").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!listitem.getImage().isEmpty()) {
                                photoRef = mFirebaseStorage.getReference(listitem.getImage());
                                photoRef.delete();
                            }
                            if(!listitem.getLink().isEmpty()) {
                                FileRef = mFirebaseStorage.getReference(listitem.getLink());
                                FileRef.delete();
                            }
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
                            intent.putExtra("Link", listitem.getLink());
                            intent.putExtra("FileName", listitem.getFilename());
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






    //download method

    private void startdownload() {
        /*try {
            String url="https://docs.google.com/uc?id=[FILE_ID]&export=download";
            String id=getID(docid);
            url=url.replace("[FILE_ID]",id);
            Download download=new Download(context, fileid ,url);
            download.start();
        }
        catch (SecurityException e){
            Toast.makeText(context,"Please grant storage permissions to download",Toast.LENGTH_LONG).show();
        } catch (Exception e) {

            Toast.makeText(activity, "An Error has occured in downloading the file", Toast.LENGTH_SHORT).show();

        }*/

        fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    File directory = new File(Environment.getExternalStorageDirectory() + "/UVCE-Connect");

                    if (!directory.exists()) {
                        directory.mkdirs();
                    }


                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    String fileName = fileid;
                    request.setDescription("File")
                            .setTitle(fileName)
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setDestinationInExternalPublicDir("/UVCE-Connect/Files", fileName)
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






    private String getID(String url) {
        String ID = "";

        for (int i = url.length() - 1; i >= 0; --i) {
            if (url.charAt(i) != '=') {
                ID = url.charAt(i) + ID;
            } else {
                break;
            }
        }

        return ID;
    }


}
