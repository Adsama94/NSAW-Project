package com.anvil.adsama.nsaw.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NsawDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "nsaw.db";

    public NsawDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_NEWS_TABLE = "CREATE TABLE " + NsawContract.NsawEntry.TABLE_NAME_NEWS + " (" +
                NsawContract.NsawEntry.COLUMN_NEWS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NsawContract.NsawEntry.COLUMN_NEWS_AUTHOR + " TEXT DEFAULT '', " +
                NsawContract.NsawEntry.COLUMN_NEWS_TITLE + " TEXT DEFAULT '', " +
                NsawContract.NsawEntry.COLUMN_NEWS_DESCRIPTION + " TEXT DEFAULT '', " +
                NsawContract.NsawEntry.COLUMN_NEWS_URL + " TEXT DEFAULT '', " +
                NsawContract.NsawEntry.COLUMN_NEWS_DATE + " TEXT DEFAULT '', " +
                NsawContract.NsawEntry.COLUMN_NEWS_IMAGEURL + " TEXT DEFAULT ''" +
                " );";


        final String SQL_CREATE_STOCK_TABLE = "CREATE TABLE " + NsawContract.NsawEntry.TABLE_NAME_STOCK + " (" +
                NsawContract.NsawEntry.COLUMN_STOCK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NsawContract.NsawEntry.COLUMN_STOCK_NAME + " TEXT DEFAULT '', " +
                NsawContract.NsawEntry.COLUMN_STOCK_REFRESH + " TEXT DEFAULT '', " +
                NsawContract.NsawEntry.COLUMN_STOCK_OPEN + " FLOAT DEFAULT 0, " +
                NsawContract.NsawEntry.COLUMN_STOCK_CLOSE + " FLOAT DEFAULT 0, " +
                NsawContract.NsawEntry.COLUMN_STOCK_HIGH + " FLOAT DEFAULT 0, " +
                NsawContract.NsawEntry.COLUMN_STOCK_LOW + " FLOAT DEFAULT 0, " +
                NsawContract.NsawEntry.COLUMN_STOCK_VOLUME + " FLOAT DEFAULT 0" +
                " );";


        final String SQL_CREATE_CURRENT_TABLE = "CREATE TABLE " + NsawContract.NsawEntry.TABLE_NAME_CURRENT + " (" +
                NsawContract.NsawEntry.COLUMN_CURRENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NsawContract.NsawEntry.COLUMN_CURRENT_SUMMARY + " TEXT DEFAULT '', " +
                NsawContract.NsawEntry.COLUMN_CURRENT_WEEKLY + " TEXT DEFAULT '', " +
                NsawContract.NsawEntry.COLUMN_CURRENT_TEMPERATURE + " FLOAT DEFAULT 0, " +
                NsawContract.NsawEntry.COLUMN_CURRENT_ICON + " TEXT DEFAULT '', " +
                NsawContract.NsawEntry.COLUMN_CURRENT_WIND + " FLOAT DEFAULT 0, " +
                NsawContract.NsawEntry.COLUMN_CURRENT_VISIBILITY + " FLOAT DEFAULT 0" +
                " );";


        final String SQL_CREATE_DAILY_TABLE = "CREATE TABLE " + NsawContract.NsawEntry.TABLE_NAME_DAILY + " (" +
                NsawContract.NsawEntry.COLUMN_DAILY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NsawContract.NsawEntry.COLUMN_DAILY_TIME + " LONG DEFAULT 0, " +
                NsawContract.NsawEntry.COLUMN_DAILY_SUMMARY + " TEXT DEFAULT '', " +
                NsawContract.NsawEntry.COLUMN_DAILY_LOW + " FLOAT DEFAULT 0, " +
                NsawContract.NsawEntry.COLUMN_DAILY_HIGH + " FLOAT DEFAULT 0, " +
                NsawContract.NsawEntry.COLUMN_DAILY_APPARENT_LOW + " FLOAT DEFAULT 0, " +
                NsawContract.NsawEntry.COLUMN_DAILY_APPARENT_HIGH + " FLOAT DEFAULT 0, " +
                NsawContract.NsawEntry.COLUMN_DAILY_DEW + " FLOAT DEFAULT 0, " +
                NsawContract.NsawEntry.COLUMN_DAILY_HUMIDITY + " FLOAT DEFAULT 0, " +
                NsawContract.NsawEntry.COLUMN_DAILY_VISIBILITY + " FLOAT DEFAULT 0 " +
                " );";


        db.execSQL(SQL_CREATE_NEWS_TABLE);
        db.execSQL(SQL_CREATE_STOCK_TABLE);
        db.execSQL(SQL_CREATE_CURRENT_TABLE);
        db.execSQL(SQL_CREATE_DAILY_TABLE);
        Log.d(NsawDbHelper.class.getSimpleName(), "TABLES CREATED " + SQL_CREATE_NEWS_TABLE + SQL_CREATE_STOCK_TABLE + SQL_CREATE_CURRENT_TABLE + SQL_CREATE_DAILY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NsawContract.NsawEntry.TABLE_NAME_NEWS);
        db.execSQL("DROP TABLE IF EXISTS " + NsawContract.NsawEntry.TABLE_NAME_STOCK);
        db.execSQL("DROP TABLE IF EXISTS " + NsawContract.NsawEntry.TABLE_NAME_CURRENT);
        db.execSQL("DROP TABLE IF EXISTS " + NsawContract.NsawEntry.TABLE_NAME_DAILY);
        onCreate(db);
    }
}