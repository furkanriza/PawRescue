package com.example.pawrescue.ContentProvider_2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AnimalProvider extends ContentProvider {
    private static final int ANIMALS = 100;
    private static final int ANIMAL_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AnimalContract.CONTENT_AUTHORITY, AnimalContract.PATH_ANIMALS, ANIMALS);
        sUriMatcher.addURI(AnimalContract.CONTENT_AUTHORITY, AnimalContract.PATH_ANIMALS + "/#", ANIMAL_ID);
    }

    private AnimalDbHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new AnimalDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        if (match == ANIMALS) {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            long id = db.insert(AnimalContract.AnimalEntry.TABLE_NAME, null, values);
            if (id == -1) {
                return null; // Insertion failed
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return ContentUris.withAppendedId(uri, id);
        }
        throw new IllegalArgumentException("Insertion is not supported for " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ANIMALS:
                rowsDeleted = db.delete(AnimalContract.AnimalEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ANIMAL_ID:
                selection = AnimalContract.AnimalEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = db.delete(AnimalContract.AnimalEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ANIMALS:
                return updateAnimal(uri, values, selection, selectionArgs);
            case ANIMAL_ID:
                selection = AnimalContract.AnimalEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateAnimal(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateAnimal(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated = db.update(AnimalContract.AnimalEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
