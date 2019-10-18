package com.uvce.uvceconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ForumActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ForumActivityAdapter forumActivityAdapter;
    List<ForumListItem> listItem=new ArrayList<>();
    String postno;
    MaterialButton post;
    String pos="0";



    @Override
    protected void onResume() {
        populateForum();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        post=findViewById(R.id.post);

        recyclerView=findViewById(R.id.recycler_view);
        forumActivityAdapter=new ForumActivityAdapter(listItem);
        recyclerView.setAdapter(forumActivityAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postno=Integer.toString(getIntent().getIntExtra("postno",0));
        //postno="0";
        populateForum();
        forumActivityAdapter.notifyDataSetChanged();

        FirebaseDatabase.getInstance().getReference("TestDatabase/Forum/"+postno).orderByKey().limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren())
                        pos = childSnapshot.getKey();
                }
                catch (Exception e){
                    Log.e("except","accept");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recyclerView.smoothScrollToPosition(0);
                View view=getLayoutInflater().inflate(R.layout.edittextdialog,null,false);
                EditText editText=view.findViewById(R.id.text);
                editText.setHint("Enter text here");

                new AlertDialog.Builder(ForumActivity.this).setView(view).setTitle("Create Post").setPositiveButton("POST", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!editText.getText().toString().isEmpty()){



                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("TestDatabase/Forum/"+postno).child(Integer.toString(Integer.parseInt(pos)-1));
                            ref.child("username").setValue(getPreferences(MODE_PRIVATE).getString("username","Anonymous"));
                            ref.child("content").setValue(editText.getText().toString());
                            ref.child("qusername").setValue("-1");
                            ref.child("qcontent").setValue("-1");
                            ref.child("timestamp").setValue(new SimpleDateFormat("dd/MM/yyyy, hh:mm a").format(new Date())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(ForumActivity.this,"Posted Successfully",Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(ForumActivity.this,"Try again with a working internet connection",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                    }
                }).setCancelable(true).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

    }

    private void populateForum(){
        FirebaseDatabase.getInstance().getReference("TestDatabase/Forum").child(postno).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listItem.clear();
                for(DataSnapshot childSnapshot:dataSnapshot.getChildren()){
                    try {
                        listItem.add(new ForumListItem(childSnapshot.child("username").getValue().toString(), childSnapshot.child("content").getValue().toString(), childSnapshot.child("qusername").getValue().toString(), childSnapshot.child("qcontent").getValue().toString(), childSnapshot.child("timestamp").getValue().toString()));
                    }
                    catch(NullPointerException e){ }
                }
                forumActivityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class ForumActivityAdapter extends RecyclerView.Adapter<ForumActivityAdapter.TheMyViewHolder>{

        public ForumActivityAdapter(List<ForumListItem> listItem) {
            this.listItem = listItem;
        }

        List<ForumListItem> listItem=new ArrayList<>();





        public class TheMyViewHolder extends RecyclerView.ViewHolder{
            public TextView username,ucontent,quotedusername,quotedcontent,timestamp;
            public CardView cardView;
            public MaterialButton reply;


            public TheMyViewHolder(View view){
                super(view);
                username=view.findViewById(R.id.username);
                ucontent=view.findViewById(R.id.content);
                quotedusername=view.findViewById(R.id.quotedusername);
                quotedcontent=view.findViewById(R.id.quotedcontent);
                timestamp=view.findViewById(R.id.timestamp);
                reply=view.findViewById(R.id.reply);
                cardView=view.findViewById(R.id.quoted);
            }
        }

        @NonNull
        @Override
        public TheMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v=getLayoutInflater().inflate(R.layout.content_forum,parent,false);
            return new TheMyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull TheMyViewHolder holder, int position) {
            holder.username.setText(listItem.get(position).getUsername());
            holder.ucontent.setText(listItem.get(position).getUcontent());
            String quoted=listItem.get(position).getQuotedusername();
            if(!quoted.equals("-1")){
                holder.cardView.setVisibility(View.VISIBLE);
                holder.quotedusername.setText(quoted);
                holder.quotedcontent.setText(listItem.get(position).getQuotedcontent());

            }
            else
                holder.cardView.setVisibility(View.GONE);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("k",Integer.toString(position));

                    recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,new RecyclerView.State(),5);
                }
            });
            holder.timestamp.setText(listItem.get(position).getTimestamp());
            holder.reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view=getLayoutInflater().inflate(R.layout.edittextdialog,null,false);
                    EditText editText=view.findViewById(R.id.text);
                    editText.setHint("Enter text here");

                    new AlertDialog.Builder(ForumActivity.this).setView(view).setTitle("Reply to Post").setPositiveButton("post", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!editText.getText().toString().isEmpty()){

                                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("TestDatabase/Forum/"+postno).child(Integer.toString(Integer.parseInt(pos)-1));
                                ref.child("username").setValue(getPreferences(MODE_PRIVATE).getString("username","Anonymous"));
                                ref.child("content").setValue(editText.getText().toString());
                                ref.child("qusername").setValue(listItem.get(position).getUsername());
                                ref.child("qcontent").setValue(listItem.get(position).getUcontent());
                                ref.child("timestamp").setValue(listItem.get(position).getTimestamp()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            populateForum();
                                            Toast.makeText(ForumActivity.this,"Posted Successfully",Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            Toast.makeText(ForumActivity.this,"Try again with a working internet connection",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }
                        }
                    }).setCancelable(true).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return listItem.size();
        }
    }

    public class ForumListItem{
        private String username,ucontent,quotedusername,quotedcontent,timestamp;

        public ForumListItem(String username, String ucontent, String quotedusername, String quotedcontent, String timestamp) {
            this.username = username;
            this.ucontent = ucontent;
            this.quotedusername = quotedusername;
            this.quotedcontent = quotedcontent;
            this.timestamp = timestamp;
        }

        public String getUsername() {
            return username;
        }

        public String getUcontent() {
            return ucontent;
        }

        public String getQuotedusername() {
            return quotedusername;
        }

        public String getQuotedcontent() {
            return quotedcontent;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }
}
