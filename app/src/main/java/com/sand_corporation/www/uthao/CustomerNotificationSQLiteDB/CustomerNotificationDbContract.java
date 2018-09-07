package com.sand_corporation.www.uthao.CustomerNotificationSQLiteDB;

/**
 * Created by HP on 9/16/2017.
 */

public class CustomerNotificationDbContract {
    public static final String DATABASE_NAME = "customer_notification";
    public static final String TABLE_NAME_NOTIFICATIONS = "notifications";
    public static final String TABLE_NOTIFICATIONS_FIRST_COLUMN_DATE = "date";
    public static final String TABLE_NOTIFICATIONS_SECOND_COLUMN_TITLE = "title";
    public static final String TABLE_NOTIFICATIONS_THIRD_COLUMN_MESSAGE = "message";
    public static final String TABLE_NOTIFICATIONS_FOURTH_COLUMN_READ_STATUS = "unread";
    public static final int READ_STATUS_UNREAD = 0;
    public static final int READ_STATUS_READ = 1;
    //SQLite does not have a separate Boolean storage class. Instead,
    //Boolean values are stored as integers 0 (false) and 1 (true).

}

