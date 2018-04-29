package com.anvil.adsama.nsaw.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class NsawProvider extends ContentProvider {

    private static final int NEWS = 300;
    private static final int STOCK = 301;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private NsawDbHelper mNsawDbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = NsawContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, NsawContract.PATH_NEWS, NEWS);
        matcher.addURI(authority, NsawContract.PATH_STOCK, STOCK);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mNsawDbHelper = new NsawDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case NEWS: {
                cursor = mNsawDbHelper.getReadableDatabase().query(NsawContract.NsawEntry.TABLE_NAME_NEWS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case STOCK: {
                cursor = mNsawDbHelper.getReadableDatabase().query(NsawContract.NsawEntry.TABLE_NAME_STOCK, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS:
                return NsawContract.NsawEntry.NEWS_CONTENT_TYPE;
            case STOCK:
                return NsawContract.NsawEntry.STOCK_CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = mNsawDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case NEWS: {
                long id = db.insert(NsawContract.NsawEntry.TABLE_NAME_NEWS, null, values);
                if (id > 0) {
                    returnUri = NsawContract.NsawEntry.buildNewsUri(id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into news " + uri);
                }
                break;
            }
            case STOCK: {
                long id = db.insert(NsawContract.NsawEntry.TABLE_NAME_STOCK, null, values);
                if (id > 0) {
                    returnUri = NsawContract.NsawEntry.buildStockUri(id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into stock " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mNsawDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if (null == selection) {
            selection = "1";
        }
        switch (match) {
            case NEWS:
                rowsDeleted = db.delete(NsawContract.NsawEntry.TABLE_NAME_NEWS, selection, selectionArgs);
                break;
            case STOCK:
                rowsDeleted = db.delete(NsawContract.NsawEntry.TABLE_NAME_STOCK, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mNsawDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case NEWS:
                rowsUpdated = db.update(NsawContract.NsawEntry.TABLE_NAME_NEWS, values, selection, selectionArgs);
                break;
            case STOCK:
                rowsUpdated = db.update(NsawContract.NsawEntry.TABLE_NAME_STOCK, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}