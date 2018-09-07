package com.sand_corporation.www.uthao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CustomerHistorySinglePageActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    private  String customerRideId;
    private String customerUID;
    private String driverUID;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    //Now bring all the view
    private TextView txtCustomerRideDate, txtDriverCarModel, txtTotalCostOfTheRide,
            txtPaymentMode,txtCustomerPickUpLocation, txtCustomerDestinationLocation,
            txtCustomerRideDistance, txtDriverName;
    private ImageView imgDriverProfilePic;
    private RatingBar rtnDriverRating;
    private DatabaseReference allCompletedRideHistoryDb;
    private Long rideEndTimeWithDate = 0L;
    private String strDriverCarModel, strTotalPayment, strPaymentMethodUsed, strPickUpLocationAddress,
    strDestinationLocationAddress, strDistance, strDriverFullName, strDriverProfilePicURL;
    private int intDriverRatingInThisRide;
    private Double pickUpLat = 0.0;
    private Double pickUpLng = 0.0;
    private Double destinationLat = 0.0;
    private Double destinationLng = 0.0;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{com.sand_corporation.www.uthao.R.color.colorPrimaryDark, com.sand_corporation.www.uthao.R.color.colorPrimary, com.sand_corporation.www.uthao.R.color.colorPrimaryLight, com.sand_corporation.www.uthao.R.color.colorAccent, com.sand_corporation.www.uthao.R.color.primary_dark_material_light};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseCrash.log("CustomerHistorySinglePageActivity:onCreate.called");
        Crashlytics.log("CustomerHistorySinglePageActivity:onCreate.called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_history_single_page);
        //This poly line is the route between pickup location & destination
        polylines = new ArrayList<>();
        customerRideId = getIntent().getExtras().getString("customerRideId");
        //By this specific 'customerRideId' we will be able to understand what data do we have to
        //fetch form server.
        customerUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mapFragment = (SupportMapFragment)getSupportFragmentManager().
                findFragmentById(R.id.customerHistorySinglePageMap);
        mapFragment.getMapAsync(this);

        //Initialize all the view
        //***********************
        txtCustomerRideDate = findViewById(R.id.txtCustomerRideDate);
        txtDriverCarModel = findViewById(R.id.txtDriverCarModel);
        txtTotalCostOfTheRide = findViewById(R.id.txtTotalCostOfTheRide);
        txtPaymentMode = findViewById(R.id.txtPaymentMode);
        txtCustomerPickUpLocation = findViewById(R.id.txtCustomerPickUpLocation);
        txtCustomerDestinationLocation = findViewById(R.id.txtCustomerDestinationLocation);
        txtCustomerRideDistance = findViewById(R.id.txtCustomerRideDistance);
        txtDriverName = findViewById(R.id.txtDriverName);

        imgDriverProfilePic = findViewById(R.id.imgDriverProfilePic);

        rtnDriverRating = findViewById(R.id.rtnDriverRating);
        //***********************

        rtnDriverRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                allCompletedRideHistoryDb.child("Driver_Rating").setValue(v);
                DatabaseReference mDriverRatingDB = FirebaseDatabase.getInstance().getReference("Users/Drivers")
                        .child(driverUID).child("Driver_Driving_History").child(customerRideId);
                mDriverRatingDB.child("Driver_Rating").setValue(v);
            }
        });

        getRideInformation();
    }

    private void getDriverPersonalInfo() {
        DatabaseReference mDriverBasicInfoRef = FirebaseDatabase.getInstance().getReference("Users/Drivers")
                .child(driverUID).child("Driver_Basic_Info");
        mDriverBasicInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Object> map = (Map<String, Object>) dataSnapshot.getValue();
                if (map.get("Driver_Car_Model") != null){
                    strDriverCarModel = map.get("Driver_Car_Model").toString();
                    txtDriverCarModel.setText(strDriverCarModel);
                }
                if (map.get("Driver_Full_Name") != null){
                    strDriverFullName = map.get("Driver_Full_Name").toString();
                    txtDriverName.setText(strDriverFullName);
                }
                if (map.get("Driver_Profile_Pic") != null){
                    strDriverProfilePicURL = map.get("Driver_Profile_Pic").toString();
                    Glide.with(getApplication()).load(strDriverProfilePicURL).into(imgDriverProfilePic);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void getRideInformation() {
        allCompletedRideHistoryDb = FirebaseDatabase.getInstance()
                .getReference("All_Completed_Ride_History").child(customerRideId);
        //From 'All_Completed_Ride_History' we are pointing to this customer this specific
        // ride history.
        allCompletedRideHistoryDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Map<String,Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("DriverUID") != null){
                        driverUID = map.get("DriverUID").toString();
                    }
                    if (map.get("Ride_End_Time") != null){
                        rideEndTimeWithDate = Long.valueOf(map.get("Ride_End_Time").toString());
                        txtCustomerRideDate.setText(getDate(rideEndTimeWithDate));
                    }

                    if (map.get("Total_Payment") != null){
                        strTotalPayment = map.get("Total_Payment").toString();
                        txtTotalCostOfTheRide.setText(strTotalPayment);
                    }
                    if (map.get("Payment_Method_Used") != null){
                        strPaymentMethodUsed = map.get("Payment_Method_Used").toString();
                        txtPaymentMode.setText(strPaymentMethodUsed);
                    }
                    if (map.get("PickUp_Location_Address") != null){
                        strPickUpLocationAddress = map.get("PickUp_Location_Address").toString();
                        txtCustomerPickUpLocation.setText(strPickUpLocationAddress);
                    }
                    if (map.get("Destination_Location_Address") != null){
                        strDestinationLocationAddress = map.get("Destination_Location_Address").toString();
                        txtCustomerDestinationLocation.setText(strDestinationLocationAddress);
                    }
                    if (map.get("Distance") != null){
                        strDistance = map.get("Distance").toString();
                        txtCustomerRideDistance.setText(strDistance);
                    }
                    if (map.get("Driver_Rating") != null){
                        intDriverRatingInThisRide = Integer.parseInt(map.get("Driver_Rating").toString());
                        rtnDriverRating.setRating(intDriverRatingInThisRide);
                    }
                    if (map.get("PickUpLat") != null){
                        pickUpLat = Double.valueOf(map.get("PickUpLat").toString());
                    }
                    if (map.get("PickUpLng") != null){
                        pickUpLng = Double.valueOf(map.get("PickUpLng").toString());
                    }
                    if (map.get("DestinationLat") != null){
                        destinationLat = Double.valueOf(map.get("DestinationLat").toString());
                    }
                    if (map.get("DestinationLng") != null){
                        destinationLng = Double.valueOf(map.get("DestinationLng").toString());
                    }
                    getDriverPersonalInfo();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private String getDate(Long ride_end_time) {
        //First we will create an object from Calender.class As we are considering
        //Different time zone. This will give us customers local time zone.
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(ride_end_time * 1000);
        String date = DateFormat.format("dd-MM-yyyy hh:mm",calendar).toString();
        return date;

    }

}
