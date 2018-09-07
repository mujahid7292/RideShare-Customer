package com.sand_corporation.www.uthao.CustomerDummyRideHistory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by HP on 7/18/2017.
 */

public class CustomerDummyRideHistoryDbHelper extends SQLiteOpenHelper {
    private Context mContext;
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE = "create table "+ CustomerDummyRideHistoryDbContract.TABLE_NAME+
            "("+ CustomerDummyRideHistoryDbContract.FIRST_COLUMN_NAME +" text,"
            + CustomerDummyRideHistoryDbContract.SECOND_COLUMN_NAME+" text,"+""
            + CustomerDummyRideHistoryDbContract.THIRD_COLUMN_NAME+" text,"+""
            + CustomerDummyRideHistoryDbContract.FOURTH_COLUMN_NAME+" text,"+""
            + CustomerDummyRideHistoryDbContract.FIFTH_COLUMN_NAME+" text,"+""
            + CustomerDummyRideHistoryDbContract.SIXTH_COLUMN_NAME+" text,"+""
            + CustomerDummyRideHistoryDbContract.SEVENTH_COLUMN_NAME+" text,"+""
            + CustomerDummyRideHistoryDbContract.EIGHTH_COLUMN_NAME+" text,"+""
            + CustomerDummyRideHistoryDbContract.NINETH_COLUMN_NAME+" BLOB,"+""
            + CustomerDummyRideHistoryDbContract.TENTH_COLUMN_NAME+" BLOB,"+""
            + CustomerDummyRideHistoryDbContract.ELEVENTH_COLUMN_NAME +" integer,"
            + CustomerDummyRideHistoryDbContract.TWELEVETH_COLUMN_NAME+" text,"+""
            + CustomerDummyRideHistoryDbContract.THIRTENTH_COLUMN_NAME+" text,"+""
            + CustomerDummyRideHistoryDbContract.FOURTEENTH_COLUMN_NAME+" text,"+""
            + CustomerDummyRideHistoryDbContract.FIFTEENTH_COLUMN_NAME+" text,"+""
            + CustomerDummyRideHistoryDbContract.SIXTEENTH_COLUMN_NAME+" text,"+""
            + CustomerDummyRideHistoryDbContract.SEVENTEENTH_COLUMN_NAME+" text,"+""
            + CustomerDummyRideHistoryDbContract.EIGHTEENTH_COLUMN_NAME+" text);";
    private static final String DROP_TABLE = "drop table if exists " + CustomerDummyRideHistoryDbContract.TABLE_NAME;

    public CustomerDummyRideHistoryDbHelper(Context context) {
        super(context, CustomerDummyRideHistoryDbContract.DATABASE_NAME, null, DATABASE_VERSION);
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
                                         byte[] imgDummyRideHistoryPolyLIne,
                                         SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        //For contentValues put method, first we have to pass Column name & then column value.
        contentValues.put(CustomerDummyRideHistoryDbContract.FIRST_COLUMN_NAME,customerDummyRideId);
        contentValues.put(CustomerDummyRideHistoryDbContract.TENTH_COLUMN_NAME,imgDummyRideHistoryPolyLIne);
        db.insert(CustomerDummyRideHistoryDbContract.TABLE_NAME,null,contentValues);
//        Toast.makeText(mContext, "Record inserted", Toast.LENGTH_LONG).show();
//        Log.i("LOG","CustomerDummyRideHistoryDbHelper saveDataToSQLiteDatabase");
    }

    public Cursor readFromSQLiteDatabase(SQLiteDatabase db){
        String[] columns = {CustomerDummyRideHistoryDbContract.FIRST_COLUMN_NAME,
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
        //Here we will pass number of column which we want to see on the recycler view.
        //As we want to see all the column. we have passed those column name.
        Cursor cursor = db.query(CustomerDummyRideHistoryDbContract.TABLE_NAME,
                columns,null,null,null,null,null,null);
//        Log.i("LOG","CustomerDummyRideHistoryDbHelper readFromSQLiteDatabase");
        return cursor;
    }

    public void updateSQLiteDatabase( String customerDummyRideId,
                                      String strCustomerRideDate,String strDriverCarModel,
                                      String strTotalCostOfTheRide, String strPaymentMode,
                                      String strCustomerPickUpLocation, String strCustomerDestinationLocation,
                                      String strCustomerRideDistance, String strDriverName,
                                      String strTotalDistanceCost, String strTotalMinutes,
                                      String strTotalMinutesCost, String strTotalCostWithoutDiscount,
                                      String strTotalDiscount, String strTotalCostAfterDiscount,
                                      byte[] imgDriverProfilePic,int intDriverRating, SQLiteDatabase db){
        //Here we want to update sync_status based on the date. As we have passed
        //only sync_status through contentValues only sync_status on that row will updated.
        ContentValues contentValues = new ContentValues();
        contentValues.put(CustomerDummyRideHistoryDbContract.SECOND_COLUMN_NAME,strDriverCarModel);
        contentValues.put(CustomerDummyRideHistoryDbContract.THIRD_COLUMN_NAME,strTotalCostOfTheRide);
        contentValues.put(CustomerDummyRideHistoryDbContract.FOURTH_COLUMN_NAME,strPaymentMode);
        contentValues.put(CustomerDummyRideHistoryDbContract.FIFTH_COLUMN_NAME,strCustomerPickUpLocation);
        contentValues.put(CustomerDummyRideHistoryDbContract.SIXTH_COLUMN_NAME,strCustomerDestinationLocation);
        contentValues.put(CustomerDummyRideHistoryDbContract.SEVENTH_COLUMN_NAME,strCustomerRideDistance);
        contentValues.put(CustomerDummyRideHistoryDbContract.EIGHTH_COLUMN_NAME,strDriverName);
        contentValues.put(CustomerDummyRideHistoryDbContract.NINETH_COLUMN_NAME,imgDriverProfilePic);
        contentValues.put(CustomerDummyRideHistoryDbContract.ELEVENTH_COLUMN_NAME,intDriverRating);
        contentValues.put(CustomerDummyRideHistoryDbContract.TWELEVETH_COLUMN_NAME,strCustomerRideDate);
        contentValues.put(CustomerDummyRideHistoryDbContract.THIRTENTH_COLUMN_NAME,strTotalDistanceCost);
        contentValues.put(CustomerDummyRideHistoryDbContract.FOURTEENTH_COLUMN_NAME,strTotalMinutes);
        contentValues.put(CustomerDummyRideHistoryDbContract.FIFTEENTH_COLUMN_NAME,strTotalMinutesCost);
        contentValues.put(CustomerDummyRideHistoryDbContract.SIXTEENTH_COLUMN_NAME,strTotalCostWithoutDiscount);
        contentValues.put(CustomerDummyRideHistoryDbContract.SEVENTEENTH_COLUMN_NAME,strTotalDiscount);
        contentValues.put(CustomerDummyRideHistoryDbContract.EIGHTEENTH_COLUMN_NAME,strTotalCostAfterDiscount);
        String whereClause = CustomerDummyRideHistoryDbContract.FIRST_COLUMN_NAME + " LIKE?";
        String[] whereArgs = {customerDummyRideId};
        //By whereClause we are indicating which column we want to update.
        //By [] whereArgs we are indicating which row we want to update.
        //In contentValues we have put only sync_status column name & sync_status
        //column value so in that row only FOURTH_COLUMN_SYNC_STATUS will update.
        //first three column will not update as we did not put any Column name &
        //column value for first two column.
        Log.i("Check","CustomerDummyRideHistoryDbHelper: customerDummyRideId=" +customerDummyRideId);
        db.update(CustomerDummyRideHistoryDbContract.TABLE_NAME, contentValues,
                whereClause,    whereArgs);
             //Column number    Row number
//        Log.i("LOG","CustomerDummyRideHistoryDbHelper updateSQLiteDatabase");

    }
}
