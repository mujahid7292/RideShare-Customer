package com.sand_corporation.www.uthao.SQDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HP on 7/18/2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE_USER_PIC = "create table "+DbContract.TABLE_USER_PIC+
            "("+DbContract.TABLE_USER_PIC_FIRST_COLUMN_NAME+" text,"+DbContract.TABLE_USER_PIC_SECOND_COLUMN_NAME+" BLOB);";
    private static final String DROP_TABLE_USER_PIC = "drop table if exists " + DbContract.TABLE_USER_PIC;

    public DbHelper(Context context) {
        super(context, DbContract.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER_PIC);
//        Log.i("LOG","DbHelper onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_USER_PIC);
        onCreate(db);
//        Log.i("LOG","DbHelper onUpgrade");
    }

    public void saveCustomerPicToSQLiteDatabase(String customerName, byte[] customer_pic,
                                         SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        //For contentValues put method, first we have to pass Column name & then column value.
        contentValues.put(DbContract.TABLE_USER_PIC_FIRST_COLUMN_NAME,customerName);
        contentValues.put(DbContract.TABLE_USER_PIC_SECOND_COLUMN_NAME,customer_pic);
        db.insert(DbContract.TABLE_USER_PIC,null,contentValues);

    }

    public Cursor retrieveCustomerPicFromQLiteDatabase(SQLiteDatabase db){
        String[] columns = {DbContract.TABLE_USER_PIC_FIRST_COLUMN_NAME,DbContract.TABLE_USER_PIC_SECOND_COLUMN_NAME};
        //Here we will pass number of column which we want to see on the recycler view.
        //As we want to see all the column. we have passed those column name.
        Cursor cursor = db.query(DbContract.TABLE_USER_PIC,columns,null,null,null,null,null,null);
//        Log.i("LOG","DbHelper readFromSQLiteDatabase");
        return cursor;
    }

    public void updateCustomerPicToSQLiteDatabase(String customerName,
                                                  byte[] customer_pic,
                                                  SQLiteDatabase db){
        //Here we want to update customer_pic based on the customerName. As we have passed
        //only customer_pic through contentValues only customer_pic on that row will updated.
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.TABLE_USER_PIC_SECOND_COLUMN_NAME,customer_pic);
        String whereClause = DbContract.TABLE_USER_PIC_FIRST_COLUMN_NAME + " LIKE?";
        String[] whereArgs = {customerName};
        //By whereClause we are indicating which column we want to update.
        //By [] whereArgs we are indicating which row we want to update.
        //In contentValues we have put only customer_pic column name & customer_pic
        //column value so in that row only TABLE_USER_PIC_SECOND_COLUMN_NAME will update.
        //first column will not update as we did not put any Column name &
        //column value for first column.
        db.update(DbContract.TABLE_USER_PIC,contentValues,whereClause,    whereArgs);
                                                    //Column number    Row number
//        Log.i("LOG","DbHelper updateSQLiteDatabase");

    }
}
