package ro.atoming.abnrnews.network;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import ro.atoming.abnrnews.data.NewsContract;
import ro.atoming.abnrnews.utils.NotificationUtils;

public class ArticleSyncTask {
    private static final String LOG_TAG = ArticleSyncTask.class.getSimpleName();

    synchronized public static void syncNews(Context context) {
        try {
            ContentResolver articlesContentResolver = context.getContentResolver();
            articlesContentResolver.delete(
                    NewsContract.NewsEntry.CONTENT_URI,
                    null,
                    null);

            String[] categoryArray = new String[]{
                    QueryUtils.CATEGORY_GENERAL,
                    QueryUtils.CATEGORY_ENTERTAINMENT,
                    QueryUtils.CATEGORY_BUSINESS,
                    QueryUtils.CATEGORY_SPORTS,
                    QueryUtils.CATEGORY_SCIENCE,
                    QueryUtils.CATEGORY_TECHNOLOGY,
                    QueryUtils.CATEGORY_HEALTH
            };
            ContentValues[] newsValues = null;
            for (int i = 0; i < categoryArray.length; i++) {
                String categoryArticle = categoryArray[i];
                String jsonResponse = QueryUtils.fetchNews(categoryArticle);
                newsValues = QueryUtils.getArticleValuesFromJsonResponse(jsonResponse, categoryArticle);

                if (newsValues != null && newsValues.length != 0) {

                    articlesContentResolver.bulkInsert(
                            NewsContract.NewsEntry.CONTENT_URI,
                            newsValues);
                }
            }
            NotificationUtils.notifyUserOfUpdate(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
