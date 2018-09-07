package com.sand_corporation.www.uthao.RemoteAppUpdate;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP on 12/13/2017.
 */

public class App extends Application {

    //Write below this line in android manifest
    //android:name=".RemoteAppUpdate.App"

    //*******************************CAUTION*********************************************
    //*******************************CAUTION*********************************************
    //As there can only one 'name' attribute so we are copying our
    //onCreate() method to com.sand_corporation.www.uthao.LanguageChange.MainApplication

    //This App class is a wrapper class. By this class we will fetch update
    //from firebase remote config.


    @Override
    public void onCreate() {
        super.onCreate();

        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        //Default Value
        Map<String , Object> defaultValue = new HashMap<>();
        defaultValue.put(RemoteAppUpdateHelper.KEY_IS_UPDATE_ENABLE,false);
        defaultValue.put(RemoteAppUpdateHelper.KEY_CURRENT_VERSION_IN_PLAY_STORE,"1.1");
        defaultValue.put(RemoteAppUpdateHelper.KEY_GOOGLE_PLAY_URL,"your_app_url_in_playstore");

        remoteConfig.setDefaults(defaultValue);
        remoteConfig.fetch(5).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    remoteConfig.activateFetched();
                }
            }
        });
        //This fetch method will check for update for every 5 minutes.
    }


}
