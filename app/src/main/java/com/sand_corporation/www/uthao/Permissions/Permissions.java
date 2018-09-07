package com.sand_corporation.www.uthao.Permissions;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by HP on 11/24/2017.
 */

public class Permissions extends AppCompatActivity{
    //Below those are the integer value we need at the time of requesting permission.
    private static final int Request_READ_PHONE_STATE = 1001;
    private static final int Request_WRITE_EXTERNAL_STORAGE = 1002;
    private static final int Request_READ_EXTERNAL_STORAGE = 1003;
    private static final int Request_CONTACTS = 1004;
    private static final int Request_FINE_LOCATION = 1005;
    private static final int Request_SEND_SMS = 1007;
    private static final int Request_READ_SMS = 1008;
    private static final int Request_RECEIVE_SMS = 1009;
    private static final int Request_GROUP_PERMISSION = 1425;

    //Those are the integer value we will use to identify which of the permission is called.
    private static final int TXT_READ_PHONE_STATE = 1;
    private static final int TXT_WRITE_EXTERNAL_STORAGE = 2;
    private static final int TXT_READ_EXTERNAL_STORAGE = 3;
    private static final int TXT_CONTACTS = 4;
    private static final int TXT_FINE_LOCATION = 5;
    private static final int TXT_SEND_SMS = 6;
    private static final int TXT_READ_SMS = 7;
    private static final int TXT_RECEIVE_SMS= 8;

    private PermissionUtil permissionUtil;

    public Permissions(Activity activity){
        permissionUtil = new PermissionUtil(activity);
    }

    //We will check whether permission is already granted or not
    private int checkPermission (int permission, Activity activity){

        //First we assume that permission is not granted. Then we check in switch statement.
        int permission_status = PackageManager.PERMISSION_DENIED;

        switch (permission){

            case TXT_READ_PHONE_STATE:
                permission_status = ContextCompat.checkSelfPermission(activity,
                        android.Manifest.permission.READ_PHONE_STATE);
                //If permission is granted than, this above line would be like below.
                //permission_status = PackageManager.PERMISSION_GRANTED;
                break;

            case TXT_WRITE_EXTERNAL_STORAGE:
                permission_status = ContextCompat.checkSelfPermission
                        (activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                //If permission is granted than, this above line would be like below.
                //permission_status = PackageManager.PERMISSION_GRANTED;
                break;

            case TXT_READ_EXTERNAL_STORAGE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    permission_status = ContextCompat.checkSelfPermission
                            (activity, android.Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                //If permission is granted than, this above line would be like below.
                //permission_status = PackageManager.PERMISSION_GRANTED;
                break;

            case TXT_CONTACTS:
                permission_status = ContextCompat.checkSelfPermission
                        (activity, android.Manifest.permission.READ_CONTACTS);
                //If permission is granted than, this above line would be like below.
                //permission_status = PackageManager.PERMISSION_GRANTED;
                break;

            case TXT_FINE_LOCATION:
                permission_status = ContextCompat.checkSelfPermission
                        (activity, android.Manifest.permission.ACCESS_FINE_LOCATION);
                //If permission is granted than, this above line would be like below.
                //permission_status = PackageManager.PERMISSION_GRANTED;
                break;

            case TXT_SEND_SMS:
                permission_status = ContextCompat.checkSelfPermission
                        (activity, android.Manifest.permission.SEND_SMS);
                //If permission is granted than, this above line would be like below.
                //permission_status = PackageManager.PERMISSION_GRANTED;
                break;
            case TXT_READ_SMS:
                permission_status = ContextCompat.checkSelfPermission
                        (activity, android.Manifest.permission.READ_SMS);
                //If permission is granted than, this above line would be like below.
                //permission_status = PackageManager.PERMISSION_GRANTED;
                break;
            case TXT_RECEIVE_SMS:
                permission_status = ContextCompat.checkSelfPermission
                        (activity, android.Manifest.permission.RECEIVE_SMS);
                //If permission is granted than, this above line would be like below.
                //permission_status = PackageManager.PERMISSION_GRANTED;
                break;
        }
        return permission_status;
    }

    //We will request a new permission by below method
    private void requestPermission (int permission, Activity activity){

        switch (permission){

            case TXT_READ_PHONE_STATE:
                ActivityCompat.requestPermissions((Activity) activity,
                        new String[] {Manifest.permission.READ_PHONE_STATE},
                        Request_READ_PHONE_STATE);
                break;

            case TXT_WRITE_EXTERNAL_STORAGE:
                ActivityCompat.requestPermissions((Activity) activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Request_WRITE_EXTERNAL_STORAGE);
                break;

            case TXT_READ_EXTERNAL_STORAGE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityCompat.requestPermissions((Activity) activity,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            Request_READ_EXTERNAL_STORAGE);
                }
                break;

            case TXT_CONTACTS:
                ActivityCompat.requestPermissions((Activity) activity,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        Request_CONTACTS);
                break;
            case TXT_FINE_LOCATION:
                ActivityCompat.requestPermissions((Activity) activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Request_FINE_LOCATION);
                break;

            case TXT_SEND_SMS:
                ActivityCompat.requestPermissions((Activity) activity,
                        new String[]{Manifest.permission.SEND_SMS},
                        Request_SEND_SMS);
                break;
            case TXT_READ_SMS:
                ActivityCompat.requestPermissions((Activity) activity,
                        new String[]{Manifest.permission.READ_SMS},
                        Request_READ_SMS);
                break;
            case TXT_RECEIVE_SMS:
                ActivityCompat.requestPermissions((Activity) activity,
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        Request_RECEIVE_SMS);
                break;
        }
    }

    //Display permission explanation to the user through alertDialog.builder() method
    private void showPermissionExplanation(final int permission, final Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        switch (permission){

            case TXT_READ_PHONE_STATE:
                builder.setMessage("For running this App we need to access your phone state." +
                        "\nPlease allow PHONE_STATE permission.");
                builder.setTitle("PHONE_STATE Permission");
                break;

            case TXT_WRITE_EXTERNAL_STORAGE:
                builder.setMessage("For running this App we need to write in your SD Card." +
                        "\nPlease allow SD Card write permission.");
                builder.setTitle("SD Card Permission");
                break;

            case TXT_READ_EXTERNAL_STORAGE:
                builder.setMessage("For running this App we need to write in your SD Card." +
                        "\nPlease allow SD Card write permission.");
                builder.setTitle("SD Card Permission");
                break;

            case TXT_CONTACTS:
                builder.setMessage("We will send sms from our server for verification " +
                        "of this device.\n" +
                        "we have to know the received sms contact number." +
                        "\nPlease allow contacts permission.");
                builder.setTitle("Contacts Permission");
                break;
            case TXT_FINE_LOCATION:
                builder.setMessage("For running this App we need to access your exact location." +
                        "\nPlease allow Location permission.");
                builder.setTitle("Location Permission");
                break;

            case TXT_SEND_SMS:
                builder.setMessage("For running this App we need to access your sms." +
                        "\nPlease allow sms permission.");
                builder.setTitle("SMS Permission");
                break;
            case TXT_READ_SMS:
                builder.setMessage("For running this App we need to access your sms." +
                        "\nPlease allow sms permission.");
                builder.setTitle("SMS Permission");
                break;
            case TXT_RECEIVE_SMS:
                builder.setMessage("For running this App we need to access your sms." +
                        "\nPlease allow sms permission.");
                builder.setTitle("SMS Permission");
                break;
        }


        builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                switch (permission){

                    case TXT_READ_PHONE_STATE:
                        requestPermission(TXT_READ_PHONE_STATE, activity);
                        break;

                    case TXT_WRITE_EXTERNAL_STORAGE:
                        requestPermission(TXT_WRITE_EXTERNAL_STORAGE, activity);
                        break;

                    case TXT_READ_EXTERNAL_STORAGE:
                        requestPermission(TXT_READ_EXTERNAL_STORAGE, activity);
                        break;

                    case TXT_CONTACTS:
                        requestPermission(TXT_CONTACTS, activity);
                        break;

                    case TXT_FINE_LOCATION:
                        requestPermission(TXT_FINE_LOCATION, activity);
                        break;


                    case TXT_SEND_SMS:
                        requestPermission(TXT_SEND_SMS, activity);
                        break;

                    case TXT_READ_SMS:
                        requestPermission(TXT_READ_SMS, activity);
                        break;

                    case TXT_RECEIVE_SMS:
                        requestPermission(TXT_RECEIVE_SMS, activity);
                        break;
                }
            }
        });

        builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Request all the permission at once.
    private void requestGroupPermission(ArrayList<String> permissions, Activity activity){
        String[]permissionList = new String[permissions.size()];
        permissions.toArray(permissionList);
        Log.i("Check","Permission List Size: " + permissions.size());
        if (permissions.size() >= 1){
            ActivityCompat.requestPermissions(activity,permissionList,
                    Request_GROUP_PERMISSION);
        }

    }

    //*************************************************************
    //*****************All the single permission*******************
    //***************************START*****************************

    private void checkAndRequestToReadPhoneState(final Activity activity) {
        if (checkPermission(TXT_READ_PHONE_STATE, activity) != PackageManager.PERMISSION_GRANTED){
            //This if statement will return true. If permission state == PERMISSION_DENIED.
            //Next we will check, if the permission was requested before. That means if
            //Permission was requested before, user was uncomfortable with giving permission.
            //Below we will check that
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) activity,
                    Manifest.permission.READ_PHONE_STATE)){
                //if above if statement return true, that means permission was requested before. So execution will
                //enter in this block of code. Here we will show rational explanation to user, about why we
                //need that permission.
                showPermissionExplanation(TXT_READ_PHONE_STATE, activity);
            } else if (!permissionUtil.checkPermissionPreference("phone_state",activity)){
                //Here above permissionUtil.checkPermissionPreference() method will return boolean result
                //By putting ! before this method we are changing the boolean result. That means if this
                //method return false, then we will change it to true. So this execution will enter this block
                //of code if only permissionUtil.checkPermissionPreference() method return false

                //Here we are checking if the permission is asking for the first time.
                requestPermission(TXT_READ_PHONE_STATE, activity);
                //Than update the PermissionUtil class about this recent permission.
                permissionUtil.updatePermissionPreference("phone_state",activity);
                //So after this update we will be able to know that this permission was asked before.
            } else {
                //If user select "Don't ask me again" in the system generated permission dialogue box.
                // we handle this situation here.
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Please Allow phone_state Permission In Your Application Setting");
                builder.setTitle("Allow phone_state Permission");
                builder.setPositiveButton("Go To App Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Here we open Application setting page.
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",activity.getPackageName(),null);
                        intent.setData(uri);
                        activity.startActivity(intent);
                    }
                });
                builder.setNegativeButton("Don't Allow Permission", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        } else {
            //If the permission is already granted before execution will jump to this block of code.
            Toast.makeText(activity,"You Have Camera Permission",Toast.LENGTH_LONG).show();

        }
    }

    private void checkAndRequestWrightToExternalStorage(final Activity activity) {
        if (checkPermission(TXT_WRITE_EXTERNAL_STORAGE,activity)
                != PackageManager.PERMISSION_GRANTED){
            //This if statement will return true. If permission state == PERMISSION_DENIED.
            //Next we will check, if the permission was requested before. That means if
            //Permission was requested before, user was uncomfortable with giving permission.
            //Below we will check that
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                //if above if statement return true, that means permission was requested before. So execution will
                //enter in this block of code. Here we will show rational explanation to user, about why we
                //need that permission.
                showPermissionExplanation(TXT_WRITE_EXTERNAL_STORAGE,activity);
            } else if (!permissionUtil.checkPermissionPreference("storage",activity)){
                //Here above permissionUtil.checkPermissionPreference() method will return boolean result
                //By putting ! before this method we are changing the boolean result. That means if this
                //method return false, then we will change it to true. So this execution will enter this block
                //of code if only permissionUtil.checkPermissionPreference() method return false

                //Here we are checking if the permission is asking for the first time.
                requestPermission(TXT_WRITE_EXTERNAL_STORAGE,activity);
                //Than update the PermissionUtil class about this recent permission.
                permissionUtil.updatePermissionPreference("storage",activity);
                //So after this update we will be able to know that this permission was asked before.
            } else {
                //If user select "Don't ask me again" in the system generated permission dialogue box.
                // we handle this situation here.
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Please Allow Storage Permission In Your Application Setting");
                builder.setTitle("Allow Storage Permission");
                builder.setPositiveButton("Go To App Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Here we open Application setting page.
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",activity.getPackageName(),null);
                        intent.setData(uri);
                        activity.startActivity(intent);
                    }
                });
                builder.setNegativeButton("Don't Allow Permission", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        } else {
            //If the permission is already granted before execution will jump to this block of code.
            Toast.makeText(activity,"You Have Storage Permission",Toast.LENGTH_LONG).show();

        }

    }

    public void checkAndRequestReadFromExternalStorage(final Activity activity) {
        if (checkPermission(TXT_READ_EXTERNAL_STORAGE,activity)
                != PackageManager.PERMISSION_GRANTED){
            //This if statement will return true. If permission state == PERMISSION_DENIED.
            //Next we will check, if the permission was requested before. That means if
            //Permission was requested before, user was uncomfortable with giving permission.
            //Below we will check that
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                //if above if statement return true, that means permission was requested before. So execution will
                //enter in this block of code. Here we will show rational explanation to user, about why we
                //need that permission.
                showPermissionExplanation(TXT_READ_EXTERNAL_STORAGE,activity);
            } else if (!permissionUtil.checkPermissionPreference("read storage",activity)){
                //Here above permissionUtil.checkPermissionPreference() method will return boolean result
                //By putting ! before this method we are changing the boolean result. That means if this
                //method return false, then we will change it to true. So this execution will enter this block
                //of code if only permissionUtil.checkPermissionPreference() method return false

                //Here we are checking if the permission is asking for the first time.
                requestPermission(TXT_READ_EXTERNAL_STORAGE,activity);
                //Than update the PermissionUtil class about this recent permission.
                permissionUtil.updatePermissionPreference("read storage",activity);
                //So after this update we will be able to know that this permission was asked before.
            } else {
                //If user select "Don't ask me again" in the system generated permission dialogue box.
                // we handle this situation here.
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Please Allow Storage Permission In Your Application Setting");
                builder.setTitle("Allow Storage Permission");
                builder.setPositiveButton("Go To App Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Here we open Application setting page.
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",activity.getPackageName(),null);
                        intent.setData(uri);
                        activity.startActivity(intent);
                    }
                });
                builder.setNegativeButton("Don't Allow Permission", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        } else {
            //If the permission is already granted before execution will jump to this block of code.
            Toast.makeText(activity,"You Have Storage Permission",Toast.LENGTH_LONG).show();

        }

    }

    public void checkAndRequestreadContacts(final Activity activity) {
        if (checkPermission(TXT_CONTACTS, activity) != PackageManager.PERMISSION_GRANTED){
            //This if statement will return true. If permission state == PERMISSION_DENIED.
            //Next we will check, if the permission was requested before. That means if
            //Permission was requested before, user was uncomfortable with giving permission.
            //Below we will check that
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) activity,
                    Manifest.permission.READ_CONTACTS)){
                //if above if statement return true, that means permission was requested before. So execution will
                //enter in this block of code. Here we will show rational explanation to user, about why we
                //need that permission.
                showPermissionExplanation(TXT_CONTACTS, activity);
            } else if (!permissionUtil.checkPermissionPreference("contacts",activity)){
                //Here above permissionUtil.checkPermissionPreference() method will return boolean result
                //By putting ! before this method we are changing the boolean result. That means if this
                //method return false, then we will change it to true. So this execution will enter this block
                //of code if only permissionUtil.checkPermissionPreference() method return false

                //Here we are checking if the permission is asking for the first time.
                requestPermission(TXT_CONTACTS, activity);
                //Than update the PermissionUtil class about this recent permission.
                permissionUtil.updatePermissionPreference("contacts",activity);
                //So after this update we will be able to know that this permission was asked before.
            } else {
                //If user select "Don't ask me again" in the system generated permission dialogue box.
                // we handle this situation here.
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Please Allow Permission to read Phone Contacts In Your Application Setting");
                builder.setTitle("Allow Permission to read Phone Contacts");
                builder.setPositiveButton("Go To App Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Here we open Application setting page.
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",activity.getPackageName(),null);
                        intent.setData(uri);
                        activity.startActivity(intent);
                    }
                });
                builder.setNegativeButton("Don't Allow Permission", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        } else {
            //If the permission is already granted before execution will jump to this block of code.
            Toast.makeText(activity,"You Have Permission to read Phone Contacts",Toast.LENGTH_LONG).show();

        }

    }

    private void checkAndRequestFineLocation(final Activity activity) {
        if (checkPermission(TXT_FINE_LOCATION, activity) != PackageManager.PERMISSION_GRANTED){
            //This if statement will return true. If permission state == PERMISSION_DENIED.
            //Next we will check, if the permission was requested before. That means if
            //Permission was requested before, user was uncomfortable with giving permission.
            //Below we will check that
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                //if above if statement return true, that means permission was requested before. So execution will
                //enter in this block of code. Here we will show rational explanation to user, about why we
                //need that permission.
                showPermissionExplanation(TXT_FINE_LOCATION, activity);
            } else if (!permissionUtil.checkPermissionPreference("fine location",activity)){
                //Here above permissionUtil.checkPermissionPreference() method will return boolean result
                //By putting ! before this method we are changing the boolean result. That means if this
                //method return false, then we will change it to true. So this execution will enter this block
                //of code if only permissionUtil.checkPermissionPreference() method return false

                //Here we are checking if the permission is asking for the first time.
                requestPermission(TXT_FINE_LOCATION, activity);
                //Than update the PermissionUtil class about this recent permission.
                permissionUtil.updatePermissionPreference("fine location",activity);
                //So after this update we will be able to know that this permission was asked before.
            } else {
                //If user select "Don't ask me again" in the system generated permission dialogue box.
                // we handle this situation here.
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Please Allow Permission To Access This Device Location In Your Application Setting");
                builder.setTitle("Allow Permission Of Device Location");
                builder.setPositiveButton("Go To App Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Here we open Application setting page.
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",activity.getPackageName(),null);
                        intent.setData(uri);
                        activity.startActivity(intent);
                    }
                });
                builder.setNegativeButton("Don't Allow Permission", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        } else {
            //If the permission is already granted before execution will jump to this block of code.
            Toast.makeText(activity,"You Have fine location Permission",Toast.LENGTH_LONG).show();

        }

    }


    private void checkAndRequestSendSMS(final Activity activity) {
        if (checkPermission(TXT_SEND_SMS, activity) != PackageManager.PERMISSION_GRANTED){
            //This if statement will return true. If permission state == PERMISSION_DENIED.
            //Next we will check, if the permission was requested before. That means if
            //Permission was requested before, user was uncomfortable with giving permission.
            //Below we will check that
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) activity,
                    Manifest.permission.SEND_SMS)){
                //if above if statement return true, that means permission was requested before. So execution will
                //enter in this block of code. Here we will show rational explanation to user, about why we
                //need that permission.
                showPermissionExplanation(TXT_SEND_SMS, activity);
            } else if (!permissionUtil.checkPermissionPreference("send sms",activity)){
                //Here above permissionUtil.checkPermissionPreference() method will return boolean result
                //By putting ! before this method we are changing the boolean result. That means if this
                //method return false, then we will change it to true. So this execution will enter this block
                //of code if only permissionUtil.checkPermissionPreference() method return false

                //Here we are checking if the permission is asking for the first time.
                requestPermission(TXT_SEND_SMS, activity);
                //Than update the PermissionUtil class about this recent permission.
                permissionUtil.updatePermissionPreference("send sms",activity);
                //So after this update we will be able to know that this permission was asked before.
            } else {
                //If user select "Don't ask me again" in the system generated permission dialogue box.
                // we handle this situation here.
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Please Allow Permission To Access This Device SMS In Your Application Setting");
                builder.setTitle("Allow SMS Permission");
                builder.setPositiveButton("Go To App Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Here we open Application setting page.
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",activity.getPackageName(),null);
                        intent.setData(uri);
                        activity.startActivity(intent);
                    }
                });
                builder.setNegativeButton("Don't Allow Permission", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        } else {
            //If the permission is already granted before execution will jump to this block of code.
            Toast.makeText(activity,"You Have SMS Send Permission",Toast.LENGTH_LONG).show();

        }

    }

    private void checkAndRequestReadSMS(final Activity activity) {
        if (checkPermission(TXT_READ_SMS, activity) != PackageManager.PERMISSION_GRANTED){
            //This if statement will return true. If permission state == PERMISSION_DENIED.
            //Next we will check, if the permission was requested before. That means if
            //Permission was requested before, user was uncomfortable with giving permission.
            //Below we will check that
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) activity,
                    Manifest.permission.READ_SMS)){
                //if above if statement return true, that means permission was requested before. So execution will
                //enter in this block of code. Here we will show rational explanation to user, about why we
                //need that permission.
                showPermissionExplanation(TXT_READ_SMS, activity);
            } else if (!permissionUtil.checkPermissionPreference("read sms",activity)){
                //Here above permissionUtil.checkPermissionPreference() method will return boolean result
                //By putting ! before this method we are changing the boolean result. That means if this
                //method return false, then we will change it to true. So this execution will enter this block
                //of code if only permissionUtil.checkPermissionPreference() method return false

                //Here we are checking if the permission is asking for the first time.
                requestPermission(TXT_READ_SMS, activity);
                //Than update the PermissionUtil class about this recent permission.
                permissionUtil.updatePermissionPreference("read sms",activity);
                //So after this update we will be able to know that this permission was asked before.
            } else {
                //If user select "Don't ask me again" in the system generated permission dialogue box.
                // we handle this situation here.
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Please Allow Permission To Access This Device SMS In Your Application Setting");
                builder.setTitle("Allow SMS Permission");
                builder.setPositiveButton("Go To App Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Here we open Application setting page.
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",activity.getPackageName(),null);
                        intent.setData(uri);
                        activity.startActivity(intent);
                    }
                });
                builder.setNegativeButton("Don't Allow Permission", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        } else {
            //If the permission is already granted before execution will jump to this block of code.
            Toast.makeText(activity,"You Have SMS Read Permission",Toast.LENGTH_LONG).show();

        }

    }

    private void checkAndRequestReceiveSMS(final Activity activity) {
        if (checkPermission(TXT_RECEIVE_SMS, activity) != PackageManager.PERMISSION_GRANTED){
            //This if statement will return true. If permission state == PERMISSION_DENIED.
            //Next we will check, if the permission was requested before. That means if
            //Permission was requested before, user was uncomfortable with giving permission.
            //Below we will check that
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.RECEIVE_SMS)){
                //if above if statement return true, that means permission was requested before. So execution will
                //enter in this block of code. Here we will show rational explanation to user, about why we
                //need that permission.
                showPermissionExplanation(TXT_RECEIVE_SMS, activity);
            } else if (!permissionUtil.checkPermissionPreference("receive sms",activity)){
                //Here above permissionUtil.checkPermissionPreference() method will return boolean result
                //By putting ! before this method we are changing the boolean result. That means if this
                //method return false, then we will change it to true. So this execution will enter this block
                //of code if only permissionUtil.checkPermissionPreference() method return false

                //Here we are checking if the permission is asking for the first time.
                requestPermission(TXT_RECEIVE_SMS, activity);
                //Than update the PermissionUtil class about this recent permission.
                permissionUtil.updatePermissionPreference("receive sms",activity);
                //So after this update we will be able to know that this permission was asked before.
            } else {
                //If user select "Don't ask me again" in the system generated permission dialogue box.
                // we handle this situation here.
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Please Allow Permission To Access This Device SMS In Your Application Setting");
                builder.setTitle("Allow SMS Permission");
                builder.setPositiveButton("Go To App Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Here we open Application setting page.
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",activity.getPackageName(),null);
                        intent.setData(uri);
                        activity.startActivity(intent);
                    }
                });
                builder.setNegativeButton("Don't Allow Permission", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        } else {
            //If the permission is already granted before execution will jump to this block of code.
            Toast.makeText(activity,"You Have SMS Receive Permission",Toast.LENGTH_LONG).show();

        }

    }


    //*************************************************************
    //*****************All the single permission*******************
    //***************************END*****************************


    //*************************************************************
    //*****************All permission By Category*******************
    //***************************START*****************************

    public void requestAllPermission(Activity activity) {
        //In permissionNeeded Arraylist we will put the permission for which we did not get user
        //approval yet.
        ArrayList<String> permissionNeeded = new ArrayList<>();
        //In permissionsAvailable Arraylist we will put all the permissions which
        // is required by this application
        ArrayList<String> permissionsAvailable = new ArrayList<>();
        permissionsAvailable.add(android.Manifest.permission.READ_PHONE_STATE);
        permissionsAvailable.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsAvailable.add(android.Manifest.permission.SEND_SMS);
        permissionsAvailable.add(android.Manifest.permission.READ_SMS);
        permissionsAvailable.add(android.Manifest.permission.RECEIVE_SMS);

        //Above we did not added camera permission. We will ask for camera permission
        //to user when we truly need it. Otherwise user will get angry.

        for (String permission : permissionsAvailable){
            if (ContextCompat.checkSelfPermission(activity,permission) !=
                    PackageManager.PERMISSION_GRANTED){
                //We will check each permission in permissionsAvailable Arraylist, whether user already
                //approved the permission or not.
                //Execution will come to this block if user did not approved the permission. So we will
                //add this specefic permission in our permissionNeeded Arraylist.
                permissionNeeded.add(permission);
            }
        }
        requestGroupPermission(permissionNeeded, activity);
        Log.i("Check", "Permission Needed: " + permissionNeeded.toString());
    }

    public void storagePermissions(Activity activity){
        checkAndRequestWrightToExternalStorage(activity);
        checkAndRequestReadFromExternalStorage(activity);
    }

    public void phoneStatePermission(Activity activity){
        checkAndRequestToReadPhoneState(activity);
    }

    public void locationPermissions(Activity activity){
        checkAndRequestFineLocation(activity);
    }

    public void smsPermissions(Activity activity){
//        checkAndRequestReadSMS(activity);
        checkAndRequestReceiveSMS(activity);
//        checkAndRequestSendSMS(activity);
    }
    //*************************************************************
    //*****************All permission By Category*******************
    //***************************END*****************************


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case Request_READ_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have READ_PHONE_STATE Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,Phone_State : Success");
                } else {
                    //Here we we may put a counter, to calculate how may times a request is denied
//                    Toast.makeText(mContext,"You Don't Have READ_PHONE_STATE Permission",Toast.LENGTH_LONG).show();
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Permissions.this);
                    builder.setMessage("Without Device Phone State Permission This App Will Shut Down");
                    builder.setTitle("Allow Phone State Permission");
                    builder.setPositiveButton("Allow Phone State Permission", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(Permissions.this, new String[]
                                            {Manifest.permission.READ_PHONE_STATE},
                                    Request_READ_PHONE_STATE);
                        }
                    });
                    builder.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    });

                    android.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    Log.i("Check","onRequestPermissionsResult,Phone_State : Failed");
                }
                break;

            case Request_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have WRITE_EXTERNAL_STORAGE Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,WRITE_EXTERNAL_STORAGE : Success");
                } else {
                    //Here we we may put a counter, to calculate how may times a request is denied
//                    Toast.makeText(mContext,"You Don't Have WRITE_EXTERNAL_STORAGE Permission",Toast.LENGTH_LONG).show();
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Permissions.this);
                    builder.setMessage("Without Storage Permission This App Will Shut Down");
                    builder.setTitle("Allow Storage Permission");
                    builder.setPositiveButton("Allow Storage Permission", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(Permissions.this, new String[]
                                            {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    Request_WRITE_EXTERNAL_STORAGE);
                        }
                    });
                    builder.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    });

                    android.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    Log.i("Check","onRequestPermissionsResult,WRITE_EXTERNAL_STORAGE : Failed");
                }
                break;

            case Request_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[2] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have WRITE_EXTERNAL_STORAGE Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,WRITE_EXTERNAL_STORAGE : Success");
                } else {
                    //Here we we may put a counter, to calculate how may times a request is denied
//                    Toast.makeText(mContext,"You Don't Have WRITE_EXTERNAL_STORAGE Permission",Toast.LENGTH_LONG).show();
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Permissions.this);
                    builder.setMessage("Without Storage Permission This App Will Shut Down");
                    builder.setTitle("Allow Storage Permission");
                    builder.setPositiveButton("Allow Storage Permission", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                ActivityCompat.requestPermissions(Permissions.this, new String[]
                                                {Manifest.permission.READ_EXTERNAL_STORAGE},
                                        Request_READ_EXTERNAL_STORAGE);
                            }
                        }
                    });
                    builder.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    });

                    android.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    Log.i("Check","onRequestPermissionsResult,WRITE_EXTERNAL_STORAGE : Failed");
                }
                break;


            case Request_CONTACTS:
                if (grantResults.length > 0 && grantResults[3] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have Permission to read Phone Contacts",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,CONTACTS : Success");
                } else {
//                    Toast.makeText(mContext,"Contacts Permission is denied.Turn off Contacts module of the app",
//                            Toast.LENGTH_LONG).show();
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Permissions.this);
                    builder.setMessage("We will verify this device by accessing received sms phone number." +
                            "\nWithout Read Contact Permission This App Will Shut Down");
                    builder.setTitle("Allow Read Contact Permission");
                    builder.setPositiveButton("Allow Read Contact Permission", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(Permissions.this, new String[]
                                            {Manifest.permission.READ_CONTACTS},
                                    Request_CONTACTS);
                        }
                    });
                    builder.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    });

                    android.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    Log.i("Check","onRequestPermissionsResult,CONTACTS : Failed");
                }
                break;

            case Request_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[4] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have FINE_LOCATION Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,FINE_LOCATION : Success");
                } else {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Permissions.this);
                    builder.setMessage("Without Device Location Permission This App Will Shut Down");
                    builder.setTitle("Allow Device Location Permission");
                    builder.setPositiveButton("Allow Device Location Permission", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(Permissions.this, new String[]
                                            {Manifest.permission.ACCESS_FINE_LOCATION},
                                    Request_FINE_LOCATION);
                        }
                    });
                    builder.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    });

                    android.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    Log.i("Check","onRequestPermissionsResult,FINE_LOCATION : Failed");
                }
                break;


            case Request_SEND_SMS:
                if (grantResults.length > 0 && grantResults[5] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have SEND_SMS Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,SEND_SMS : Success");
                } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                    builder.setMessage("To run this app in your device we need access device sms. Without access to device sms" +
//                            " this app will not run!!!!\nPlease allow access to device sms permission in the next screen");
//                    builder.setTitle("Allow sms permission");
//                    builder.setPositiveButton("Allow sms permission", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            requestPermission(TXT_READ_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("read sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                            requestPermission(TXT_RECEIVE_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("receive sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                            requestPermission(TXT_SEND_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("send sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                        }
//                    });
//                    builder.setNegativeButton("Deny sms Permission", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                            finish();
//                        }
//                    });
//
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//                    Toast.makeText(mContext,"You Don't Have SEND_SMS Permission",Toast.LENGTH_LONG).show();
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Permissions.this);
                    builder.setMessage("Without SMS View Permission This App Will Shut Down");
                    builder.setTitle("Allow SMS Permission");
                    builder.setPositiveButton("Allow SMS Permission", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(Permissions.this, new String[]
                                            {Manifest.permission.SEND_SMS},
                                    Request_SEND_SMS);
                        }
                    });
                    builder.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    });

                    android.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    Log.i("Check","onRequestPermissionsResult,SEND_SMS : Failed");
                }
                break;

            case Request_READ_SMS:
                if (grantResults.length > 0 && grantResults[6] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have READ_SMS Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,READ_SMS : Success");
                } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                    builder.setMessage("To run this app in your device we need access device sms. Without access to device sms" +
//                            " this app will not run!!!!\nPlease allow access to device sms permission in the next screen");
//                    builder.setTitle("Allow sms permission");
//                    builder.setPositiveButton("Allow sms permission", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            requestPermission(TXT_READ_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("read sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                            requestPermission(TXT_RECEIVE_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("receive sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                            requestPermission(TXT_SEND_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("send sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                        }
//                    });
//                    builder.setNegativeButton("Deny sms Permission", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                            finish();
//                        }
//                    });
//
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//                    Toast.makeText(mContext,"You Don't Have READ_SMS Permission",Toast.LENGTH_LONG).show();
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Permissions.this);
                    builder.setMessage("Without Device Location Permission This App Will Shut Down");
                    builder.setTitle("Allow Device Location Permission");
                    builder.setPositiveButton("Allow Device Location Permission", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(Permissions.this, new String[]
                                            {Manifest.permission.READ_SMS},
                                    Request_READ_SMS);
                        }
                    });
                    builder.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    });

                    android.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    Log.i("Check","onRequestPermissionsResult,READ_SMS : Failed");
                }
                break;

            case Request_RECEIVE_SMS:
                if (grantResults.length > 0 && grantResults[7] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have RECEIVE_SMS Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,RECEIVE_SMS : Success");
                } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                    builder.setMessage("To run this app in your device we need access device sms. Without access to device sms" +
//                            " this app will not run!!!!\nPlease allow access to device sms permission in the next screen");
//                    builder.setTitle("Allow sms permission");
//                    builder.setPositiveButton("Allow sms permission", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            requestPermission(TXT_READ_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("read sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                            requestPermission(TXT_RECEIVE_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("receive sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                            requestPermission(TXT_SEND_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("send sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                        }
//                    });
//                    builder.setNegativeButton("Deny sms Permission", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                            finish();
//                        }
//                    });
//
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//                    Toast.makeText(mContext,"You Don't Have RECEIVE_SMS Permission",Toast.LENGTH_LONG).show();
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Permissions.this);
                    builder.setMessage("Without SMS View Permission This App Will Shut Down");
                    builder.setTitle("Allow SMS Permission");
                    builder.setPositiveButton("Allow SMS Permission", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(Permissions.this, new String[]
                                            {Manifest.permission.RECEIVE_SMS},
                                    Request_RECEIVE_SMS);
                        }
                    });
                    builder.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    });

                    android.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    Log.i("Check","onRequestPermissionsResult,RECEIVE_SMS : Failed");
                }
                break;

            case Request_GROUP_PERMISSION:
                String result = "";
                int i = 0;

                for (String perm : permissions){
                    String status = "";
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        status = "Granted";
                    } else {
                        status = "Denied";
                    }
                    result = result + "\n" + perm + " : " + status;

                    i++;
                }

                Log.i("Check", result);

//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                builder.setTitle("Group Permission Status");
//                builder.setMessage(result);
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
