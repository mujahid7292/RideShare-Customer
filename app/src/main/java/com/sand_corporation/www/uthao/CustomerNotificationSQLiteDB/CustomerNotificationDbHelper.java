package com.sand_corporation.www.uthao.CustomerNotificationSQLiteDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HP on 7/18/2017.
 */

public class CustomerNotificationDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE_NOTIFICATIONS = "create table "+ CustomerNotificationDbContract.TABLE_NAME_NOTIFICATIONS +
            "("+ CustomerNotificationDbContract.TABLE_NOTIFICATIONS_FIRST_COLUMN_DATE +" text,"+
            CustomerNotificationDbContract.TABLE_NOTIFICATIONS_SECOND_COLUMN_TITLE +" text,"+
            CustomerNotificationDbContract.TABLE_NOTIFICATIONS_THIRD_COLUMN_MESSAGE +" text,"+
            CustomerNotificationDbContract.TABLE_NOTIFICATIONS_FOURTH_COLUMN_READ_STATUS +" integer);";
    private static final String DROP_TABLE_NOTIFICATIONS = "drop table if exists " + CustomerNotificationDbContract.TABLE_NAME_NOTIFICATIONS;

    public CustomerNotificationDbHelper(Context context) {
        super(context, CustomerNotificationDbContract.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTIFICATIONS);
//        Log.i("LOG","CustomerNotificationDbHelper onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_NOTIFICATIONS);
        onCreate(db);
//        Log.i("LOG","CustomerNotificationDbHelper onUpgrade");
    }

    public void saveNotificationToSQLiteDatabase(String date, String title, String message,int isNotificationRead,
                                                 SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        //For contentValues put method, first we have to pass Column name & then column value.
        contentValues.put(CustomerNotificationDbContract.TABLE_NOTIFICATIONS_FIRST_COLUMN_DATE,date);
        contentValues.put(CustomerNotificationDbContract.TABLE_NOTIFICATIONS_SECOND_COLUMN_TITLE,title);
        contentValues.put(CustomerNotificationDbContract.TABLE_NOTIFICATIONS_THIRD_COLUMN_MESSAGE,message);
        contentValues.put(CustomerNotificationDbContract.TABLE_NOTIFICATIONS_FOURTH_COLUMN_READ_STATUS,isNotificationRead);
        db.insert(CustomerNotificationDbContract.TABLE_NAME_NOTIFICATIONS,null,contentValues);

    }

    public Cursor retrieveNotificationFromQLiteDatabase(SQLiteDatabase db){
        String[] columns = {CustomerNotificationDbContract.TABLE_NOTIFICATIONS_FIRST_COLUMN_DATE,
                CustomerNotificationDbContract.TABLE_NOTIFICATIONS_SECOND_COLUMN_TITLE,
                CustomerNotificationDbContract.TABLE_NOTIFICATIONS_THIRD_COLUMN_MESSAGE,
                CustomerNotificationDbContract.TABLE_NOTIFICATIONS_FOURTH_COLUMN_READ_STATUS};
        //Here we will pass number of column which we want to see on the recycler view.
        //As we want to see all the column. we have passed those column name.
        Cursor cursor = db.query(CustomerNotificationDbContract.TABLE_NAME_NOTIFICATIONS,columns,
                null,null,null,null,null,null);
//        Log.i("LOG","CustomerNotificationDbHelper readFromSQLiteDatabase");
        return cursor;
    }

    public void updateNotificationStatusToSQLiteDatabase(String date,
                                                  int isNotificationRead,
                                                  SQLiteDatabase db){
        //Here we want to update notification status based on the date. As we have passed
        //only isNotificationRead through contentValues only isNotificationRead on that row will updated.
        ContentValues contentValues = new ContentValues();
        contentValues.put(CustomerNotificationDbContract.TABLE_NOTIFICATIONS_FOURTH_COLUMN_READ_STATUS,isNotificationRead);
        String whereClause = CustomerNotificationDbContract.TABLE_NOTIFICATIONS_FIRST_COLUMN_DATE + " LIKE?";
        String[] whereArgs = {date};
        //By whereClause we are indicating which column we want to update.
        //By [] whereArgs we are indicating which row we want to update.
        //In contentValues we have put only customer_pic column name & customer_pic
        //column value so in that row only TABLE_NOTIFICATIONS_SECOND_COLUMN_TITLE will update.
        //first column will not update as we did not put any Column name &
        //column value for first column.
        db.update(CustomerNotificationDbContract.TABLE_NAME_NOTIFICATIONS,contentValues,whereClause,
                whereArgs);
                                                    //Column number    Row number
//        Log.i("LOG","CustomerNotificationDbHelper updateSQLiteDatabase");

    }
}
