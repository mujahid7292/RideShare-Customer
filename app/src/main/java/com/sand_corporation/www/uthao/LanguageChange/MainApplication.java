package com.sand_corporation.www.uthao.LanguageChange;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.sand_corporation.www.uthao.RemoteAppUpdate.RemoteAppUpdateHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP on 12/5/2017.
 */

public class MainApplication extends Application {

//    private RefWatcher refWatcher;
//    public static RefWatcher getRefWatcher(Context context) {
//        MainApplication application = (MainApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }



    //We have to add this class in our Manifest file

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocalHelper.onAttach(base,"en"));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //We have to enable initializeAppAutoUpdateChecker in production version
        initializeAppAutoUpdateChecker();

        //We have to disable leak cannery in production version
//        initializeLeakCanery();




    }

//    private void initializeLeakCanery() {
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        refWatcher = LeakCanary.install(this);
//    }

    private void initializeAppAutoUpdateChecker() {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
//
//        //Default Value
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
