package com.anvil.adsama.nsaw.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NsawDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "nsaw.db";

    public NsawDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_NSAW_TABLE = "CREATE TABLE " + NsawContract.NsawEntry.TABLE_NAME + " (" +
                NsawContract.NsawEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + " );";
        db.execSQL(SQL_CREATE_NSAW_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NsawContract.NsawEntry.TABLE_NAME);
        onCreate(db);
    }
}