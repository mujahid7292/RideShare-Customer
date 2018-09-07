package com.sand_corporation.www.uthao.CustomerRideHistory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HP on 2/13/2018.
 */

public class CustomerRideHistoryDbHelper extends SQLiteOpenHelper {

    private Context mContext;
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE = "create table "+ CustomerRideHistoryDbContract.TABLE_NAME+
            "("+ CustomerRideHistoryDbContract.FIRST_COLUMN_NAME +" text,"
            + CustomerRideHistoryDbContract.SECOND_COLUMN_NAME+" text,"+""
            + CustomerRideHistoryDbContract.THIRD_COLUMN_NAME+" text,"+""
            + CustomerRideHistoryDbContract.FOURTH_COLUMN_NAME+" text,"+""
            + CustomerRideHistoryDbContract.FIFTH_COLUMN_NAME+" text,"+""
            + CustomerRideHistoryDbContract.SIXTH_COLUMN_NAME+" text,"+""
            + CustomerRideHistoryDbContract.SEVENTH_COLUMN_NAME+" text,"+""
            + CustomerRideHistoryDbContract.EIGHTH_COLUMN_NAME+" text,"+""
            + CustomerRideHistoryDbContract.NINETH_COLUMN_NAME+" BLOB,"+""
            + CustomerRideHistoryDbContract.TENTH_COLUMN_NAME+" text,"+""
            + CustomerRideHistoryDbContract.ELEVENTH_COLUMN_NAME +" integer,"
            + CustomerRideHistoryDbContract.TWELEVETH_COLUMN_NAME+" text,"+""
            + CustomerRideHistoryDbContract.THIRTENTH_COLUMN_NAME+" text,"+""
            + CustomerRideHistoryDbContract.FOURTEENTH_COLUMN_NAME+" text,"+""
            + CustomerRideHistoryDbContract.FIFTEENTH_COLUMN_NAME+" text,"+""
            + CustomerRideHistoryDbContract.SIXTEENTH_COLUMN_NAME+" text,"+""
            + CustomerRideHistoryDbContract.SEVENTEENTH_COLUMN_NAME+" text,"+""
            + CustomerRideHistoryDbContract.EIGHTEENTH_COLUMN_NAME+" text);";
    private static final String DROP_TABLE = "drop table if exists " + CustomerRideHistoryDbContract.TABLE_NAME;

    public CustomerRideHistoryDbHelper(Context context) {
        super(context, CustomerRideHistoryDbContract.DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
//        Log.i("LOG","CustomerDummyRideHistoryDbHelper onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
//        Log.i("LOG","CustomerDummyRideHistoryDbHelper onUpgrade");
    }

    public void saveDataToSQLiteDatabase(String customerDummyRideId,
                                         String strCustomerRideDate,String strDriverCarModel,
                                         String strTotalCostOfTheRide, String strPaymentMode,
                                         String strCustomerPickUpLocation, String strCustomerDestinationLocation,
                                         String strCustomerRideDistance, String strDriverName,
                                         String strTotalDistanceCost, String strTotalMinutes,
                                         String strTotalMinutesCost, String strTotalCostWithoutDiscount,
                                         String strTotalDiscount, String strTotalCostAfterDiscount,
                                         String polyLineTotalRideLength,
                                         byte[] imgDriverProfilePic,int intDriverRating, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        //For contentValues put method, first we have to pass Column name & then column value.
        contentValues.put(CustomerRideHistoryDbContract.FIRST_COLUMN_NAME,customerDummyRideId);
        contentValues.put(CustomerRideHistoryDbContract.SECOND_COLUMN_NAME,strDriverCarModel);
        contentValues.put(CustomerRideHistoryDbContract.THIRD_COLUMN_NAME,strTotalCostOfTheRide);
        contentValues.put(CustomerRideHistoryDbContract.FOURTH_COLUMN_NAME,strPaymentMode);
        contentValues.put(CustomerRideHistoryDbContract.FIFTH_COLUMN_NAME,strCustomerPickUpLocation);
        contentValues.put(CustomerRideHistoryDbContract.SIXTH_COLUMN_NAME,strCustomerDestinationLocation);
        contentValues.put(CustomerRideHistoryDbContract.SEVENTH_COLUMN_NAME,strCustomerRideDistance);
        contentValues.put(CustomerRideHistoryDbContract.EIGHTH_COLUMN_NAME,strDriverName);
        contentValues.put(CustomerRideHistoryDbContract.NINETH_COLUMN_NAME,imgDriverProfilePic);
        contentValues.put(CustomerRideHistoryDbContract.TENTH_COLUMN_NAME,polyLineTotalRideLength);
        contentValues.put(CustomerRideHistoryDbContract.ELEVENTH_COLUMN_NAME,intDriverRating);
        contentValues.put(CustomerRideHistoryDbContract.TWELEVETH_COLUMN_NAME,strCustomerRideDate);
        contentValues.put(CustomerRideHistoryDbContract.THIRTENTH_COLUMN_NAME,strTotalDistanceCost);
        contentValues.put(CustomerRideHistoryDbContract.FOURTEENTH_COLUMN_NAME,strTotalMinutes);
        contentValues.put(CustomerRideHistoryDbContract.FIFTEENTH_COLUMN_NAME,strTotalMinutesCost);
        contentValues.put(CustomerRideHistoryDbContract.SIXTEENTH_COLUMN_NAME,strTotalCostWithoutDiscount);
        contentValues.put(CustomerRideHistoryDbContract.SEVENTEENTH_COLUMN_NAME,strTotalDiscount);
        contentValues.put(CustomerRideHistoryDbContract.EIGHTEENTH_COLUMN_NAME,strTotalCostAfterDiscount);
        db.insert(CustomerRideHistoryDbContract.TABLE_NAME,null,contentValues);
//        Toast.makeText(mContext, "Record inserted", Toast.LENGTH_LONG).show();
//        Log.i("LOG","CustomerDummyRideHistoryDbHelper saveDataToSQLiteDatabase");
    }

    public Cursor readFromSQLiteDatabase(SQLiteDatabase db){
        String[] columns = {CustomerRideHistoryDbContract.FIRST_COLUMN_NAME,
                CustomerRideHistoryDbContract.SECOND_COLUMN_NAME,
                CustomerRideHistoryDbContract.THIRD_COLUMN_NAME,
                CustomerRideHistoryDbContract.FOURTH_COLUMN_NAME,
                CustomerRideHistoryDbContract.FIFTH_COLUMN_NAME,
                CustomerRideHistoryDbContract.SIXTH_COLUMN_NAME,
                CustomerRideHistoryDbContract.SEVENTH_COLUMN_NAME,
                CustomerRideHistoryDbContract.EIGHTH_COLUMN_NAME,
                CustomerRideHistoryDbContract.NINETH_COLUMN_NAME,
                CustomerRideHistoryDbContract.TENTH_COLUMN_NAME,
                CustomerRideHistoryDbContract.ELEVENTH_COLUMN_NAME,
                CustomerRideHistoryDbContract.TWELEVETH_COLUMN_NAME,
                CustomerRideHistoryDbContract.THIRTENTH_COLUMN_NAME,
                CustomerRideHistoryDbContract.FOURTEENTH_COLUMN_NAME,
                CustomerRideHistoryDbContract.FIFTEENTH_COLUMN_NAME,
                CustomerRideHistoryDbContract.SIXTEENTH_COLUMN_NAME,
                CustomerRideHistoryDbContract.SEVENTEENTH_COLUMN_NAME,
                CustomerRideHistoryDbContract.EIGHTEENTH_COLUMN_NAME};
        //Here we will pass number of column which we want to see on the recycler view.
        //As we want to see all the column. we have passed those column name.
        Cursor cursor = db.query(CustomerRideHistoryDbContract.TABLE_NAME,
                columns,null,null,null,null,null,null);
//        Log.i("LOG","CustomerDummyRideHistoryDbHelper readFromSQLiteDatabase");
        return cursor;
    }


}
