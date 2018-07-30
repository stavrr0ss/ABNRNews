package ro.atoming.abnrnews.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class NewsContract {
    public static final String CONTENT_AUTHORITY = "ro.atoming.abnrnews";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ARTICLE = "article";

    private NewsContract() {
    }

    public static class NewsEntry implements BaseColumns {

        //TODO : complete the database also with trailer details
        public static final String TABLE_NAME = "news";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_SOURCE_ID = "sourceId";
        public static final String COLUMN_SOURCE_NAME = "sourceName";
        public static final String COLUMN_ARTICLE_TITLE = "title";
        public static final String COLUMN_ARTICLE_AUTHOR = "author";
        public static final String COLUMN_ARTICLE_DESCRIPTION = "description";
        public static final String COLUMN_ARTICLE_URL = "url";
        public static final String COLUMN_ARTICLE_IMAGE = "image";
        public static final String COLUMN_ARTICLE_DATE = "date";
        public static final String COLUMN_ARTICLE_CATEGORY = "category";


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ARTICLE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLE;
    }
}
