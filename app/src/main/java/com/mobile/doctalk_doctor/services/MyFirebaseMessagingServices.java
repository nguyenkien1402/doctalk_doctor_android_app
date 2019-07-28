package com.mobile.doctalk_doctor.services;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mobile.doctalk_doctor.R;
import com.mobile.doctalk_doctor.activity.MainActivity;
import com.mobile.doctalk_doctor.activity.NotificationActivity;
import com.mobile.doctalk_doctor.utility.Config;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;

import java.util.Collections;
import java.util.Map;


public class MyFirebaseMessagingServices extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMS";
    private String message = "";

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            sendNotification(bitmap);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData() != null){
            getImage(remoteMessage);
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG,"Token: "+s);
    }

    private void sendNotification(Bitmap bitmap){

        Log.d(TAG,"Send notification");

        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.bigPicture(bitmap);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);



//        ActivityManager.RunningAppProcessInfo myProcessInfo = new ActivityManager.RunningAppProcessInfo();
//        ActivityManager.getMyMemoryState(myProcessInfo);
//        boolean isInBackground = myProcessInfo.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
//        if(isInBackground){
//            Log.d(TAG,"App is running in background");
//            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent , 0);
//            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//            String NOTIFICATION_CHANNEL_ID = "101";
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//
//                @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);
//
//                //Configure Notification Channel
//                notificationChannel.setDescription("Game Notifications");
//                notificationChannel.enableLights(true);
//                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//                notificationChannel.enableVibration(true);
//                notificationManager.createNotificationChannel(notificationChannel);
//            }
//
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
//                    .setSmallIcon(R.mipmap.ic_launcher_round)
//                    .setContentTitle(Config.title)
//                    .setAutoCancel(true)
//                    .setSound(defaultSound)
//                    .setContentText(Config.content)
//                    .setContentIntent(pendingIntent)
//                    .setStyle(style)
//                    .setLargeIcon(bitmap)
//                    .setWhen(System.currentTimeMillis())
//                    .setPriority(Notification.PRIORITY_MAX);
//
//            notificationManager.notify(1, notificationBuilder.build());
//        }else{
//            Log.d(TAG,"App is not running in background");
//            startActivity(intent);
//
//        }
    }

    private void getImage(final RemoteMessage remoteMessage) {
        try{
            Map<String, String> data = remoteMessage.getData();
            Config.id = Integer.parseInt(data.get("id"));
            Config.title = data.get("title");
            Config.content = data.get("content");
            Config.imageUrl = data.get("imageUrl");
            Config.gameUrl = data.get("gameUrl");
            JSONArray array = new JSONArray(data.get("userId"));
            for(int i = 0 ; i < array.length() ; i++){
                Config.userIds.add(array.getString(i));
            }
            //Create thread to fetch image from notification
            if(remoteMessage.getData()!=null){

                Handler uiHandler = new Handler(Looper.getMainLooper());
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Get image from data Notification
                        Picasso.get()
                                .load(Config.imageUrl)
                                .into(target);
                    }
                }) ;
            }
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }

    }
}
