package ro.atoming.abnrnews.network;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import ro.atoming.abnrnews.data.NewsContract;

public class ArticleSyncTask {
    private static final String LOG_TAG = ArticleSyncTask.class.getSimpleName();

    synchronized public static void syncHeadlineNews(Context context) {
        try {


            String jsonResponse = QueryUtils.fetchNews(QueryUtils.CATEGORY_SPORTS);
            ContentValues[] newsValues = QueryUtils.getArticleValuesFromJsonResponse(jsonResponse);
            if (newsValues != null && newsValues.length != 0) {
                ContentResolver articlesContentResolver = context.getContentResolver();
                articlesContentResolver.bulkInsert(
                        NewsContract.NewsEntry.CONTENT_URI,
                        newsValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
