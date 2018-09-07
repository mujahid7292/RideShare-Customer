package com.sand_corporation.www.uthao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.crash.FirebaseCrash;
import com.sand_corporation.www.uthao.CustomerDummyHistoryRecyclerView.CustomerDummyRecyclerAdapter;
import com.sand_corporation.www.uthao.CustomerDummyHistoryRecyclerView.DummyHistory;
import com.sand_corporation.www.uthao.CustomerDummyRideHistory.CustomerDummyRideHistoryDbContract;
import com.sand_corporation.www.uthao.CustomerDummyRideHistory.CustomerDummyRideHistoryDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CustomerDummyHistoryActivity extends AppCompatActivity {

    private RecyclerView customerDummyHistoryRecyclerView;
    private ArrayList<DummyHistory> arrayList = new ArrayList<>();
    private CustomerDummyRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView ic_back_sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseCrash.log("CustomerDummyHistoryActivity:onCreate.called");
        Crashlytics.log("CustomerDummyHistoryActivity:onCreate.called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dummy_history);

        customerDummyHistoryRecyclerView = findViewById(R.id.customerDummyHistoryRecyclerView);
        customerDummyHistoryRecyclerView.setNestedScrollingEnabled(false);
        //This setNestedScrollingEnabled() method will keep scrolling smooth.
        customerDummyHistoryRecyclerView.setHasFixedSize(true);

        //Initialize layoutManager
        layoutManager = new LinearLayoutManager(CustomerDummyHistoryActivity.this);
        //Now we will attach the layOutManager to Recycler View.
        customerDummyHistoryRecyclerView.setLayoutManager(layoutManager);

        //Here we will put customer's dummy life time ride id to 'arrayList'

        //Now initialize the object from CustomerDummyRecyclerAdapter.class
        adapter = new CustomerDummyRecyclerAdapter(arrayList,CustomerDummyHistoryActivity.this);
        customerDummyHistoryRecyclerView.setAdapter(adapter);

        ic_back_sign = findViewById(R.id.ic_back_sign);
        ic_back_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Now we will get the Customer's life time ride history from fire base database
        getCustomerDummyRideHistoryFromSQLite();
    }

    private void getCustomerDummyRideHistoryFromSQLite() {
        //At the beginning if there is previous data available in the adapter we need to clear those
        //data.
        arrayList.clear();
        CustomerDummyRideHistoryDbHelper helper = new CustomerDummyRideHistoryDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.readFromSQLiteDatabase(db);
        while (cursor.moveToNext()){ //this line will return true, un till there is new row to be read
            int customerDummyRideId_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.FIRST_COLUMN_NAME);
            int strCustomerPickUpLocation_column_id =
                    cursor.getColumnIndex(CustomerDummyRideHistoryDbContract.FIFTH_COLUMN_NAME);
            int strCustomerDestinationLocation_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.SIXTH_COLUMN_NAME);
            int imgDummyRideHistoryPolyLIne_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.TENTH_COLUMN_NAME);
            int strCustomerRideDate_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.TWELEVETH_COLUMN_NAME);

            String customerDummyRideId = cursor.getString(customerDummyRideId_column_id);
            String CustomerRideDate = cursor.getString(strCustomerRideDate_column_id);
            String strCustomerPickUpLocation = cursor.getString(strCustomerPickUpLocation_column_id);
            String strCustomerDestinationLocation = cursor.getString(strCustomerDestinationLocation_column_id);
            byte[] imgDummyRideHistoryPolyLIne = cursor.getBlob(imgDummyRideHistoryPolyLIne_column_id);

            DummyHistory dummyHistory = new DummyHistory(CustomerRideDate,strCustomerPickUpLocation,
                    strCustomerDestinationLocation,customerDummyRideId,imgDummyRideHistoryPolyLIne);
            arrayList.add(dummyHistory);
        }
//        Log.i("LOG","Main Activity readDataFromSQLiteDatabase");
        adapter.notifyDataSetChanged();
//        Log.i("LOG","Main Activity readDataFromSQLiteDatabase notifyDataSetChanged() complete");
        cursor.close();
        helper.close();
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
