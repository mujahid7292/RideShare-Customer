package com.sand_corporation.www.uthao.BackGroundJob;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.fabric.sdk.android.Fabric;

import static android.support.v4.app.ActivityCompat.requestPermissions;

/**
 * Created by HP on 12/19/2017.
 */

public class FireBaseJobDispatcherBackGroundJob extends JobService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{
    //This extended JobService is from firebase.jobdispatcher.JobService

    private static final int MY_FINE_LOCATION_PERMISSION = 200;
    private String customerUID;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location mLastLocation;
    private FirebaseFirestore mFireCustomerBackGroundDbRef;


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters job) {
        //This onStartJob() method will run on main thread. If job is big it will freeze the main
        //thread. So we have to run big job in separate thread. We could use AsyncTask to run our job in
        //separate thread. If we run our job in separate thread than we have to return true from
        //this method.
        customerUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mFireCustomerBackGroundDbRef = FirebaseFirestore.getInstance();
        buildGoogleApiClient();
        jobFinished(job,false);
        //If we run our long running task into separate thread than we have to call jobFinished() method,
        //after completion of our long running back ground task. If we don't call this method system will
        //assume some where in back ground there is task going on. This will drain our battery.

        //We have supplied second argument as false. If we wanted to reschedule the same job again
        //we would put true as second argument.

        return true;
    }

    private void updateBackGroundLocationToServer(Location mLastLocation, String customerUID) {
        double latitude = mLastLocation.getLatitude();
        double longitude = mLastLocation.getLongitude();
        SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());

        Log.i("LOG","latitude " + latitude +" longitude "+ longitude);

        HashMap map = new HashMap<>();
        map.put("BackGroundLocationDate",date);
        map.put("BackGroundLatitude",latitude);
        map.put("BackGroundLongitude",longitude);

        DatabaseReference mCustomerBackGroundDbRef = FirebaseDatabase.getInstance()
                .getReference("Users/Customers")
                .child(customerUID)
                .child("Customer_BackGround_Location");
        mCustomerBackGroundDbRef.updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            FirebaseCrash.log("FireBaseJobDispatcherBackGroundJob:RealTimeDB: success");
                            Crashlytics.log("FireBaseJobDispatcherBackGroundJob:RealTimeDB: success");
                        } else {
                            FirebaseCrash.log("FireBaseJobDispatcherBackGroundJob:RealTimeDB: failed");
                            Crashlytics.log("FireBaseJobDispatcherBackGroundJob:RealTimeDB: failed");
                        }
                    }
                });

        HashMap<String, Object> fireMap = new HashMap<>();
        fireMap.put("BackGroundLocationDate",date);
        fireMap.put("BackGroundLatitude",latitude);
        fireMap.put("BackGroundLongitude",longitude);

        mFireCustomerBackGroundDbRef
                .collection("Users")
                .document("Customers")
                .collection(customerUID)
                .document("Customer_BackGround_Location")
                .set(fireMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            FirebaseCrash.log("FireBaseJobDispatcherBackGroundJob:FireStoreDB: success");
                            Crashlytics.log("FireBaseJobDispatcherBackGroundJob:FireStoreDB: success");
                        } else {
                            FirebaseCrash.log("FireBaseJobDispatcherBackGroundJob:FireStoreDB: failed");
                            Crashlytics.log("FireBaseJobDispatcherBackGroundJob:FireStoreDB: failed");
                        }
                    }
                });


        if (googleApiClient.isConnected()){
            Log.i("LOG","CONNECTED onLocationChanged");
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        //If your job terminate before completion, than system will call this onStopJob() method.
        //if you want to reschedule the failed job, than you have to return true from this method.
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        updateBackGroundLocationToServer(mLastLocation,customerUID);

    }


    protected synchronized void buildGoogleApiClient() {
        FirebaseCrash.log("Home:buildGoogleApiClient.called");
        Crashlytics.log("Home:buildGoogleApiClient.called");
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)  //Very important // PlaceAutoComplete will not work
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        FirebaseCrash.log("Home:GoogleApiClient:onConnected.called");
        Crashlytics.log("Home:GoogleApiClient:onConnected.called");
        //This method will be called when googleApiClient will successfully connected.
        //After googleApiClient is connected, than we can call for location request.
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true); //this is the key ingredient

        checkIsLocationIsEnabled(builder,googleApiClient,locationRequest);


    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        googleApiClient.connect();
    }

    private void checkIsLocationIsEnabled(LocationSettingsRequest.Builder builder,
                                          final GoogleApiClient googleApiClient,
                                          final LocationRequest locationRequest) {
        FirebaseCrash.log("Home:checkIsLocationIsEnabled.called");
        Crashlytics.log("Home:checkIsLocationIsEnabled.called");
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.
                checkLocationSettings(this.googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        requestLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.

                        break;
                }
            }
        });
    }



    protected void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            if (googleApiClient.isConnected()){
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                        locationRequest, this);
            } else {
                Log.i("LOG","googleApiClientService is NOT connected");
                googleApiClient.connect();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions((Activity)
                        getApplicationContext(),new String[]
                        {android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_FINE_LOCATION_PERMISSION);
            }
        }

    }

    private boolean checkInternetConnection(){
        //Add permission in android manifest
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            //If the device is AirPlane mode networkInfo = null.
//            Log.i("LOG","Main Activity checkInternetConnection connection ok");
            return true;
        }else {
//            Log.i("LOG","Main Activity checkInternetConnection no connection");
            return false;
        }
    }
}
