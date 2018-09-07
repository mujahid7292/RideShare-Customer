package com.sand_corporation.www.uthao.FirebaseMessaging;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by HP on 12/8/2017.
 */

public class NotificationIntentService extends IntentService {

    //Must register this intent service in the manifest.
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public NotificationIntentService() {
        super("notificationIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        switch (intent.getAction()) {
            case "left":
                Handler leftHandler = new Handler(Looper.getMainLooper());
                leftHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "You clicked the left button",
                                Toast.LENGTH_LONG).show();
                        Log.i("Check","You clicked the left button");
                    }
                });
                break;
            case "right":
                Handler rightHandler = new Handler(Looper.getMainLooper());
                rightHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "You clicked the right button",
                                Toast.LENGTH_LONG).show();
                        Log.i("Check","You clicked the right button");
                    }
                });
                break;
        }
    }
}
