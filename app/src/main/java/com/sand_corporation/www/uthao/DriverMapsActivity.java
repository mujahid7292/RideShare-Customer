package com.sand_corporation.www.uthao;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverMapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener ,RoutingListener{

    private static final int PERMISSION_REQUEST = 102;
    private Button DriverSettings, btnrideStatus;
    private int rideStatus = 0;
    //By above 'rideStatus' variable we will keep track of our driver ride status
    //Example: whether he picked up the customer, whether he dropped the customer.
    private LatLng customerDestinationLatLng;
    private String customerDestination;
    private LatLng customerpickUpLocationLatLng;
    private boolean gotAssignedCustomerPickupLocation = false;
    private boolean reachedToAssignedCustomerPickUpLocation = false;
    private boolean reachedToAssignedCustomerDestinationLocation = false;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location mLastLocation;
    private String customerUID = "";
    private boolean isDriverLoggingOut = false;
    //By this boolean variable we will keep track of driver's logging out of the app.
    private LinearLayout assignedCustomerInfoPanel;
    private ImageView assignedCutomerProfilePic;
    private TextView assignedCustomerName, assignedCustomerPhoneNumber,
            assignedCustomerDestination;
    private String carType = "";
    private Long rideStartTime = 0L;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.colorPrimaryDark,
            R.color.colorPrimary, R.color.colorPrimaryLight, R.color.colorAccent,
            R.color.colorBallRelease};

    @Override
    protected void onStart() {
        super.onStart();
        connectDriverToDataBase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_maps);
        //This poly line is the route between driver & customer
        polylines = new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.driverMap);
        mapFragment.getMapAsync(this);
        //Check for CustomerDummyRideHistory Connection permission
        checkGPSConnectionPermission();
        assignedCustomerInfoPanel = findViewById(R.id.assignedCustomerInfoPanel);
        assignedCutomerProfilePic = findViewById(R.id.assignedCutomerProfilePic);
        assignedCustomerName = findViewById(R.id.assignedCustomerName);
        assignedCustomerPhoneNumber = findViewById(R.id.assignedCustomerPhoneNumber);
        assignedCustomerDestination = findViewById(R.id.assignedCustomerDestination);

        DriverSettings = findViewById(R.id.DriverSettings);
        btnrideStatus = findViewById(R.id.btnrideStatus);
        btnrideStatus.setText("Waiting for customer");

    }

    private void getThisDriverCarType() {
        String driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mDriverCarModelRef = FirebaseDatabase.getInstance()
                .getReference("Users/Drivers")
                .child(driverUID).child("Driver_Basic_Info");
        mDriverCarModelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("Driver_Car_Model") != null){
                        carType = map.get("Driver_Car_Model").toString();
                        Log.i("CarType","Car Type: "+ carType);
                        Toast.makeText(DriverMapsActivity.this, "Car Type: " + carType,Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkGPSConnectionPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_REQUEST);
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //After map is ready we will call GoogleApiClient to connect by buildGoogleApiClient() method.
        buildGoogleApiClient();
        checkGPSConnectionPermission();
        //Above permission check is for below code.
        mMap.setMyLocationEnabled(true);
        getThisDriverCarType();
        getAssignedCustomer();
        DriverSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverMapsActivity.this, DriverSettingsActivity.class);
                startActivity(intent);
            }
        });
        btnrideStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (rideStatus){
                    case 0:
                        btnrideStatus.setText("");
                        //rideStatus == 0,That means our driver is waiting for customer
                        break;
                    case 1:
                        //rideStatus == 1,That means our driver is on his way to pick up customer
                        //Check whether driver cancelled the ride
                        String didDriverCancelTheRide = btnrideStatus.getText().toString();
                        if (didDriverCancelTheRide.equals("Cancel")){
                            //Let customer know that our driver cancelled the ride & change
                            // customer_ride_status == 0
                            rideStatus = 0;
                            DatabaseReference mCustomerCurrentRideInfoPnaelReffinish = FirebaseDatabase.getInstance()
                                    .getReference("Users/Customers")
                                    .child(customerUID)
                                    .child("Current_Ride_Info_Panle");
                            HashMap mapfinish = new HashMap();
                            mapfinish.put("Ride_Status",rideStatus);
                            mCustomerCurrentRideInfoPnaelReffinish.updateChildren(mapfinish);
                            endCurrentRide();
                        }
                        break;
                    case 2:
                        //rideStatus == 2,That means our driver is on his way to drop our customer
                        //on his destination.
                        rideStartTime = getCurrentTimeStamp();
                        rideStatus = 2;
                        //We will let our customer know His Ride_Status.
                        DatabaseReference mCustomerCurrentRideInfoPnaelRef = FirebaseDatabase.getInstance()
                                .getReference("Users/Customers")
                                .child(customerUID)
                                .child("Current_Ride_Info_Panle");
                        HashMap map = new HashMap();
                        map.put("Ride_Status",rideStatus);
                        mCustomerCurrentRideInfoPnaelRef.updateChildren(map);
                        reachedToAssignedCustomerPickUpLocation = true;
                        btnrideStatus.setText("Destination Distance:------");
                        getRouteToCustomerDestinationLocation();
                        break;
                    case 3:
                        //*********************
                        rideStatus = 0;
                        DatabaseReference mCustomerCurrentRideInfoPnaelReffinish = FirebaseDatabase.getInstance()
                                .getReference("Users/Customers")
                                .child(customerUID)
                                .child("Current_Ride_Info_Panle");
                        HashMap mapfinish = new HashMap();
                        mapfinish.put("Ride_Status",rideStatus);
                        mCustomerCurrentRideInfoPnaelReffinish.updateChildren(mapfinish);
                        //**********************
                        //rideStatus == 3,That means our driver has dropped our customer
                        //on his destination. Now we will record this ride history in our database
                        recordRide();
                        endCurrentRide();
                        break;

                    case 4:
                        rideStatus = 4;
                        //rideStatus == 2, That means our driver cancelled the ride
                        endCurrentRide();
                        break;
                }
            }
        });
    }

    private void recordRide() {
        String driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mCustomerRideHistory = FirebaseDatabase.getInstance()
                .getReference("Users/Customers")
                .child(customerUID)
                .child("Customer_Ride_History");
        DatabaseReference mDriverRideHistory = FirebaseDatabase.getInstance()
                .getReference("Users/Drivers")
                .child(driverUID)
                .child("Driver_Driving_History");
        DatabaseReference mAllCompletedRideOFUthoaApp = FirebaseDatabase.getInstance().
                getReference("All_Completed_Ride_History");
        String requestUniqueID = mAllCompletedRideOFUthoaApp.push().getKey();
        //This above will give us an unique id to save this ride.
        //Now we will save this unique id on customer "Customer_Ride_History" database & also
        //"Driver_Driving_History" database.
        mCustomerRideHistory.child(requestUniqueID).setValue(true);
        mDriverRideHistory.child(requestUniqueID).setValue(true);

        //Now we will save all the required info of this ride on "All_Completed_Ride_History"
        //database.
        HashMap map = new HashMap();
        map.put("CustomerUID",customerUID);
        map.put("DriverUID",driverUID);
        map.put("Driver_Rating", 0);
        map.put("Customer_Rating", 0);
        map.put("Ride_End_Time", getCurrentTimeStamp());
        map.put("Ride_Start_Time", rideStartTime);
        map.put("Total_Payment","Tk-770");
        map.put("Payment_Method_Used","Cash");
        map.put("PickUp_Location_Address","ChayaBithi Eastern Housing");
        map.put("Destination_Location_Address",customerDestination);
        map.put("Distance","13");
        map.put("PickUpLat",customerpickUpLocationLatLng.latitude);
        map.put("PickUpLng",customerpickUpLocationLatLng.longitude);
        map.put("DestinationLat",customerDestinationLatLng.latitude);
        map.put("DestinationLng",customerDestinationLatLng.longitude);
        //map.put("","");
        mAllCompletedRideOFUthoaApp.child(requestUniqueID).updateChildren(map);
    }

    private Long getCurrentTimeStamp() {
        Long time = System.currentTimeMillis()/1000;
        return time;
    }


    private void endCurrentRide() {
        //Now we will remove customer uthao request data from fireBase Database.
        DatabaseReference mCustomerDatabaseRef = FirebaseDatabase.getInstance()
                .getReference("Customer_request");
        GeoFire geoFire = new GeoFire(mCustomerDatabaseRef);
        geoFire.removeLocation(customerUID);
        ////Now we will remove customer ID from 'driverFoundID' child (CustomerRequestID)
        //Now we have to let our driver know that customer has cancel ride. So we
        //will remove customerID from inside this driver's child.
        //So for precaution we will first check if this customer has found some driver.
        //than we remove this customer id from that driver's database
        String driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (driverUID != null){
            DatabaseReference mDriverDatabaseRef = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child("Drivers")
                    .child(driverUID)
                    .child("Customer_Request");
            mDriverDatabaseRef.setValue(true);
        }
        //So now go back to previous state
        btnrideStatus.setText("Waiting for customer");
        erasePolyLine();
        customerUID = "";
        if (mCustomerRequestRefListener != null){
            mCustomerRequestRef.removeEventListener(mCustomerRequestRefListener);
        }
        if (pickUpLocationMarker != null){
            pickUpLocationMarker.remove();
        }
        assignedCustomerInfoPanel.setVisibility(View.GONE);
        assignedCustomerName.setText("");
        assignedCustomerPhoneNumber.setText("");
        assignedCustomerDestination.setText("Destination:-----");
        assignedCutomerProfilePic.setImageResource(R.mipmap.ic_blank_profile_pic);
        gotAssignedCustomerPickupLocation = false;
        reachedToAssignedCustomerPickUpLocation = false;
        reachedToAssignedCustomerDestinationLocation = false;
        rideStartTime = 0L;
        DatabaseReference mDriverStatusDbRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child("Drivers").child(driverUID);
        Map map = new HashMap();
        map.put("Driver_Status",0);
        mDriverStatusDbRef.updateChildren(map);
    }

    private void getAssignedCustomer() {
        String driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mDriverDatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child("Drivers").child(driverUID).child("Customer_Request")
                .child("CustomerRequestID");
        //We will look for our customer under our drivers ID in the firebase database. So we will
        //put listener under "CustomerRequestID" child. I f suddenly this child has child
        //under it, than this below value event listener will let us know.
        mDriverDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    //If code execution enter this line, that means our driver has a customer
                    rideStatus = 1;
                    //rideStatus == 1,That means our driver is on his way to pick up customer.
                    customerUID = dataSnapshot.getValue().toString();
                    getAssignedCustomerPickupLocation();
                    getAssignedCustomerDestination();
                    getAssignedCustomerInfo();
                    //So now create "Current_Ride_Info_Panle" under customerUID;
                    //We will let our customer know His Ride_Status.
                    DatabaseReference mCustomerCurrentRideInfoPnaelRef = FirebaseDatabase.getInstance()
                            .getReference("Users/Customers")
                            .child(customerUID)
                            .child("Current_Ride_Info_Panle");
                    HashMap map = new HashMap();
                    map.put("Ride_Status",rideStatus);
                    mCustomerCurrentRideInfoPnaelRef.updateChildren(map);
                } else {
                    //If code execution enter this line, that means customer has cancelled his
                    //ride.
                    //Now we will go back to previous state
                    endCurrentRide();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getAssignedCustomerDestination() {
        String driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mDriverDatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child("Drivers").child(driverUID).child("Customer_Request");
        mDriverDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    //If code execution enter this line, that means our driver has a customer
                    //Destination
                    Map<String , Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("CustomerDestination") != null){
                        customerDestination = map.get("CustomerDestination").toString();
                        assignedCustomerDestination.setText("Destination: "+customerDestination);
                    } else {
                        assignedCustomerDestination.setText("Destination:-----");
                    }

                    Double destinationLat = 0.0;
                    Double destinationLng = 0.0;
                    if (map.get("CustomerDestinationLat") != null){
                        destinationLat = Double.valueOf(map.get("CustomerDestinationLat").toString());
                    }
                    if (map.get("CustomerDestinationLng") != null){
                        destinationLng = Double.valueOf(map.get("CustomerDestinationLng").toString());
                    }
                    customerDestinationLatLng = new LatLng(destinationLat,destinationLng);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(customerDestinationLatLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getAssignedCustomerInfo() {
        DatabaseReference mCustomerDataBaseRef = FirebaseDatabase.getInstance()
                .getReference("Users/Customers")
                .child(customerUID)
                .child("Customer_Basic_Info");
        mCustomerDataBaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    //After confirming that our customer has info on database than we make
                    //visible 'assignedCustomerInfoPanel'
                    //So before fetching data we have to show loading sign.
                    assignedCustomerInfoPanel.setVisibility(View.VISIBLE);
                    Map<String, Object> map =(Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("Customer_Name") != null){
                         String customerName = map.get("Customer_Name").toString();
                        assignedCustomerName.setText(customerName);
                    }
                    if (map.get("Customer_Mobile") != null) {
                        String customerMobile = map.get("Customer_Mobile").toString();
                        assignedCustomerPhoneNumber.setText(customerMobile);
                    }
                    //Now we will display customer's profile pic using glide.
                    if (map.get("Customer_Profile_Pic_Url") != null) {
                        String firebaseStorageCustomerPP = map.get("Customer_Profile_Pic_Url").toString();
                        Glide.with(getApplication()).load(firebaseStorageCustomerPP).into(assignedCutomerProfilePic);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private DatabaseReference mCustomerRequestRef;
    private ValueEventListener mCustomerRequestRefListener;
    private Marker pickUpLocationMarker;

    private void getAssignedCustomerPickupLocation() {
        mCustomerRequestRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Customer_request")
                .child(customerUID)
                .child("l");
        mCustomerRequestRefListener = mCustomerRequestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && !customerUID.equals("")){
                    double customerLat = 0;
                    double customerLng = 0;
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    if (map.get(0) != null){
                        customerLat = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1) != null){
                        customerLng = Double.parseDouble(map.get(1).toString());
                    }
                    //Now we will show our customers location on driver map
                    customerpickUpLocationLatLng = new LatLng(customerLat,customerLng);
                    pickUpLocationMarker = mMap.addMarker(new MarkerOptions().position(customerpickUpLocationLatLng).title("Pick Up Location")
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pick_up_marker)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(customerpickUpLocationLatLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                    getRouteToCustomerPickUpLocation(customerpickUpLocationLatLng);
                    checkWhetherOurDriverReachedToCustomerPickUpLocation();
                    gotAssignedCustomerPickupLocation = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkWhetherOurDriverReachedToCustomerPickUpLocation() {
        //Now we will get the distance between Our customer & Driver
        //Distance start here
        Location customerLoc = new Location("");
        customerLoc.setLatitude(customerpickUpLocationLatLng.latitude);
        customerLoc.setLongitude(customerpickUpLocationLatLng.longitude);
        Log.i("DriverMapsActivity","checkWhetherOurDriverReachedToCustomerPickUpLocation():\n "
                + "Customer Latitude: " + customerpickUpLocationLatLng.latitude +
                "Customer Longitude: " + customerpickUpLocationLatLng.longitude  );

        Location driverLoc = new Location("");
        if (mLastLocation != null){
            driverLoc.setLatitude(mLastLocation.getLatitude());
            driverLoc.setLongitude(mLastLocation.getLongitude());
        }




        float distanceBetweenCustomerAndDriver = driverLoc.distanceTo(customerLoc);


        if (distanceBetweenCustomerAndDriver < 100 && !reachedToAssignedCustomerPickUpLocation){
            //This means our driver reached within 200 meter of our customer location. Now we can enabled the button
            //To pick up customer. As our driver reached to customer, So now he will advance to destination. So
            //we don't need any polyline between driver & pickup location. now we can remove it.
            erasePolyLine();
            rideStatus = 2;
            btnrideStatus.setText("Pick Up Customer" );
            gotAssignedCustomerPickupLocation = false;
        } else {

            //#########################################################
            //***********************To Be Changed*********************
            //**************************Start**************************
//            btnrideStatus.setText("Pick up location distance: " + String.valueOf(distanceBetweenCustomerAndDriver)
//                    +" Meter");
            btnrideStatus.setText("Cancel");
            //#########################################################
            //***********************To Be Changed*********************
            //**************************End**************************
        }

        //Distance end here
    }

    private void checkWhetherOurDriverReachedToCustomerDestinationLocation() {
        //Now we will get the distance between Our Uthao car and Our Destination
        //Distance start here
        Location destinationLoc = new Location("");
        destinationLoc.setLatitude(customerDestinationLatLng.latitude);
        destinationLoc.setLongitude(customerDestinationLatLng.longitude);
        Log.i("DriverMapsActivity","checkWhetherOurDriverReachedToCustomerDestinationLocation():\n "
                + "Destination Latitude: " + customerDestinationLatLng.latitude +
                "Destination Longitude: " + customerDestinationLatLng.longitude  );

        Location driverLoc = new Location("");
        if (mLastLocation != null){
            driverLoc.setLatitude(mLastLocation.getLatitude());
            driverLoc.setLongitude(mLastLocation.getLongitude());
        }



        float distanceBetweenUthaoAndDestination = driverLoc.distanceTo(destinationLoc);


        if (distanceBetweenUthaoAndDestination < 200 && !reachedToAssignedCustomerDestinationLocation){
            //This means our driver reached within 200 meter of our customer location. Now we can enabled the button
            //To pick up customer. As our driver reached to customer, So now he will advance to destination. So
            //we don't need any polyline between driver & pickup location. now we can remove it.
            erasePolyLine();
            rideStatus = 3;
            //rideStatus == 3,That means our driver has dropped our customer
            //on his destination.
            //We will let our customer know His Ride_Status.
            DatabaseReference mCustomerCurrentRideInfoPnaelRef = FirebaseDatabase.getInstance()
                    .getReference("Users/Customers").child(customerUID).child("Current_Ride_Info_Panle");
            HashMap map = new HashMap();
            map.put("Ride_Status",rideStatus);
            mCustomerCurrentRideInfoPnaelRef.updateChildren(map);
            btnrideStatus.setText("Drop Customer" );
            reachedToAssignedCustomerDestinationLocation = true;
        } else {
            btnrideStatus.setText("Destination distance: " + String.valueOf(distanceBetweenUthaoAndDestination)
                    +" Meter");
        }

        //Distance end here
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //This method will be called when googleApiClient will successfully connected.
        //After googleApiClient is connected, than we can call for location request.
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        checkGPSConnectionPermission();
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        //This method will be called when googleApiClient connection will suspend.

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //This method will be called when googleApiClient will fail to connect.
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(50)
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));
        if (gotAssignedCustomerPickupLocation){
            checkWhetherOurDriverReachedToCustomerPickUpLocation();
        }
        if (reachedToAssignedCustomerPickUpLocation){
            getRouteToCustomerDestinationLocation();
            checkWhetherOurDriverReachedToCustomerDestinationLocation();
        }
        //Now we will send this driver location to firebase database. This activity is open
        //means driver is available for taking customer. So we will let know our server about
        //this driver location & his userID
        String driverUID = FirebaseAuth.getInstance().getUid();
//      GeoFire uses the Firebase database for data storage, allowing query results to be updated
//      in realtime as they change. GeoFire selectively loads only the data near certain locations,
//      keeping your applications light and responsive, even with extremely large datasets.
        if (!carType.equals("")){
            DatabaseReference mDriverIsAvailable = FirebaseDatabase.getInstance()
                    .getReference("DriversAvailAble")
                    .child(carType);
            DatabaseReference mDriverIsWorking=FirebaseDatabase.getInstance()
                    .getReference("Drivers_Working")
                    .child(carType);
            GeoFire geoFireIsAvailable = new GeoFire(mDriverIsAvailable);
            GeoFire geoFireIsWorking = new GeoFire(mDriverIsWorking);
            DatabaseReference mDriverStatus = FirebaseDatabase.getInstance()
                    .getReference("Users/Drivers")
                    .child(driverUID)
                    .child("Driver_Status");
            switch (customerUID){
                case "":
                    //'customerUID' blank means driver is available for picking up new customer. So we will store
                    //this driver location in our firebase 'DriversAvailAble' child.
                    geoFireIsWorking.removeLocation(driverUID);
                    geoFireIsAvailable.setLocation(driverUID,new GeoLocation(location.getLatitude(),
                            location.getLongitude()));
                    //This first argument 'driverUID' will be the child of the mDriverIsAvailable. Under this
                    //latitude and longitude will be created.
                    mDriverStatus.setValue(0);
                    //If driverStatus = 0 that means driver is available.
                    break;
                default:
                    //Default means 'customerUID' other than blank, Means our driver has customer. So first we will
                    //remove our driver location from our firebase 'DriversAvailAble' child & put his location in
                    //our firebase "Drivers_Working" child.
                    geoFireIsAvailable.removeLocation(driverUID);
                    geoFireIsWorking.setLocation(driverUID,new GeoLocation(location.getLatitude(), location.getLongitude()));
                    mDriverStatus.setValue(1);
                    //If driverStatus = 1 that means driver is working.
                    break;
            }
        } else {
            getThisDriverCarType();
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isDriverLoggingOut){
            //If driver is not logging out than we can disconnect driver from this method.
            disConnectDriverFromDatabase();
            //If driver is logging out than we can disconnect driver from driverLogOut() method.
            //Otherwise app will crash
        }
        finish();
    }

    public void driverLogOut(View view) {
        isDriverLoggingOut = true;
        disConnectDriverFromDatabase();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(DriverMapsActivity.this, Home.class);
        startActivity(intent);
        finish();
    }

    private void disConnectDriverFromDatabase(){
        //Now we will send this driver status to firebase database. This activity is closed
        //means driver is unavailable for taking customer. So we will let know our server about
        //this driver status & his userID
        String driverUID = FirebaseAuth.getInstance().getUid();
        DatabaseReference mDriverDataBaseRef = FirebaseDatabase.getInstance()
                .getReference("DriversAvailAble")
                .child(carType);
        GeoFire geoFire = new GeoFire(mDriverDataBaseRef);
        geoFire.removeLocation(driverUID);

        if (googleApiClient.isConnected()){
            googleApiClient.disconnect();
        }
    }

    private void connectDriverToDataBase(){
        if (googleApiClient != null){
            if (!googleApiClient.isConnected()){
                googleApiClient.connect();
            }
        }
    }


    //Code about routing between driver & customer starts here
    private void getRouteToCustomerPickUpLocation(LatLng customerpickUpLocationLatLng) {
        if (mLastLocation != null){
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)  //Disable alternativeRoutes
                    .waypoints(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()),
                            customerpickUpLocationLatLng)
                    .build();
            routing.execute();
        }

    }

    private void getRouteToCustomerDestinationLocation() {
        if (mLastLocation != null){
            if (customerDestinationLatLng.latitude != 0 && customerDestinationLatLng.longitude != 0){
                Routing routing = new Routing.Builder()
                        .travelMode(AbstractRouting.TravelMode.DRIVING)
                        .withListener(this)
                        .alternativeRoutes(true)  //Disable alternativeRoutes
                        .waypoints(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()),
                                customerDestinationLatLng)
                        .build();
                routing.execute();
            } else {
                Toast.makeText(DriverMapsActivity.this,"Routing is not avaiable\nNo Destination Selected",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        //First we remove any polylines which we previously have
        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        //Now we will create new polylines
        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {

    }

    private void erasePolyLine(){
        for (Polyline line : polylines){
            line.remove();
            //Above we are removing one poly line at a time
        }
//        Now we have to clear polylines array list
        polylines.clear();
    }


    //Code about routing between driver & customer ends here
}
