package ro.atoming.abnrnews.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static ro.atoming.abnrnews.data.NewsContract.CONTENT_AUTHORITY;
import static ro.atoming.abnrnews.data.NewsContract.NewsEntry;
import static ro.atoming.abnrnews.data.NewsContract.PATH_ARTICLE;

public class NewsProvider extends ContentProvider {

    public static final String LOG_TAG = NewsProvider.class.getSimpleName();

    public static final int ARTICLES = 100;
    public static final int ARTICLE_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMAtcher();
    private NewsDbHelper mNewsHelper;

    private static UriMatcher buildUriMAtcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //add the matcher for directory
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_ARTICLE, ARTICLES);
        //add the matcher for single article
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_ARTICLE + "/#", ARTICLE_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mNewsHelper = new NewsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortBy) {
        SQLiteDatabase db = mNewsHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ARTICLES:
                cursor = db.query(NewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortBy);
                break;
            case ARTICLE_ID:
                selection = NewsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(NewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortBy);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown Uri !");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ARTICLES:
                return NewsEntry.CONTENT_LIST_TYPE;
            case ARTICLE_ID:
                return NewsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mNewsHelper.getWritableDatabase();
        Uri returnUri;//this the uri to be returned.
        int match = sUriMatcher.match(uri);

        switch (match) {
            case ARTICLES:
                long id = db.insert(NewsEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(NewsEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mNewsHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case ARTICLES:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {

                        long id = db.insert(NewsEntry.TABLE_NAME, null, value);
                        if (id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mNewsHelper.getWritableDatabase();

        int rowsDeleted;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ARTICLES:
                rowsDeleted = db.delete(NewsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ARTICLE_ID:
                selection = NewsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(NewsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion not supported for " + uri);
        }
        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mNewsHelper.getWritableDatabase();
        int rowsUpdated;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ARTICLES:
                rowsUpdated = db.update(NewsEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                if (rowsUpdated != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsUpdated;
            case ARTICLE_ID:
                selection = NewsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(NewsEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                if (rowsUpdated != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsUpdated;
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
}
