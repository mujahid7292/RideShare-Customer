package com.sand_corporation.www.uthao.FirebaseMessaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by HP on 12/7/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    String customerUID;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String lattestToken = FirebaseInstanceId.getInstance().getToken();
        sendTokenToServer(lattestToken);
    }

    private void sendTokenToServer(String lattesToken) {
        //Save token to shared preferences.
        SharedPreferences preferences = getSharedPreferences("FCM_TOKEN",
                Context.MODE_PRIVATE);
        //Context.MODE_PRIVATE This means MyData will be accessible by only this app
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FCM_LATEST_TOKEN",lattesToken);
        editor.commit();

        Log.i("Token","LattesToken FireBase Instance Id Token: " + lattesToken);

    }
}
