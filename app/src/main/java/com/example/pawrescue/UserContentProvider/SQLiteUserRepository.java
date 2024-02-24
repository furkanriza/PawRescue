package com.example.pawrescue.UserContentProvider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.content.Context;
import com.example.pawrescue.Model.User;
import android.database.sqlite.SQLiteException;


public class SQLiteUserRepository implements UserRepository {

    private static SQLiteUserRepository instance;
    private DatabaseHelper dbHelper;

    // Private constructor with Context parameter
    private SQLiteUserRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Static method to get instance
    public static synchronized SQLiteUserRepository getInstance(Context context) {
        if (instance == null) {
            instance = new SQLiteUserRepository(context);
        }
        return instance;
    }

    public static SQLiteUserRepository getInstance() {
        return instance;
    }

    @Override
    public void addUser(User user) throws SQLiteException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_USER_ID, user.getId());
        values.put(UserContract.UserEntry.COLUMN_NAME, user.getName());
        values.put(UserContract.UserEntry.COLUMN_SURNAME, user.getSurname());
        values.put(UserContract.UserEntry.COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        values.put(UserContract.UserEntry.COLUMN_EMAIL, user.getEmail());
        values.put(UserContract.UserEntry.COLUMN_PASSWORD, user.getPassword());

        long id = db.insert(UserContract.UserEntry.TABLE_NAME, null, values);
        if (id == -1) {
            // Handle error
        }
        db.close();
    }


    @Override
    public boolean checkUser(String email, String password) throws SQLiteException {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT id FROM users WHERE email = ? AND password = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{email, password});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    @Override
    public void updateUser(User user) throws SQLiteException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_USER_ID, user.getId());
        values.put(UserContract.UserEntry.COLUMN_NAME, user.getName());
        values.put(UserContract.UserEntry.COLUMN_SURNAME, user.getSurname());
        values.put(UserContract.UserEntry.COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        values.put(UserContract.UserEntry.COLUMN_PASSWORD, user.getPassword());

        String selection = UserContract.UserEntry.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { user.getEmail() };

        int count = db.update(
                UserContract.UserEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        if (count < 1) {
            // Handle the case where the user isn't updated
        }
        db.close();
    }

    @Override
    public void deleteUser(String email) throws SQLiteException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = UserContract.UserEntry.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };

        int count = db.delete(
                UserContract.UserEntry.TABLE_NAME,
                selection,
                selectionArgs);

        if (count < 1) {
            // Handle the case where no rows are deleted
        }
        db.close();
    }


    public Cursor queryUsers(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(UserContract.UserEntry.TABLE_NAME);

        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

}