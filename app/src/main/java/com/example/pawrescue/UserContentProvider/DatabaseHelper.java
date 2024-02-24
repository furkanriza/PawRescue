package com.example.pawrescue.UserContentProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pawrescue.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + UserContract.UserEntry.TABLE_NAME + " ("
                + UserContract.UserEntry.COLUMN_USER_ID + " TEXT, "
                + UserContract.UserEntry.COLUMN_NAME + " TEXT, "
                + UserContract.UserEntry.COLUMN_SURNAME + " TEXT, "
                + UserContract.UserEntry.COLUMN_PHONE_NUMBER + " TEXT, "
                + UserContract.UserEntry.COLUMN_EMAIL + " TEXT, "
                + UserContract.UserEntry.COLUMN_PASSWORD + " TEXT);";

        db.execSQL(CREATE_USERS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME);
        onCreate(db);
    }
}
