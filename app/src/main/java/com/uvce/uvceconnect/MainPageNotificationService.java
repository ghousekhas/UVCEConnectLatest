package com.uvce.uvceconnect;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MainPageNotificationService extends FirebaseMessagingService {

    Bitmap largeNotificaitonImage;
    String title, prev_title = " ";
    String body, prev_body = " ";
    String image="", prev_image="";
    Bitmap veryLargeNotificationImage;
    Intent intent;
    private final String PREFERENECE = "UVCE-prefereceFile";
    private SharedPreferences preference;
    final String Notification_Title = "Notification_Title", Notification_Body = "Notification_Body", Notification_Image = "Notification_Image";



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> message = remoteMessage.getData();
        title = message.get("title");
        body = message.get("body");
        if(message.containsKey("image"))
            image = message.get("image");
        intentSelector(message);
        getLogoImage();
        preference = getSharedPreferences(PREFERENECE, MODE_PRIVATE);
        sendNotification();
    }

    public void intentSelector(Map<String,String> message){
        if(message.containsKey("syllabus")) {
            intent = new Intent(getApplicationContext(), Academic.class);
            intent.putExtra("tab", 0);
        }
        else if(message.containsKey("question")){
            intent=new Intent(getApplicationContext(),Academic.class);
            intent.putExtra("tab",1);
        }
        else{
            intent=new Intent(getApplicationContext(),MainActivity.class);
        }

    }

    public void getLogoImage() {
        StorageReference imageref = FirebaseStorage.getInstance().getReference().child("logo/" +title+".jpg");
        FutureTarget<Bitmap> futureBitmap = GlideApp.with(getApplicationContext()).asBitmap().load(imageref).diskCacheStrategy(DiskCacheStrategy.NONE).submit();
        try {
            largeNotificaitonImage = futureBitmap.get();
        } catch (ExecutionException e) {
        } catch (InterruptedException e1) {
        }
    }

    public void sendNotification() {
        if(preference.contains(Notification_Title))
            prev_title = preference.getString(Notification_Title, " ");
        if(preference.contains(Notification_Body))
            prev_body = preference.getString(Notification_Body, " ");
        if(preference.contains(Notification_Image))
            prev_image = preference.getString(Notification_Image, " ");
        if(!(prev_title.equals(title) && prev_body.equals(body) && prev_image.equals(image))) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel("uvce_connect_main_channel", "UVCE CONNECT", NotificationManager.IMPORTANCE_HIGH);

                // Configure the notification channel.
                notificationChannel.setDescription("Notifications about college stuff");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.BLUE);
                notificationChannel.setVibrationPattern(new long[]{0, 700, 200, 700});
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder notificaitonBuilder = new NotificationCompat.Builder(this, "uvce_connect_main_channel");


            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            //Actual notification builder


            notificaitonBuilder.setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentInfo("Does this matter?")
                    .setLargeIcon(largeNotificaitonImage);

            if (!image.isEmpty()) {
                StorageReference imageref = FirebaseStorage.getInstance().getReference().child(image);
                FutureTarget<Bitmap> futureBitmap = GlideApp.with(getApplicationContext()).asBitmap().load(imageref).submit(700, 350);
                try {
                    veryLargeNotificationImage = futureBitmap.get();
                } catch (ExecutionException e) {
                } catch (InterruptedException e1) {
                }
                NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle()
                        .bigLargeIcon(largeNotificaitonImage)
                        .bigPicture(veryLargeNotificationImage);
                notificaitonBuilder.setStyle(style);
                futureBitmap.cancel(false);
            }


            notificationManager.notify((int) System.currentTimeMillis(), notificaitonBuilder.build());

            SharedPreferences.Editor editor = preference.edit();
            editor.putString(Notification_Title, title);
            editor.putString(Notification_Body, body);
            editor.putString(Notification_Image, image);
            editor.apply();

        }


    }
}




