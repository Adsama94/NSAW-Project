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
    public static final String PATH_CURRENT = "weather_current";
    public static final String PATH_DAILY = "weather_daily";

    public static final class NsawEntry implements BaseColumns {

        public static final Uri NEWS_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();
        public static final Uri STOCK_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_STOCK).build();
        public static final Uri WEATHER_CURRENT_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CURRENT).build();
        public static final Uri WEATHER_DAILY_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DAILY).build();
        public static final String NEWS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;
        public static final String STOCK_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STOCK;
        public static final String WEATHER_CURRENT_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CURRENT;
        public static final String WEATHER_DAILY_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CURRENT;

        public static final String TABLE_NAME_NEWS = "newsdata";
        public static final String TABLE_NAME_STOCK = "stockdata";
        public static final String TABLE_NAME_CURRENT = "currentdata";
        public static final String TABLE_NAME_DAILY = "dailydata";

        public static final String COLUMN_NEWS_ID = BaseColumns._ID;
        public static final String COLUMN_NEWS_AUTHOR = "author";
        public static final String COLUMN_NEWS_TITLE = "title";
        public static final String COLUMN_NEWS_DESCRIPTION = "description";
        public static final String COLUMN_NEWS_URL = "article_url";
        public static final String COLUMN_NEWS_DATE = "upload_date";
        public static final String COLUMN_NEWS_IMAGEURL = "image_url";

        public static final String COLUMN_STOCK_ID = BaseColumns._ID;
        public static final String COLUMN_STOCK_NAME = "company_name";
        public static final String COLUMN_STOCK_REFRESH = "refresh_time";
        public static final String COLUMN_STOCK_OPEN = "stock_open";
        public static final String COLUMN_STOCK_CLOSE = "stock_close";
        public static final String COLUMN_STOCK_HIGH = "stock_high";
        public static final String COLUMN_STOCK_LOW = "stock_low";
        public static final String COLUMN_STOCK_VOLUME = "stock_volume";

        public static final String COLUMN_CURRENT_ID = BaseColumns._ID;
        public static final String COLUMN_CURRENT_SUMMARY = "current_summary";
        public static final String COLUMN_CURRENT_WEEKLY = "weekly_summary";
        public static final String COLUMN_CURRENT_TEMPERATURE = "current_temperature";
        public static final String COLUMN_CURRENT_ICON = "current_icon";
        public static final String COLUMN_CURRENT_WIND = "wind_speed";
        public static final String COLUMN_CURRENT_VISIBILITY = "current_visibility";

        public static final String COLUMN_DAILY_ID = BaseColumns._ID;
        public static final String COLUMN_DAILY_TIME = "daily_time";
        public static final String COLUMN_DAILY_SUMMARY = "daily_summary";
        public static final String COLUMN_DAILY_LOW = "temp_low";
        public static final String COLUMN_DAILY_HIGH = "temp_high";
        public static final String COLUMN_DAILY_DEW = "dew_point";
        public static final String COLUMN_DAILY_APPARENT_HIGH = "apparent_high";
        public static final String COLUMN_DAILY_APPARENT_LOW = "apparent_low";
        public static final String COLUMN_DAILY_HUMIDITY = "daily_humidity";
        public static final String COLUMN_DAILY_VISIBILITY = "daily_visibility";

        public static final String[] NEWS_COLUMNS = {COLUMN_NEWS_ID, COLUMN_NEWS_AUTHOR, COLUMN_NEWS_TITLE, COLUMN_NEWS_DESCRIPTION, COLUMN_NEWS_URL, COLUMN_NEWS_DATE, COLUMN_NEWS_IMAGEURL};
        public static final String[] STOCK_COLUMNS = {COLUMN_STOCK_ID, COLUMN_STOCK_NAME, COLUMN_STOCK_REFRESH, COLUMN_STOCK_OPEN, COLUMN_STOCK_CLOSE, COLUMN_STOCK_HIGH, COLUMN_STOCK_LOW, COLUMN_STOCK_VOLUME};
        public static final String[] CURRENT_COLUMNS = {COLUMN_CURRENT_ID, COLUMN_CURRENT_SUMMARY, COLUMN_CURRENT_WEEKLY, COLUMN_CURRENT_TEMPERATURE, COLUMN_CURRENT_ICON, COLUMN_CURRENT_WIND, COLUMN_CURRENT_VISIBILITY};
        public static final String[] DAILY_COLUMNS = {COLUMN_DAILY_ID, COLUMN_DAILY_TIME, COLUMN_DAILY_SUMMARY, COLUMN_DAILY_LOW, COLUMN_DAILY_HIGH, COLUMN_DAILY_APPARENT_HIGH, COLUMN_DAILY_APPARENT_LOW, COLUMN_DAILY_DEW, COLUMN_DAILY_HUMIDITY, COLUMN_DAILY_VISIBILITY};


        public static Uri buildNewsUri(long id) {
            return ContentUris.withAppendedId(NEWS_CONTENT_URI, id);
        }

        public static Uri buildStockUri(long id) {
            return ContentUris.withAppendedId(STOCK_CONTENT_URI, id);
        }

        public static Uri buildWeatherCurrentUri(long id) {
            return ContentUris.withAppendedId(WEATHER_CURRENT_CONTENT_URI, id);
        }

        public static Uri buildWeatherDailyUri(long id) {
            return ContentUris.withAppendedId(WEATHER_DAILY_CONTENT_URI, id);
        }
    }
}