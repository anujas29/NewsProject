package anuja.project.finalproject.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by USER on 02-12-2017.
 */

public class NewsContract {
    public static final String AUTHORITY = "com.anuja.finalproject";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TASKS = "news";

    public static final class NewsEntry implements BaseColumns {

        public static final String TABLE_NAME = "news";


        public static Uri getUriWithId(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static Uri buildSaleUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;

        public static final String COLUMN_ID = "news_id";
        public static final String COLUMN_URL="url";
        public static final String COLUMN_SITE= "site";
        public static final String COLUMN_TITLE="title";
        public static final String COLUMN_DATE="date";
        public static final String COLUMN_IMAGE="image_url";
        public static final String COLUMN_TEXT="text";
        public static final String COLUMN_GLOBAL="global";
        public static final String COLUMN_LOCAL="local";
        public static final String COLUMN_FAV="fav";

        public static final String[] NEWS_COLUMNS = {
                _ID,
                COLUMN_ID,
                COLUMN_URL,
                COLUMN_SITE,
                COLUMN_TITLE,
                COLUMN_DATE,
                COLUMN_IMAGE,
                COLUMN_TEXT,
                COLUMN_GLOBAL,
                COLUMN_LOCAL,
                COLUMN_FAV
        };

        public static final String[] WIDGET_COLUMNS = {
                _ID,
                COLUMN_TITLE
        };

    }

    public static final String SQL_CREATE_NEWS_TABLE = "CREATE TABLE IF NOT EXISTS "+
            NewsEntry.TABLE_NAME+"("+
            NewsEntry._ID+" INTEGER PRIMARY KEY,"+
            NewsEntry.COLUMN_ID+" TEXT NOT NULL,"+
            NewsEntry.COLUMN_URL+" TEXT NOT NULL,"+
            NewsEntry.COLUMN_SITE+" TEXT NOT NULL,"+
            NewsEntry.COLUMN_TITLE+" TEXT NOT NULL,"+
            NewsEntry.COLUMN_DATE+" TEXT NOT NULL,"+
            NewsEntry.COLUMN_IMAGE+" TEXT NOT NULL,"+
            NewsEntry.COLUMN_TEXT+" TEXT NOT NULL,"+
            NewsEntry.COLUMN_GLOBAL+" INTEGER DEFAULT 0,"+
            NewsEntry.COLUMN_LOCAL+" INTEGER DEFAULT 0,"+
            NewsEntry.COLUMN_FAV+" INTEGER DEFAULT 0"+
            ");";

}
