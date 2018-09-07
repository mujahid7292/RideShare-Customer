package com.sand_corporation.www.uthao.FirebaseMessaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sand_corporation.www.uthao.CustomerNotificationSQLiteDB.CustomerNotificationDbHelper;
import com.sand_corporation.www.uthao.R;
import com.sand_corporation.www.uthao.Volley.MySingelTon;

import java.util.Calendar;
import java.util.Locale;

import static android.support.v4.app.NotificationCompat.STREAM_DEFAULT;

/**
 * Created by HP on 12/7/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String notification_type = remoteMessage.getData().get("type");
        Log.i("Check","notification_type: " +notification_type);
        switch (notification_type){

            case Configuration.NOTIFICATION_TYPE_MESSAGE:
                Log.i("Check","sendMessageNotification() called");
                sendMessageNotification(remoteMessage);
                break;

            case Configuration.NOTIFICATION_TYPE_IMAGE_WITH_MESSAGE:
                handleImageMessage(remoteMessage);
                break;

            case Configuration.NOTIFICATION_TYPE_WEB_URL:
                sendWebUrlNotification(remoteMessage);
                break;
        }
        //Both two method from below is important.


//        sendNotificationUsingCustomLayOut(remoteMessage);
    }

    private void sendWebUrlNotification(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0){
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
            String webUrl = remoteMessage.getData().get("webUrl");

            Intent intent = new Intent(this,NotificationWebViewActivity.class);
            intent.putExtra("title",title);
            intent.putExtra("message",message);
            intent.putExtra("webUrl",webUrl);

            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0,intent,PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                    "Default_Notification_Channel");
            builder.setContentTitle(title);
            builder.setContentText(message);
            builder.setAutoCancel(true);
            builder.setSmallIcon(getNotificationIcon(builder));
            //Sound
//            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, STREAM_DEFAULT);
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    STREAM_DEFAULT);
            //Vibration
            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
            //LED
            builder.setLights(Color.RED, 3000, 3000);
            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.notify(0,builder.build());
        }
    }

    private void sendMessageNotification(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
            Long date = getCurrentTimeStamp();
            Log.i("Check","title: " +title + "\n" +
            "message: " + message);

            Intent intent = new Intent(this,NotificationMsgActivity.class);
            intent.putExtra("title",title);
            intent.putExtra("message",message);

            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0,intent,PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                    "Default_Notification_Channel");
            builder.setContentTitle(title);
            builder.setContentText(message);
            builder.setAutoCancel(true);
            builder.setSmallIcon(getNotificationIcon(builder));
            //Sound
//            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, STREAM_DEFAULT);
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    STREAM_DEFAULT);
            //Vibration
            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
            //LED
            builder.setLights(Color.RED, 3000, 3000);
            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.notify(0,builder.build());


            CustomerNotificationDbHelper helper = new CustomerNotificationDbHelper(getBaseContext());
            SQLiteDatabase db = helper.getWritableDatabase();
            helper.saveNotificationToSQLiteDatabase(getDate(date),title,message,0,db);
            helper.close();
        }
    }

    private Long getCurrentTimeStamp() {
        Long time = System.currentTimeMillis()/1000;
        return time;
    }

    private String getDate(Long ride_end_time) {
        //First we will create an object from Calender.class As we are considering
        //Different time zone. This will give us customers local time zone.
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(ride_end_time * 1000);
        String date = DateFormat.format("dd-MM-yyyy hh:mm",calendar).toString();
        return date;

    }

    private void handleImageMessage(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0){
            final String title = remoteMessage.getData().get("title");
            final String subtext = remoteMessage.getData().get("subtext");
            final String message = remoteMessage.getData().get("message");
            String img_url = remoteMessage.getData().get("img_url");
            String webUrl = remoteMessage.getData().get("webUrl");
            Uri sound_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            //If we want to display server provided image with notification, than we have to
            //omit notification payload. In that case we have to send only data pay load.
            //Only data payload can display image in system notification's tray.
            Intent intent = null;
            if (remoteMessage.getNotification() != null){
                String click_action = remoteMessage.getNotification().getClickAction();
                intent = new Intent(click_action);
                intent.putExtra("click_action",click_action);
            } else {
                intent = new Intent(this,NotificationMsgActivity.class);
            }

            intent.putExtra("webUrl",webUrl);
            intent.putExtra("title",title);
            intent.putExtra("subtext",title);
            intent.putExtra("message",message);
            intent.putExtra("img_url",img_url);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            Log.i("Token","title: " + title + "\n" +
//                    "message: " + message + "\n" +
//                    "img_url: " + img_url + "\n" +
//                    "webUrl: " + webUrl  + "\n" +
//                    "click_action: " + click_action);

            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0,intent,PendingIntent.FLAG_ONE_SHOT);

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                    "Default_Notification_Channel");
            builder.setContentTitle(title);
            builder.setSubText(subtext);
            builder.setContentText(message);
            builder.setAutoCancel(true);
            builder.setSmallIcon(getNotificationIcon(builder));
//            builder.setSound(sound_uri);
            //Sound
//            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, STREAM_DEFAULT);
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    STREAM_DEFAULT);
            //Vibration
            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
            //LED
            builder.setLights(Color.RED, 3000, 3000);
            builder.setContentIntent(pendingIntent);

            ImageRequest imageRequest = new ImageRequest(img_url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    //here we will get the image from the server
                    builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(response));

                    //Now we can display the notification with image
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(0,builder.build());

                }
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            MySingelTon.getmInsatance(this).addToRequestQue(imageRequest);

        }
    }


    private void handleMessage(String message) {
        Log.i("Token","handleMessage: " + message);
        Intent pushNotification = new Intent(Configuration.NOTIFICATION_TYPE_WEB_URL);
        pushNotification.putExtra(Configuration.NOTIFICATION_TYPE_WEB_URL,message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            int color = 0x008000;
            notificationBuilder.setColor(Color.RED);
            notificationBuilder.setColorized(true);
            return R.drawable.ic_launcher;

        }
        return R.drawable.ic_launcher;
    }

    private void sendNotificationUsingCustomLayOut(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0){
            final String title = remoteMessage.getData().get("title");
            final String subtext = remoteMessage.getData().get("subtext");
            final String message = remoteMessage.getData().get("message");
            String img_url = remoteMessage.getData().get("img_url");
            String webUrl = remoteMessage.getData().get("webUrl");
            Uri sound_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            //If we want to display server provided image with notification, than we have to
            //omit notification payload. In that case we have to send only data pay load.
            //Only data payload can display image in system notification's tray.
            Intent intent = null;
            if (remoteMessage.getNotification() != null){
                String click_action = remoteMessage.getNotification().getClickAction();
                intent = new Intent(click_action);
                intent.putExtra("click_action",click_action);
            } else {
                intent = new Intent(this,NotificationMsgActivity.class);
            }

            intent.putExtra("webUrl",webUrl);
            intent.putExtra("title",title);
            intent.putExtra("subtext",title);
            intent.putExtra("message",message);
            intent.putExtra("img_url",img_url);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            Log.i("Token","title: " + title + "\n" +
//                    "message: " + message + "\n" +
//                    "img_url: " + img_url + "\n" +
//                    "webUrl: " + webUrl  + "\n" +
//                    "click_action: " + click_action);

            final RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.view_expanded_notification);
//        expandedView.setTextViewText(R.id.timestamp, DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME));
            expandedView.setTextViewText(R.id.notification_expanded_title, title);
            expandedView.setTextViewText(R.id.notification_expanded_subtext, subtext);
            expandedView.setTextViewText(R.id.notification_expanded_message, message);



            // adding action to left button
            Intent leftIntent = new Intent(this,
                    NotificationIntentService.class);
            leftIntent.setAction("left");
            expandedView.setOnClickPendingIntent(R.id.left_notification_button,
                    PendingIntent.getService(this, 0, leftIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT));

            // adding action to right button
            Intent rightIntent = new Intent(this, NotificationIntentService.class);
            rightIntent.setAction("right");
            expandedView.setOnClickPendingIntent(R.id.right_notification_button,
                    PendingIntent.getService(this, 1,
                            rightIntent, PendingIntent.FLAG_UPDATE_CURRENT));

            RemoteViews collapsedView = new RemoteViews(getPackageName(),
                    R.layout.view_collapsed_notification);
//        collapsedView.setTextViewText(R.id.timestamp, DateUtils.formatDateTime(this,
// System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME));
            collapsedView.setTextViewText(R.id.notification_collapsed_title,title);
            collapsedView.setTextViewText(R.id.notification_collapsed_subtext,subtext);

            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0,intent,PendingIntent.FLAG_ONE_SHOT);

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                    "Default_Notification_Channel");
            builder.setSmallIcon(getNotificationIcon(builder));
            builder.setContentTitle(title);
            builder.setContentText(message);
            builder.setSubText(subtext);
            builder.setAutoCancel(true);
            builder.setContentIntent(pendingIntent);
            builder.setCustomContentView(collapsedView);
            builder.setCustomBigContentView(expandedView);
            builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());


            ImageRequest imageRequest = new ImageRequest(img_url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    //here we will get the image from the server
                    expandedView.setImageViewBitmap(R.id.notification_img,response);
                    //Now we can display the notification with image
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(0,builder.build());

                }
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            MySingelTon.getmInsatance(this).addToRequestQue(imageRequest);

        }

    }



}
