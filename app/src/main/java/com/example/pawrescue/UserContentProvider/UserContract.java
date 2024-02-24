package com.example.pawrescue.UserContentProvider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class UserContract {

    // Authority of the content provider
    public static final String AUTHORITY = "com.example.pawrescue.userprovider";
    // Base URI for the content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Path for the "user" directory
    public static final String PATH_USERS = "users";

    private UserContract() {}

    // Inner class that defines the table contents
    public static final class UserEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USERS);

        // MIME type for lists of users
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_USERS;
        // MIME type for a single user
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_USERS;

        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USER_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SURNAME = "surname";
        public static final String COLUMN_PHONE_NUMBER = "phoneNumber";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        // Additional columns can be added as needed
    }
}

