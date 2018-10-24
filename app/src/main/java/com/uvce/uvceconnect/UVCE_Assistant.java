package com.uvce.uvceconnect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.api.AIListener;
import ai.api.AIServiceException;

import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.model.AIEvent;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import android.provider.Settings.Secure;
import android.widget.Toast;


public class UVCE_Assistant extends AppCompatActivity implements AIListener {

    RecyclerView recyclerView;
    EditText editText;
    RelativeLayout addBtn;
    DatabaseReference ref;
    FirebaseRecyclerAdapter<ChatMessage,chat_rec> adapter;
    Boolean flagFab = true;
    private String android_id;
    private String name;
    private AIService aiService;
    boolean name_flag = false;
    String temp_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uvce__assistant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        android_id =  Secure.getString(getApplicationContext().getContentResolver(),
                Secure.ANDROID_ID);

        //Permissions
        if (Build.VERSION.SDK_INT >= 23) { // if android version >= 6.0
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(), android.Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {

                // if permission was not granted initially, ask the user again.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.RECORD_AUDIO},
                        1);
            }
        }

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        editText = (EditText)findViewById(R.id.editText);
        addBtn = (RelativeLayout)findViewById(R.id.addBtn);

        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);



        initialchat();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ref.child("Devices").child(android_id).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        try {

                            if (dataSnapshot.getValue() == null) {

                                ChatMessage chatMessage = new ChatMessage("May I know your name please?", "bot");
                                ref.child("Devices").child(android_id).child("chat").push().setValue(chatMessage);
                                addBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        name = editText.getText().toString().trim();
                                        ChatMessage chatMessagereply = new ChatMessage(name, "user");
                                        ref.child("Devices").child(android_id).child("chat").push().setValue(chatMessagereply);
                                        if (!name_flag) {
                                            if (name.contains("My name is "))
                                                name = name.replace("My name is ", "");
                                            else if (name.contains("my name is "))
                                                name = name.replace("my name is ", "");
                                            else if (name.contains("My name's "))
                                                name = name.replace("My name's ", "");
                                            else if (name.contains("my name's "))
                                                name = name.replace("my name's ", "");
                                            else if (name.contains("It's "))
                                                name = name.replace("It's ", "");
                                            else if (name.contains("it's "))
                                                name = name.replace("it's ", "");
                                            else if (name.contains("It is "))
                                                name = name.replace("It is ", "");
                                            else if (name.contains("it is "))
                                                name = name.replace("it is ", "");
                                            else if (name.contains("Myself "))
                                                name = name.replace("Myself ", "");
                                            else if (name.contains("myself "))
                                                name = name.replace("myself ", "");
                                            ChatMessage chatMessage = new ChatMessage("So your name is " + name + ", am I right?", "bot");
                                            ref.child("Devices").child(android_id).child("chat").push().setValue(chatMessage);
                                            temp_name = name;
                                            name_flag = true;
                                        } else {
                                            if (editText.getText().toString().trim().contains("Yes") || editText.getText().toString().trim().contains("yes") || editText.getText().toString().trim().contains("Yep") || editText.getText().toString().trim().contains("yep")) {
                                                name = temp_name;
                                                ref.child("Devices").child(android_id).child("Name").setValue(name);
                                                ChatMessage chatMessage = new ChatMessage("Thank You! Ask me anything, " + name + ".", "bot");
                                                ref.child("Devices").child(android_id).child("chat").push().setValue(chatMessage);
                                                onnamereceived();
                                            } else {
                                                name_flag = false;
                                                ChatMessage chatMessage = new ChatMessage("Sorry to ask again. May I know your name please?", "bot");
                                                ref.child("Devices").child(android_id).child("chat").push().setValue(chatMessage);
                                            }
                                        }

                                        editText.setText("");

                                    }
                                });

                            } else {
                                name = dataSnapshot.getValue().toString();
                                onnamereceived();
                            }
                        } catch (Exception e) {

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }, 1000);








        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ImageView fab_img = (ImageView)findViewById(R.id.fab_img);
                Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.ic_send_white_24dp);
                Bitmap img1 = BitmapFactory.decodeResource(getResources(),R.drawable.ic_mic_white_24dp);


                if (s.toString().trim().length()!=0 && flagFab){
                    ImageViewAnimatedChange(getApplicationContext(),fab_img,img);
                    flagFab=false;

                }
                else if (s.toString().trim().length()==0){
                    ImageViewAnimatedChange(getApplicationContext(),fab_img,img1);
                    flagFab=true;

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        adapter = new FirebaseRecyclerAdapter<ChatMessage, chat_rec>(ChatMessage.class,R.layout.msglist,chat_rec.class,ref.child("Devices").child(android_id).child("chat")) {
            @Override
            protected void populateViewHolder(chat_rec viewHolder, ChatMessage model, int position) {

                if (model.getMsgUser().equals("user")) {


                    viewHolder.rightText.setText(model.getMsgText());

                    viewHolder.rightText.setVisibility(View.VISIBLE);
                    viewHolder.leftText.setVisibility(View.GONE);
                }
                else {
                    viewHolder.leftText.setText(model.getMsgText());

                    viewHolder.rightText.setVisibility(View.GONE);
                    viewHolder.leftText.setVisibility(View.VISIBLE);
                }
            }
        };

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int msgCount = adapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisiblePosition == -1 ||
                        (positionStart >= (msgCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);

                }

            }
        });

        recyclerView.setAdapter(adapter);


    }
    public void ImageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.zoom_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, R.anim.zoom_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageBitmap(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    @Override
    public void onResult(ai.api.model.AIResponse response) {


        Result result = response.getResult();

        String message = result.getResolvedQuery();
        ChatMessage chatMessage0 = new ChatMessage(message, "user");
        ref.child("Devices").child(android_id).child("chat").push().setValue(chatMessage0);


        String reply = result.getFulfillment().getSpeech();
        ChatMessage chatMessage = new ChatMessage(reply, "bot");
        ref.child("Devices").child(android_id).child("chat").push().setValue(chatMessage);


    }

    @Override
    public void onError(ai.api.model.AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    void initialchat()
    {
        final AIConfiguration config = new AIConfiguration("108eee18636643379296e12abafe380f",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(getApplicationContext(), config);
        aiService.setListener(this);

        final AIDataService aiDataService = new AIDataService(getApplicationContext(), config);

        final AIRequest aiRequest = new AIRequest();

        ChatMessage chatMessage = new ChatMessage("Hi", "user");
        //ref.child("Devices").child(android_id).child("chat").push().setValue(chatMessage);

        aiRequest.setQuery("ABCD1234");
        new AsyncTask<AIRequest,Void,AIResponse>(){

            @Override
            protected AIResponse doInBackground(AIRequest... aiRequests) {
                final AIRequest request = aiRequests[0];
                try {
                    final AIResponse response = aiDataService.request(aiRequest);
                    return response;
                } catch (AIServiceException e) {
                }
                return null;
            }
            @Override
            protected void onPostExecute(AIResponse response) {
                if (response != null) {

                    Result result = response.getResult();
                    String reply = result.getFulfillment().getSpeech();
                    ChatMessage chatMessage = new ChatMessage(reply, "bot");
                    ref.child("Devices").child(android_id).child("chat").push().setValue(chatMessage);
                }
            }
        }.execute(aiRequest);
    }

    void onnamereceived()
    {
        final AIConfiguration config = new AIConfiguration("108eee18636643379296e12abafe380f",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(getApplicationContext(), config);
        aiService.setListener(this);

        final AIDataService aiDataService = new AIDataService(getApplicationContext(), config);

        final AIRequest aiRequest = new AIRequest();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                String message = editText.getText().toString().trim();

                if (!message.equals("")) {

                    ChatMessage chatMessage = new ChatMessage(message, "user");
                    ref.child("Devices").child(android_id).child("chat").push().setValue(chatMessage);

                    aiRequest.setQuery(message);
                    new AsyncTask<AIRequest,Void,AIResponse>(){

                        @Override
                        protected AIResponse doInBackground(AIRequest... aiRequests) {
                            final AIRequest request = aiRequests[0];
                            try {
                                final AIResponse response = aiDataService.request(aiRequest);
                                return response;
                            } catch (AIServiceException e) {
                            }
                            return null;
                        }
                        @Override
                        protected void onPostExecute(AIResponse response) {
                            if (response != null) {

                                Result result = response.getResult();
                                String reply = result.getFulfillment().getSpeech();
                                char replypunc = reply.charAt(reply.length()-1);
                                reply = reply.substring(0, reply.length()-1);
                                ChatMessage chatMessage = new ChatMessage(reply + ", " + name + replypunc, "bot");
                                ref.child("Devices").child(android_id).child("chat").push().setValue(chatMessage);
                            }
                        }
                    }.execute(aiRequest);
                }
                else {
                    aiService.startListening();
                }

                editText.setText("");

            }
        });
    }

}
