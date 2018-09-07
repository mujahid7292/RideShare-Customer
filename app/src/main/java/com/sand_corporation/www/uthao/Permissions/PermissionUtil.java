package com.sand_corporation.www.uthao.Permissions;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.sand_corporation.www.uthao.R;

/**
 * Created by HP on 10/21/2017.
 */

public class PermissionUtil {

    private Context mContext;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PermissionUtil(Context context){
        this.mContext = context;
        sharedPreferences = mContext.getSharedPreferences
                (context.getString(R.string.permission_preference),
                Context.MODE_PRIVATE);
        editor = sharedPreferences.edit(); //We will not write 'editor.commit' here
    }

    public void updatePermissionPreference(String permission,Context mContext){
        switch (permission){

            case "phone_state":
                editor.putBoolean(mContext.getString(R.string.permission_read_phone_state),true);
                //editor.putBoolean("permission_read_phone_state", true);
                FirebaseCrash.log("PermissionsUtil: user allowed phone_state permissions");
                editor.commit();
                Log.i("Check","user allowed phone_state permissions");
                Log.i("Check","Permission status: \n" +
                        "phone_state: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_read_phone_state), false)+"\n"+
                        "external storage write: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_write_external_storage), false)+"\n"+
                        "contacts: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_contacts), false)+"\n"+
                        "fine location: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_fine_location), false)+"\n"+
                        "send sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_send_sms), false)+"\n"+
                        "read sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_read_sms), false)+"\n"+
                        "receive sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_receive_sms), false)+"\n");
                break;

            case "storage":
                editor.putBoolean(mContext.getString(R.string.permission_write_external_storage), true);
                //editor.putBoolean("permission_write_external_storage", true);
                FirebaseCrash.log("PermissionsUtil: user allowed external storage write permissions");
                editor.commit();
                Log.i("Check","user allowed external storage write permissions");
                Log.i("Check","Permission status: \n" +
                        "phone_state: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_read_phone_state), false)+"\n"+
                        "external storage write: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_write_external_storage), false)+"\n"+
                        "contacts: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_contacts), false)+"\n"+
                        "fine location: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_fine_location), false)+"\n"+
                        "send sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_send_sms), false)+"\n"+
                        "read sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_read_sms), false)+"\n"+
                        "receive sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_receive_sms), false)+"\n");
                break;

            case "read storage":
                editor.putBoolean(mContext.getString(R.string.permission_read_external_storage), true);
                //editor.putBoolean("permission_write_external_storage", true);
                FirebaseCrash.log("PermissionsUtil: user allowed external storage read permissions");
                editor.commit();
                Log.i("Check","user allowed external storage read permissions");
                Log.i("Check","Permission status: \n" +
                        "phone_state: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_read_phone_state), false)+"\n"+
                        "external storage write: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_write_external_storage), false)+"\n"+
                        "contacts: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_contacts), false)+"\n"+
                        "fine location: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_fine_location), false)+"\n"+
                        "send sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_send_sms), false)+"\n"+
                        "read sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_read_sms), false)+"\n"+
                        "receive sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_receive_sms), false)+"\n");
                break;

            case "contacts":
                editor.putBoolean(mContext.getString(R.string.permission_contacts), true);
                //editor.putBoolean("permission_contacts", true);
                FirebaseCrash.log("PermissionsUtil: user allowed contacts permissions");
                editor.commit();
                Log.i("Check","user allowed contacts permissions");
                Log.i("Check","Permission status: \n" +
                        "phone_state: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_read_phone_state), false)+"\n"+
                        "external storage write: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_write_external_storage), false)+"\n"+
                        "contacts: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_contacts), false)+"\n"+
                        "fine location: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_fine_location), false)+"\n"+
                        "send sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_send_sms), false)+"\n"+
                        "read sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_read_sms), false)+"\n"+
                        "receive sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_receive_sms), false)+"\n");
                break;

            case "fine location":
                editor.putBoolean(mContext.getString(R.string.permission_fine_location), true);
                //editor.putBoolean("permission_fine_location", true);
                FirebaseCrash.log("PermissionsUtil: user allowed fine location permissions");
                editor.commit();
                Log.i("Check","user allowed fine location permissions");
                Log.i("Check","Permission status: \n" +
                        "phone_state: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_read_phone_state), false)+"\n"+
                        "external storage write: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_write_external_storage), false)+"\n"+
                        "contacts: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_contacts), false)+"\n"+
                        "fine location: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_fine_location), false)+"\n"+
                        "send sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_send_sms), false)+"\n"+
                        "read sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_read_sms), false)+"\n"+
                        "receive sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_receive_sms), false)+"\n");
                break;

            case "send sms":
                editor.putBoolean(mContext.getString(R.string.permission_send_sms), true);
                //editor.putBoolean("permission_send_sms", true);
                FirebaseCrash.log("PermissionsUtil: user allowed send sms permissions");
                editor.commit();
                Log.i("Check","user allowed send sms permissions");
                Log.i("Check","Permission status: \n" +
                        "phone_state: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_read_phone_state), false)+"\n"+
                        "external storage write: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_write_external_storage), false)+"\n"+
                        "contacts: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_contacts), false)+"\n"+
                        "fine location: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_fine_location), false)+"\n"+
                        "send sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_send_sms), false)+"\n"+
                        "read sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_read_sms), false)+"\n"+
                        "receive sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_receive_sms), false)+"\n");
                break;

            case "read sms":
                editor.putBoolean(mContext.getString(R.string.permission_read_sms), true);
                //editor.putBoolean("permission_read_sms", true);
                FirebaseCrash.log("PermissionsUtil: user allowed read sms permissions");
                editor.commit();
                Log.i("Check","user allowed read sms permissions");
                Log.i("Check","Permission status: \n" +
                        "phone_state: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_read_phone_state), false)+"\n"+
                        "external storage write: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_write_external_storage), false)+"\n"+
                        "contacts: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_contacts), false)+"\n"+
                        "fine location: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_fine_location), false)+"\n"+
                        "send sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_send_sms), false)+"\n"+
                        "read sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_read_sms), false)+"\n"+
                        "receive sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_receive_sms), false)+"\n");
                break;

            case "receive sms":
                editor.putBoolean(mContext.getString(R.string.permission_receive_sms), true);
                //editor.putBoolean("permission_receive_sms", true);
                FirebaseCrash.log("PermissionsUtil: user allowed receive sms permissions");
                editor.commit();
                Log.i("Check","user allowed receive sms permissions");
                Log.i("Check","Permission status: \n" +
                        "phone_state: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_read_phone_state), false)+"\n"+
                        "external storage write: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_write_external_storage), false)+"\n"+
                        "contacts: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_contacts), false)+"\n"+
                        "fine location: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_fine_location), false)+"\n"+
                        "send sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_send_sms), false)+"\n"+
                        "read sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_read_sms), false)+"\n"+
                        "receive sms: " + sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_receive_sms), false)+"\n");
                break;

        }
    }

    public boolean checkPermissionPreference(String permission,Context mContext){
        boolean isAskForPermissionShownToUser = false;

        switch (permission){

            case "phone_state":
                isAskForPermissionShownToUser = sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_read_phone_state), false);
                //isShown = sharedPreferences.getBoolean("permission_read_phone_state", false);
                //this "false" argument is the default value. If this "PERMISSION_READ_PHONE_STATE"
                //does not exist than this default value "false" will be returned.
                break;

            case "storage":
                isAskForPermissionShownToUser = sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_write_external_storage), false);
                //isShown = sharedPreferences.getBoolean("permission_write_external_storage", false);
                //this "false" argument is the default value. If this "PERMISSION_WRITE_EXTERNAL_STORAGE"
                //does not exist than this default value "false" will be returned.
                break;

            case "read storage":
                isAskForPermissionShownToUser = sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_read_external_storage), false);
                //isShown = sharedPreferences.getBoolean("permission_write_external_storage", false);
                //this "false" argument is the default value. If this "PERMISSION_WRITE_EXTERNAL_STORAGE"
                //does not exist than this default value "false" will be returned.
                break;

            case "contacts":
                isAskForPermissionShownToUser = sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_contacts), false);
                //isShown = sharedPreferences.getBoolean("permission_contacts", false);
                //this "false" argument is the default value. If this "PERMISSION_CONTACTS"
                //does not exist than this default value "false" will be returned.
                break;

            case "fine location":
                isAskForPermissionShownToUser = sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_fine_location), false);
                //isShown = sharedPreferences.getBoolean("permission_fine_location", false);
                //this "false" argument is the default value. If this "PERMISSION_FINE_LOCATION"
                //does not exist than this default value "false" will be returned.
                break;

            case "send sms":
                isAskForPermissionShownToUser = sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_send_sms), false);
                //isShown = sharedPreferences.getBoolean("permission_send_sms", false);
                //this "false" argument is the default value. If this "PERMISSION_SEND_SMS"
                //does not exist than this default value "false" will be returned.
                break;

            case "read sms":
                isAskForPermissionShownToUser = sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_read_sms), false);
                //isShown = sharedPreferences.getBoolean("permission_read_sms", false);
                //this "false" argument is the default value. If this "PERMISSION_READ_SMS"
                //does not exist than this default value "false" will be returned.
                break;

            case "receive sms":
                isAskForPermissionShownToUser = sharedPreferences.getBoolean
                        (mContext.getString(R.string.permission_receive_sms), false);
                //isShown = sharedPreferences.getBoolean("permission_receive_sms", false);
                //this "false" argument is the default value. If this "PERMISSION_RECEIVE_SMS"
                //does not exist than this default value "false" will be returned.
                break;

        }
        return isAskForPermissionShownToUser;
    }

}
