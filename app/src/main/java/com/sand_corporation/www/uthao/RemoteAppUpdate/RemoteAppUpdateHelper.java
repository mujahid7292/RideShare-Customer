package com.sand_corporation.www.uthao.RemoteAppUpdate;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

/**
 * Created by HP on 12/13/2017.
 */

public class RemoteAppUpdateHelper {

    public static String KEY_IS_UPDATE_ENABLE = "is_update";
    public static String KEY_CURRENT_VERSION_IN_PLAY_STORE = "version";
    public static String KEY_GOOGLE_PLAY_URL = "update_url";

    public interface onUpdateCheckListener{
        //Class which will implement this interface, must implement this
        //below method.
        void onUpdateCheckListener(String appUrl);
    }

    //Below this one is method
    public static Builder with(Context context){
        //This method return type is 'Builder'
        return new Builder(context);
    }

    private Context context;
    private onUpdateCheckListener onUpdateCheckListener;

    //Below this one is constructor
    public RemoteAppUpdateHelper(Context context, RemoteAppUpdateHelper.onUpdateCheckListener onUpdateCheckListener) {
        this.context = context;
        this.onUpdateCheckListener = onUpdateCheckListener;
    }

    public void check(){
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        if (remoteConfig.getBoolean(KEY_IS_UPDATE_ENABLE)){
            String currentVersionInPlayStore = remoteConfig.getString(KEY_CURRENT_VERSION_IN_PLAY_STORE);
            String thisDeviceAppVersion = getAppVersion(context);
            String googlePlayUrl = remoteConfig.getString(KEY_GOOGLE_PLAY_URL);
            Log.i("Check","currentVersionInPlayStore: " +currentVersionInPlayStore +"\n" +
                                "googlePlayUrl: " + googlePlayUrl);
            if (!TextUtils.equals(currentVersionInPlayStore, thisDeviceAppVersion)
                    && onUpdateCheckListener != null){
                onUpdateCheckListener.onUpdateCheckListener(googlePlayUrl);
            }
        }

    }

    private String getAppVersion(Context context) {
        String thisDeviceAppVersion = "";

        try {
            thisDeviceAppVersion = context.getPackageManager().
                    getPackageInfo(context.getPackageName(),0).versionName;
            thisDeviceAppVersion = thisDeviceAppVersion.replaceAll("[a-zA-Z]|-","");
            Log.i("Check","thisDeviceAppVersion: " +thisDeviceAppVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return thisDeviceAppVersion;
    }

    public static class Builder{

        private Context context;
        private onUpdateCheckListener onUpdateCheckListener;

        //Below this one is constructor
        public Builder(Context context) {
            this.context = context;
        }

        //Below this one is method
        public Builder onUpdateCheck(onUpdateCheckListener onUpdateCheckListener){
            //This method return type is 'Builder'
            this.onUpdateCheckListener = onUpdateCheckListener;
            return this;
        }

        public RemoteAppUpdateHelper build(){
            return new RemoteAppUpdateHelper(context,onUpdateCheckListener);
        }

        public RemoteAppUpdateHelper check(){
            RemoteAppUpdateHelper updateHelper = build();
            updateHelper.check();

            return updateHelper;
        }
    }
}
