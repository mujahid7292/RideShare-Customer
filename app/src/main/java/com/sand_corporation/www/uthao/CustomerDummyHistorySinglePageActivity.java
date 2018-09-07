package com.sand_corporation.www.uthao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.crash.FirebaseCrash;
import com.sand_corporation.www.uthao.Animation.TabAnimation;
import com.sand_corporation.www.uthao.CustomerDummyRideHistory.CustomerDummyRideHistoryDbContract;
import com.sand_corporation.www.uthao.CustomerDummyRideHistory.CustomerDummyRideHistoryDbHelper;

import java.util.Calendar;
import java.util.Locale;

public class CustomerDummyHistorySinglePageActivity extends AppCompatActivity {

    private String customerDummyRideId;
    private TabHost tabHost;
    private ImageView imgDummyRideHistoryPolyLIneDummyHisSingle, imgDriverProfilePicDummyHisSingle,
            ic_back_sign;
    private TextView txtCustomerRideDateDummyHisSingle,txtDriverCarModelDummyHisSingle,
            txtTotalCostOfTheRideDummyHisSingle,txtPaymentModeDummyHisSingle,
            txtCustomerPickUpLocationDummyHisSingle,txtCustomerDestinationLocationDummyHisSingle
            ,txtDriverNameDummyHisSingle;
    //Below those textViews font will be changed
    private TextView receiptUnit, receiptFare, receiptTotal, receiptBaseFare, receiptBaseFareDistance,
            receiptBaseFarePerCosting, receiptBaseFareTotalCosting, receiptTotalDistance,
            txtCustomerRideDistanceDummyHisSingle, receiptTotalDistancePerUnitCosting,
            receiptTotalDistanceTotalCosting, receiptTotalMinutes, receiptTotalMinutesQuantity,
            receiptTotalMinutesPerUnitCosting, receiptTotalMinutesTotalCosting, receiptTotalFareWithoutDiscount,
            receiptTotalDiscountText, receiptTotalDiscountAmountInThisRide, receiptTotalText, receiptTotalFareAfterDiscount;
    private RatingBar rtnDriverRatingDummyHisSingle;

    private  String CustomerRideDate, strCustomerPickUpLocation,strCustomerDestinationLocation
            ,strDriverCarModel,strTotalCostOfTheRide,strPaymentMode,strCustomerRideDistance
             ,strDriverName, strTotalDistanceCost, strTotalMinutes, strTotalMinutesCost,
            strTotalCostWithoutDiscount, strTotalDiscount,  strTotalCostAfterDiscount;
    private int intDriverRating;
    private byte[] imgDummyRideHistoryPolyLIne,imgDriverProfilePic;

    @Override
    protected void onResume() {
        super.onResume();

        //Change TabHost title color and underline color
        TabHost tabhost = getTabHost();
        for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
        {
            //Change Title color
            TextView tv = (TextView) tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffffff"));
            //Change Title underline color
            View v = tabhost.getTabWidget().getChildAt(i);
            v.setBackgroundResource(R.drawable.tab_widget_bottom_indicator_by_xml);
        }
    }

    private TabHost getTabHost() {
        return tabHost = findViewById(R.id.tabHost);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseCrash.log("CustomerDummyHistorySinglePageActivity:onCreate.called");
        Crashlytics.log("CustomerDummyHistorySinglePageActivity:onCreate.called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dummy_history_single_page);

        customerDummyRideId = getIntent().getExtras().getString("customerDummyRideId");
        //By this specific 'customerRideId' we will be able to understand what data do we have to
        //fetch form server.

        imgDummyRideHistoryPolyLIneDummyHisSingle = findViewById
                (R.id.imgDummyRideHistoryPolyLIneDummyHisSingle);
        imgDriverProfilePicDummyHisSingle = findViewById(R.id.imgDriverProfilePicDummyHisSingle);
        ic_back_sign = findViewById(R.id.ic_back_sign);
        ic_back_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtCustomerRideDateDummyHisSingle = findViewById(R.id.txtCustomerRideDateDummyHisSingle);
        txtDriverCarModelDummyHisSingle = findViewById(R.id.txtDriverCarModelDummyHisSingle);
        txtTotalCostOfTheRideDummyHisSingle = findViewById(R.id.txtTotalCostOfTheRideDummyHisSingle);
        txtPaymentModeDummyHisSingle = findViewById(R.id.txtPaymentModeDummyHisSingle);
        txtCustomerPickUpLocationDummyHisSingle = findViewById
                (R.id.txtCustomerPickUpLocationDummyHisSingle);
        txtCustomerDestinationLocationDummyHisSingle = findViewById
                (R.id.txtCustomerDestinationLocationDummyHisSingle);
        txtDriverNameDummyHisSingle = findViewById(R.id.txtDriverNameDummyHisSingle);

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/Amandella.ttf");
        //Below those textViews font will be changed
        receiptUnit = findViewById(R.id.receiptUnit);
        receiptUnit.setTypeface(tf);
        receiptFare = findViewById(R.id.receiptFare);
        receiptFare.setTypeface(tf);
        receiptTotal = findViewById(R.id.receiptTotal);
        receiptTotal.setTypeface(tf);
        receiptBaseFare = findViewById(R.id.receiptBaseFare);
        receiptBaseFare.setTypeface(tf);
        receiptBaseFareDistance = findViewById(R.id.receiptBaseFareDistance);
        receiptBaseFareDistance.setTypeface(tf);
        receiptBaseFarePerCosting = findViewById(R.id.receiptBaseFarePerCosting);
        receiptBaseFarePerCosting.setTypeface(tf);
        receiptBaseFareTotalCosting = findViewById(R.id.receiptBaseFareTotalCosting);
        receiptBaseFareTotalCosting.setTypeface(tf);
        receiptTotalDistance = findViewById(R.id.receiptTotalDistance);
        receiptTotalDistance.setTypeface(tf);
        txtCustomerRideDistanceDummyHisSingle = findViewById(R.id.txtCustomerRideDistanceDummyHisSingle);
        txtCustomerRideDistanceDummyHisSingle.setTypeface(tf);
        receiptTotalDistancePerUnitCosting = findViewById(R.id.receiptTotalDistancePerUnitCosting);
        receiptTotalDistancePerUnitCosting.setTypeface(tf);
        receiptTotalDistanceTotalCosting = findViewById(R.id.receiptTotalDistanceTotalCosting);
        receiptTotalDistanceTotalCosting.setTypeface(tf);
        receiptTotalMinutes = findViewById(R.id.receiptTotalMinutes);
        receiptTotalMinutes.setTypeface(tf);
        receiptTotalMinutesQuantity = findViewById(R.id.receiptTotalMinutesQuantity);
        receiptTotalMinutesQuantity.setTypeface(tf);
        receiptTotalMinutesPerUnitCosting = findViewById(R.id.receiptTotalMinutesPerUnitCosting);
        receiptTotalMinutesPerUnitCosting.setTypeface(tf);
        receiptTotalMinutesTotalCosting = findViewById(R.id.receiptTotalMinutesTotalCosting);
        receiptTotalMinutesTotalCosting.setTypeface(tf);
        receiptTotalFareWithoutDiscount = findViewById(R.id.receiptTotalFareWithoutDiscount);
        receiptTotalFareWithoutDiscount.setTypeface(tf);
        receiptTotalDiscountText = findViewById(R.id.receiptTotalDiscountText);
        receiptTotalDiscountText.setTypeface(tf);
        receiptTotalDiscountAmountInThisRide = findViewById(R.id.receiptTotalDiscountAmountInThisRide);
        receiptTotalDiscountAmountInThisRide.setTypeface(tf);
        receiptTotalText = findViewById(R.id.receiptTotalText);
        receiptTotalText.setTypeface(tf);
        receiptTotalFareAfterDiscount = findViewById(R.id.receiptTotalFareAfterDiscount);
        receiptTotalFareAfterDiscount.setTypeface(tf);



        rtnDriverRatingDummyHisSingle = findViewById(R.id.rtnDriverRatingDummyHisSingle);

        //For custom color only using layer drawable to fill the star colors
        LayerDrawable stars = (LayerDrawable) rtnDriverRatingDummyHisSingle
                .getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#969696"),
                PorterDuff.Mode.SRC_ATOP); // for filled stars
        stars.getDrawable(1).setColorFilter(Color.parseColor("#969696"),
                PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.colorTransparentWhite),
                PorterDuff.Mode.SRC_ATOP); // for empty stars

        rtnDriverRatingDummyHisSingle.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Log.i("Check","New Rating Value: " + v);
            }
        });
        setUpTabLayOut();
        getCustomerDummyRideHistoryFromSQLite();
        displayDataOnTheViews();
    }

    private void setUpTabLayOut() {
        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec(getResources().getString(R.string.money_receipt));
        spec.setContent(R.id.tab1);
        spec.setIndicator(getResources().getString(R.string.money_receipt));
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec(getResources().getString(R.string.complain));
        spec.setContent(R.id.tab2);
        spec.setIndicator(getResources().getString(R.string.complain));
        tabHost.addTab(spec);

        //Event
        tabHost.setOnTabChangedListener(new TabAnimation(this,tabHost));
    }

    private void displayDataOnTheViews() {
        if (imgDummyRideHistoryPolyLIne != null){
            imgDummyRideHistoryPolyLIneDummyHisSingle.setImageBitmap(getImage(imgDummyRideHistoryPolyLIne));
        }
        if (imgDriverProfilePic != null){
            imgDriverProfilePicDummyHisSingle.setImageBitmap(getImage(imgDriverProfilePic));
        }
        txtCustomerRideDateDummyHisSingle.setText(CustomerRideDate == null? "N/A":CustomerRideDate);
        txtDriverCarModelDummyHisSingle.setText(strDriverCarModel == null? "N/A":strDriverCarModel);
        txtTotalCostOfTheRideDummyHisSingle.setText(strTotalCostAfterDiscount == null? "N/A":strTotalCostAfterDiscount);
        txtPaymentModeDummyHisSingle.setText(strPaymentMode == null? "N/A":strPaymentMode);
        txtCustomerPickUpLocationDummyHisSingle.setText(strCustomerPickUpLocation == null? "N/A": strCustomerPickUpLocation);
        txtCustomerDestinationLocationDummyHisSingle.setText(strCustomerDestinationLocation == null? "N/A":strCustomerDestinationLocation);
        txtCustomerRideDistanceDummyHisSingle.setText(strCustomerRideDistance == null? "N/A":strCustomerRideDistance);
        txtDriverNameDummyHisSingle.setText(strDriverName == null? "N/A":strDriverName);
        receiptTotalDistanceTotalCosting.setText(strTotalDistanceCost == null? "N/A":strTotalDistanceCost);
        receiptTotalMinutesQuantity.setText(strTotalMinutes == null? "N/A":strTotalMinutes);
        receiptTotalMinutesTotalCosting.setText(strTotalMinutesCost == null? "N/A":strTotalMinutesCost);
        receiptTotalFareWithoutDiscount.setText(strTotalCostWithoutDiscount == null? "N/A":strTotalCostWithoutDiscount);
        receiptTotalDiscountAmountInThisRide.setText(strTotalDiscount == null? "N/A":strTotalDiscount);
        receiptTotalFareAfterDiscount.setText(strTotalCostAfterDiscount == null? "N/A":strTotalCostAfterDiscount);
        rtnDriverRatingDummyHisSingle.setRating(intDriverRating);
    }

    private void getCustomerDummyRideHistoryFromSQLite() {
        String[] columns = new String[]{CustomerDummyRideHistoryDbContract.FIRST_COLUMN_NAME,
                CustomerDummyRideHistoryDbContract.SECOND_COLUMN_NAME,
                CustomerDummyRideHistoryDbContract.THIRD_COLUMN_NAME,
                CustomerDummyRideHistoryDbContract.FOURTH_COLUMN_NAME,
                CustomerDummyRideHistoryDbContract.FIFTH_COLUMN_NAME,
                CustomerDummyRideHistoryDbContract.SIXTH_COLUMN_NAME,
                CustomerDummyRideHistoryDbContract.SEVENTH_COLUMN_NAME,
                CustomerDummyRideHistoryDbContract.EIGHTH_COLUMN_NAME,
                CustomerDummyRideHistoryDbContract.NINETH_COLUMN_NAME,
                CustomerDummyRideHistoryDbContract.TENTH_COLUMN_NAME,
                CustomerDummyRideHistoryDbContract.ELEVENTH_COLUMN_NAME,
                CustomerDummyRideHistoryDbContract.TWELEVETH_COLUMN_NAME,
                CustomerDummyRideHistoryDbContract.THIRTENTH_COLUMN_NAME,
                CustomerDummyRideHistoryDbContract.FOURTEENTH_COLUMN_NAME,
                CustomerDummyRideHistoryDbContract.FIFTEENTH_COLUMN_NAME,
                CustomerDummyRideHistoryDbContract.SIXTEENTH_COLUMN_NAME,
                CustomerDummyRideHistoryDbContract.SEVENTEENTH_COLUMN_NAME,
                CustomerDummyRideHistoryDbContract.EIGHTEENTH_COLUMN_NAME};
        //Here we will pass number of column which we want to see on the single dummy page view.
        //As we want to see all the column except first column. So we did not pass first column name.
        CustomerDummyRideHistoryDbHelper helper = new CustomerDummyRideHistoryDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
//        Cursor cursor = helper.readFromSQLiteDatabase(db);
        //This where clause will tell in which column we have the 'customerDummyRideId'.
        String whereClause = CustomerDummyRideHistoryDbContract.FIRST_COLUMN_NAME + " LIKE?";
        //after selecting first column, we will pass in which row we want to go by passing
        //'customerDummyRideId' to whereArgs.
        String[] whereArgs = {customerDummyRideId};
        Cursor cursor = db.query(CustomerDummyRideHistoryDbContract.TABLE_NAME,
                columns, whereClause,
                whereArgs, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            int strDriverCarModel_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.SECOND_COLUMN_NAME);
            int strTotalCostOfTheRide_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.THIRD_COLUMN_NAME);
            int strPaymentMode_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.FOURTH_COLUMN_NAME);
            int strCustomerPickUpLocation_column_id =
                    cursor.getColumnIndex(CustomerDummyRideHistoryDbContract.FIFTH_COLUMN_NAME);
            int strCustomerDestinationLocation_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.SIXTH_COLUMN_NAME);
            int strCustomerRideDistance_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.SEVENTH_COLUMN_NAME);
            int strDriverName_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.EIGHTH_COLUMN_NAME);
            int imgDriverProfilePic_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.NINETH_COLUMN_NAME);
            int imgDummyRideHistoryPolyLIne_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.TENTH_COLUMN_NAME);
            int intDriverRating_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.ELEVENTH_COLUMN_NAME);
            int strCustomerRideDate_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.TWELEVETH_COLUMN_NAME);
            int strTotalDistanceCost_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.THIRTENTH_COLUMN_NAME);
            int strTotalMinutes_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.FOURTEENTH_COLUMN_NAME);
            int strTotalMinutesCost_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.FIFTEENTH_COLUMN_NAME);
            int strTotalCostWithoutDiscount_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.SIXTEENTH_COLUMN_NAME);
            int strTotalDiscount_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.SEVENTEENTH_COLUMN_NAME);
            int strTotalCostAfterDiscount_column_id = cursor.getColumnIndex
                    (CustomerDummyRideHistoryDbContract.EIGHTEENTH_COLUMN_NAME);

            CustomerRideDate = cursor.getString(strCustomerRideDate_column_id);
            strDriverCarModel = cursor.getString(strDriverCarModel_column_id);
            strTotalCostOfTheRide = cursor.getString(strTotalCostOfTheRide_column_id);
            strPaymentMode = cursor.getString(strPaymentMode_column_id);
            strCustomerPickUpLocation = cursor.getString(strCustomerPickUpLocation_column_id);
            strCustomerDestinationLocation = cursor.getString(strCustomerDestinationLocation_column_id);
            strCustomerRideDistance = cursor.getString(strCustomerRideDistance_column_id);
            strDriverName = cursor.getString(strDriverName_column_id);
            strTotalDistanceCost = cursor.getString(strTotalDistanceCost_column_id);
            strTotalMinutes = cursor.getString(strTotalMinutes_column_id);
            strTotalMinutesCost = cursor.getString(strTotalMinutesCost_column_id);
            strTotalCostWithoutDiscount = cursor.getString(strTotalCostWithoutDiscount_column_id);
            strTotalDiscount = cursor.getString(strTotalDiscount_column_id);
            strTotalCostAfterDiscount = cursor.getString(strTotalCostAfterDiscount_column_id);
            imgDriverProfilePic = cursor.getBlob(imgDriverProfilePic_column_id);
            imgDummyRideHistoryPolyLIne = cursor.getBlob(imgDummyRideHistoryPolyLIne_column_id);
            intDriverRating = cursor.getInt(intDriverRating_column_id);
        }

        cursor.close();
        helper.close();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
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
