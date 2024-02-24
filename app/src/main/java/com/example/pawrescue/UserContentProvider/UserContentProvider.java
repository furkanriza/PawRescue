package com.example.pawrescue.UserContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pawrescue.Model.User;

public class UserContentProvider extends ContentProvider {

    // Database helper object
    private SQLiteUserRepository userRepository;
    private static final int USERS = 100;
    private static final int USER_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // The content URI of the form "content://com.example.pawrescue.userprovider/users" will map to the
        // integer code USERS
        sUriMatcher.addURI(UserContract.AUTHORITY, UserContract.PATH_USERS, USERS);

        // The content URI of the form "content://com.example.pawrescue.userprovider/users/#" will map to the
        // integer code USER_ID
        sUriMatcher.addURI(UserContract.AUTHORITY, UserContract.PATH_USERS + "/#", USER_ID);
    }

    @Override
    public boolean onCreate() {
        userRepository = SQLiteUserRepository.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case USERS:
                cursor = userRepository.queryUsers(projection, selection, selectionArgs, sortOrder);
                break;
            case USER_ID:
                selection = UserContract.UserEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = userRepository.queryUsers(projection, selection, selectionArgs, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case USERS:
                return UserContract.UserEntry.CONTENT_LIST_TYPE;
            case USER_ID:
                return UserContract.UserEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + sUriMatcher.match(uri));
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case USERS:
                long id = 0;
                try {
                    userRepository.addUser(ContentValuesToUser(values));
                } catch (SQLiteException e) {
                    throw new RuntimeException(e);
                }
                if (id == -1) {
                    Log.e("UserContentProvider", "Insertion failed for " + uri);
                    return null;
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case USERS:
                try {
                    userRepository.deleteUser(selection);
                } catch (SQLiteException e) {
                    throw new RuntimeException(e);
                }
                break;
            case USER_ID:
                selection = UserContract.UserEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                try {
                    userRepository.deleteUser(selection);
                } catch (SQLiteException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        return 1;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        User user = ContentValuesToUser(values);
        switch (sUriMatcher.match(uri)) {
            case USERS:
                try {
                    userRepository.updateUser(user);
                } catch (SQLiteException e) {
                    throw new RuntimeException(e);
                }
                break;
            case USER_ID:
                selection = UserContract.UserEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                try {
                    userRepository.updateUser(user);
                } catch (SQLiteException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
        return 1;
    }

    // Helper method to convert ContentValues to User object
    private User ContentValuesToUser(ContentValues values) {
        String id = values.getAsString(UserContract.UserEntry.COLUMN_USER_ID);
        String name = values.getAsString(UserContract.UserEntry.COLUMN_NAME);
        String surname = values.getAsString(UserContract.UserEntry.COLUMN_SURNAME);
        String phoneNumber = values.getAsString(UserContract.UserEntry.COLUMN_PHONE_NUMBER);
        String email = values.getAsString(UserContract.UserEntry.COLUMN_EMAIL);
        String password = values.getAsString(UserContract.UserEntry.COLUMN_PASSWORD);

        User user = new User(id, name, surname, phoneNumber, email, password);

        return user;
    }

    public static User fetchUser(Uri userUri, Context context) {
        String[] projection = {
                UserContract.UserEntry.COLUMN_USER_ID,
                UserContract.UserEntry.COLUMN_NAME,
                UserContract.UserEntry.COLUMN_SURNAME,
                UserContract.UserEntry.COLUMN_PHONE_NUMBER,
                UserContract.UserEntry.COLUMN_EMAIL,
                UserContract.UserEntry.COLUMN_PASSWORD // Be cautious with handling passwords
        };

        Cursor cursor = null;
        User user = null;

        try {
            cursor = context.getContentResolver().query(userUri, projection, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_USER_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME));
                String surname = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_SURNAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_PHONE_NUMBER));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_PASSWORD));

                user = new User(id, name, surname, phoneNumber, email, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }

}
