package ro.atoming.abnrnews.ui.fragments;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import ro.atoming.abnrnews.data.NewsContract;

import static ro.atoming.abnrnews.data.NewsProvider.LOG_TAG;

public class FragmentUtils {

    private static Context mContext;

    public static Loader<Cursor> fragmentLoader(Context context, String category) {
        String[] projection = {NewsContract.NewsEntry.COLUMN_ARTICLE_TITLE,
                NewsContract.NewsEntry.COLUMN_ARTICLE_DESCRIPTION,
                NewsContract.NewsEntry.COLUMN_SOURCE_NAME,
                NewsContract.NewsEntry.COLUMN_ARTICLE_DATE,
                NewsContract.NewsEntry.COLUMN_ARTICLE_IMAGE,
                NewsContract.NewsEntry.COLUMN_ARTICLE_URL
        };
        String selection = NewsContract.NewsEntry.COLUMN_ARTICLE_CATEGORY + " = ?";
        String[] selectionArgs = {category};
        CursorLoader newsLoader = new CursorLoader(context,
                NewsContract.NewsEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
        if (newsLoader == null) {
            Log.d(LOG_TAG, "THE CURSOR LOADER IS NULL!!!!");
        }
        return newsLoader;
    }

}
