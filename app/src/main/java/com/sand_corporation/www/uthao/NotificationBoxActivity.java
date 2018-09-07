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
import com.sand_corporation.www.uthao.CustomerNotificationRecyclerView.Notification;
import com.sand_corporation.www.uthao.CustomerNotificationRecyclerView.NotificationRecyclerAdapter;
import com.sand_corporation.www.uthao.CustomerNotificationSQLiteDB.CustomerNotificationDbContract;
import com.sand_corporation.www.uthao.CustomerNotificationSQLiteDB.CustomerNotificationDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class NotificationBoxActivity extends AppCompatActivity {

    private RecyclerView notificationsRecyclerView;
    private ArrayList<Notification> arrayList = new ArrayList<>();
    private NotificationRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView ic_back_sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseCrash.log("NotificationBoxActivity:onCreate.called");
        Crashlytics.log("NotificationBoxActivity:onCreate.called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_box);

        notificationsRecyclerView = findViewById(R.id.notificationsRecyclerView);
        notificationsRecyclerView.setNestedScrollingEnabled(false);
        //This setNestedScrollingEnabled() method will keep scrolling smooth.
        notificationsRecyclerView.setHasFixedSize(true);

        //Initialize layoutManager
        layoutManager = new LinearLayoutManager(NotificationBoxActivity.this);
        //Now we will attach the layOutManager to Recycler View.
        notificationsRecyclerView.setLayoutManager(layoutManager);

        //Now initialize the object from NotificationRecyclerAdapter.class
        adapter = new NotificationRecyclerAdapter(arrayList, NotificationBoxActivity.this);
        notificationsRecyclerView.setAdapter(adapter);

        ic_back_sign = findViewById(R.id.ic_back_sign);
        ic_back_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Now we will get the Notification from SQLite database
        getNotificationFromSQLite();
    }

    private void getNotificationFromSQLite() {
        FirebaseCrash.log("NotificationBoxActivity:getNotificationFromSQLite.called");
        Crashlytics.log("NotificationBoxActivity:getNotificationFromSQLite.called");
        //At the beginning if there is previous data available in the adapter we need to clear those
        //data.
        arrayList.clear();
        CustomerNotificationDbHelper helper = new CustomerNotificationDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.retrieveNotificationFromQLiteDatabase(db);
        while (cursor.moveToNext()){ //this line will return true, un till there is new row to be read
            int date_column_id = cursor.getColumnIndex
                    (CustomerNotificationDbContract.TABLE_NOTIFICATIONS_FIRST_COLUMN_DATE);
            int title_column_id = cursor.getColumnIndex
                    (CustomerNotificationDbContract.TABLE_NOTIFICATIONS_SECOND_COLUMN_TITLE);
            int message_column_id = cursor.getColumnIndex
                    (CustomerNotificationDbContract.TABLE_NOTIFICATIONS_THIRD_COLUMN_MESSAGE);
            int notification_read_status_column_id = cursor.getColumnIndex
                    (CustomerNotificationDbContract.TABLE_NOTIFICATIONS_FOURTH_COLUMN_READ_STATUS);

            String date = cursor.getString(date_column_id);
            String title = cursor.getString(title_column_id);
            String message = cursor.getString(message_column_id);
            int notification_read_status = cursor.getInt(notification_read_status_column_id);

            Notification notification = new Notification(date,title,message,notification_read_status);
            arrayList.add(notification);
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
