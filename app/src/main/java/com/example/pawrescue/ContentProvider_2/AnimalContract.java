package com.example.pawrescue.ContentProvider_2;

import android.net.Uri;
import android.provider.BaseColumns;

public final class AnimalContract {
    public static final String CONTENT_AUTHORITY = "com.example.pawrescue.provider";

    // Base URI to contact the Content Provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Path for the "animals" directory
    public static final String PATH_ANIMALS = "animals";

    public static final class AnimalEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ANIMALS);

        public static final String TABLE_NAME = "animals";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";

    }
}
