package com.sand_corporation.www.uthao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.crash.FirebaseCrash;
import com.sand_corporation.www.uthao.CustomerHistoryRecyclerView.CustomerRecyclerAdapter;
import com.sand_corporation.www.uthao.CustomerHistoryRecyclerView.History;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class CustomerHistoryActivity extends AppCompatActivity {

    private RecyclerView customerHistoryRecyclerView;
    private ArrayList<History> arrayList = new ArrayList<>();
    private CustomerRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String customerUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseCrash.log("CustomerHistoryActivity:onCreate.called");
        Crashlytics.log("CustomerHistoryActivity:onCreate.called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_history);

        customerUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        customerHistoryRecyclerView = findViewById(R.id.customerHistoryRecyclerView);
        customerHistoryRecyclerView.setNestedScrollingEnabled(false);
        //This setNestedScrollingEnabled() method will keep scrolling smooth.
        customerHistoryRecyclerView.setHasFixedSize(true);

        //Initialize layoutManager
        layoutManager = new LinearLayoutManager(CustomerHistoryActivity.this);
        //Now we will attach the layOutManager to Recycler View.
        customerHistoryRecyclerView.setLayoutManager(layoutManager);

        //Here we will put customer's life time ride id to 'arrayList'

        //Now initialize the object from CustomerDummyRecyclerAdapter.class
        adapter = new CustomerRecyclerAdapter(arrayList,CustomerHistoryActivity.this);
        customerHistoryRecyclerView.setAdapter(adapter);

        //Now we will get the Customer's life time ride history from fire base database
        getCustomerRideHistoryFromFireBase();
    }

    private void getCustomerRideHistoryFromFireBase() {
        DatabaseReference mCustomerRideHistoryRef = FirebaseDatabase.getInstance().getReference("Users/Customers")
                .child(customerUID).child("Customer_Ride_History");
        mCustomerRideHistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot rideKey : dataSnapshot.getChildren()){
                        //Here we created a variable 'rideKey' which is DataSnapshot type.
                        //So in each loop, we will go to our customer's 'Customer_Ride_History' database & we will
                        //pick ride ID and through this ID in 'rideKey' variable.
                        String uniqueRideID = rideKey.getKey();
                        fetchRideHistoryToRecyclerView(uniqueRideID);
                        //So in next loop 'rideKey' variable will be empty. That means it will store only one
                        //ride Key at a time not Array of ride key.
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchRideHistoryToRecyclerView(String uniqueRideID) {
        DatabaseReference historyDBRef = FirebaseDatabase.getInstance().getReference("All_Completed_Ride_History")
                .child(uniqueRideID);
        historyDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String rideId = dataSnapshot.getKey();
                    Long ride_end_time = 0L;
                    //As we are using single value event listener, we will use for loop to extract data
                    //from ride id child
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        if (child.getKey().equals("Ride_End_Time")){
                            //Execution of code will not enter this block unless our for loop find the right
                            //child.
                            ride_end_time = Long.valueOf(child.getValue().toString());
                        }
                        if (child.getKey().equals("customerUID")){
                            //Execution of code will not enter this block unless our for loop find the right
                            //child.
                            customerUID = child.getValue().toString();
                        }

                    }
                    History history = new History(rideId,getDate(ride_end_time));
                    Log.i("CustomerHistoryActivity",rideId);
                    arrayList.add(history);
                    Log.i("CustomerHistoryActivity","ArrayList Size: " + arrayList.size());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
