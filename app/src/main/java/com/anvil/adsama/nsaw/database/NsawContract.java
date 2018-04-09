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

    public static final class NsawEntry implements BaseColumns {

        public static final Uri NEWS_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();
        public static final Uri STOCK_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_STOCK).build();
        public static final String NEWS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;
        public static final String STOCK_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STOCK;

        public static final String TABLE_NAME_NEWS = "newsdata";
        public static final String TABLE_NAME_STOCK = "stockdata";

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

        public static final String[] NEWS_COLUMNS = {COLUMN_NEWS_ID, COLUMN_NEWS_AUTHOR, COLUMN_NEWS_TITLE, COLUMN_NEWS_DESCRIPTION, COLUMN_NEWS_URL, COLUMN_NEWS_DATE, COLUMN_NEWS_IMAGEURL};
        public static final String[] STOCK_COLUMNS = {COLUMN_STOCK_ID, COLUMN_STOCK_NAME, COLUMN_STOCK_REFRESH, COLUMN_STOCK_OPEN, COLUMN_STOCK_CLOSE, COLUMN_STOCK_HIGH, COLUMN_STOCK_LOW, COLUMN_STOCK_VOLUME};

        public static Uri buildNewsUri(long id) {
            return ContentUris.withAppendedId(NEWS_CONTENT_URI, id);
        }

        public static Uri buildStockUri(long id) {
            return ContentUris.withAppendedId(STOCK_CONTENT_URI, id);
        }

        public static final int COL_NEWS_ID = 0;
        public static final int COL_NEWS_AUTHOR = 1;
        public static final int COL_NEWS_TITLE = 2;
        public static final int COL_NEWS_DESCRIPTION = 3;
        public static final int COL_NEWS_URL = 4;
        public static final int COL_NEWS_DATE = 5;
        public static final int COL_NEWS_IMGURL = 6;

        public static final int COL_STOCK_ID = 0;
        public static final int COL_STOCK_NAME = 1;
        public static final int COL_STOCK_REFRESH = 2;
        public static final int COL_STOCK_OPEN = 3;
        public static final int COL_STOCK_CLOSE = 4;
        public static final int COL_STOCK_HIGH = 5;
        public static final int COL_STOCK_LOW = 6;
        public static final int COL_STOCK_VOLUME = 7;
    }
}