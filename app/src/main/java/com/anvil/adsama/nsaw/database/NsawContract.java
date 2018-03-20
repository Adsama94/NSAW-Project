package com.anvil.adsama.nsaw.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class NsawContract {

    public static final String CONTENT_AUTHORITY = "com.anvil.adsama.nsaw";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_NEWS = "news";
    public static final String PATH_STOCK = "stock";
    public static final String PATH_WEATHER = "weather";

    public static final class NsawEntry implements BaseColumns {

        public static final Uri NEWS_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();
        public static final Uri STOCK_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_STOCK).build();
        public static final Uri WEATHER_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER).build();
        public static final String NEWS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;
        public static final String STOCK_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STOCK;
        public static final String WEATHER_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;

        public static final String TABLE_NAME = "nsawdata";

        public static Uri buildNewsUri(long id) {
            return ContentUris.withAppendedId(NEWS_CONTENT_URI, id);
        }

        public static Uri buildStockUri(long id) {
            return ContentUris.withAppendedId(STOCK_CONTENT_URI, id);
        }

        public static Uri buildWeatherUri(long id) {
            return ContentUris.withAppendedId(WEATHER_CONTENT_URI, id);
        }
    }
}