package ro.atoming.abnrnews.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static ro.atoming.abnrnews.data.NewsContract.*;

public class NewsDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "news.db";
    public static final int DATABASE_VERSION = 1;
    private static final String CREATE_ENTRIES = "CREATE TABLE " +
            NewsEntry.TABLE_NAME + " (" +
            NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            NewsEntry.COLUMN_SOURCE_ID + " TEXT ," +
            NewsEntry.COLUMN_SOURCE_NAME + " TEXT ," +
            NewsEntry.COLUMN_ARTICLE_AUTHOR + " TEXT ," +
            NewsEntry.COLUMN_ARTICLE_TITLE + " TEXT ," +
            NewsEntry.COLUMN_ARTICLE_DESCRIPTION + " TEXT ," +
            NewsEntry.COLUMN_ARTICLE_URL + " TEXT ," +
            NewsEntry.COLUMN_ARTICLE_IMAGE + " TEXT ," +
            NewsEntry.COLUMN_ARTICLE_DATE + " TEXT ," +
            NewsEntry.COLUMN_ARTICLE_CATEGORY + " TEXT );";

    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
