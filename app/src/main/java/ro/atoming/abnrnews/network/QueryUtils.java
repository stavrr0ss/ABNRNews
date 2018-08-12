package ro.atoming.abnrnews.network;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ro.atoming.abnrnews.R;
import ro.atoming.abnrnews.model.Article;
import ro.atoming.abnrnews.model.ArticleSource;

import static ro.atoming.abnrnews.data.NewsContract.NewsEntry;

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final String ARTICLES_ARRAY = "articles";
    private static final String ARTICLE_SOURCE = "source";
    private static final String SOURCE_ID = "id";
    private static final String SOURCE_NAME = "name";
    private static final String ARTICLE_AUTHOR = "author";
    private static final String ARTICLE_TITLE = "title";
    private static final String ARTICLE_DESCRIPTION = "description";
    private static final String ARTICLE_URL = "url";
    private static final String ARTICLE_IMAGE = "urlToImage";
    private static final String ARTICLE_DATE = "publishedAt";

    public static final String HEADLINE_BASE_URL = "https://newsapi.org/v2/top-headlines";
    public static final String EVERYTHING_BASE_URL = "https://newsapi.org/v2/everything";
    public static final String API_KEY = "";
    public static final String api_key = "apiKey";

    //Headlines search parameters
    public static final String CATEGORY = "category";
    public static final String CATEGORY_BUSINESS = "business";
    public static final String CATEGORY_ENTERTAINMENT = "entertainment";
    public static final String CATEGORY_SPORTS = "sports";
    public static final String CATEGORY_HEALTH = "health";
    public static final String CATEGORY_SCIENCE = "science";
    public static final String CATEGORY_TECHNOLOGY = "technology";
    public static final String CATEGORY_GENERAL = "general";

    public static final String COUNTRY = "country";
    public static final String pageSize = "pageSize";
    public static final String query = "q";



    // private static Context context ;

    //helper method to build the desired url by category and country(later)
    public static String buildNewsUri(String category, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences
                (context);
        String countryPref = sharedPreferences.getString(context.getString(R.string.pref_country_key), context.getString(R.string.key_country_UnitedStates));
        String articleNumbersPref = sharedPreferences.getString(context.getString(R.string.pref_pageSize_key),
                context.getString(R.string.pref_pageSize_defaultValue));

        Uri buildUri = Uri.parse(HEADLINE_BASE_URL).buildUpon()
                .appendQueryParameter(CATEGORY,category)
                .appendQueryParameter(pageSize, articleNumbersPref)
                .appendQueryParameter(COUNTRY, countryPref)
                .appendQueryParameter(api_key, API_KEY)
                .build();
        Log.d(LOG_TAG, "THIS IS THE BUILD URL !!!!!!! : " + buildUri.toString());
        return buildUri.toString();
    }

    /**
     * helper method fot building the searchQuery
     *
     * @param searchTerm is the query inserted by the user
     * @param context    the Activity context
     * @return
     */
    public static String buildQueryUri(String searchTerm, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences
                (context);
        String articleNumbersPref = sharedPreferences.getString(context.getString(R.string.pref_pageSize_key),
                context.getString(R.string.pref_pageSize_defaultValue));

        Uri buildUri = Uri.parse(EVERYTHING_BASE_URL).buildUpon()
                .appendQueryParameter(pageSize, articleNumbersPref)
                .appendQueryParameter(query, searchTerm)
                .appendQueryParameter(api_key, API_KEY)
                .build();
        Log.d(LOG_TAG, "THIS IS THE QUERY URL !!!!!!! : " + buildUri.toString());
        return buildUri.toString();
    }

    public static String fetchNews(String category, Context context) {
        URL url = buildUrl(buildNewsUri(category, context));
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG,"Problem with HTTP response !",e);
        }
        return jsonResponse;
    }

    /**
     * method used to extract a List of articles for the My News Fragment
     */
    public static List<Article> queryArticles(String searchTerm, Context context) {
        URL url = buildUrl(buildQueryUri(searchTerm, context));
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with HTTP response !", e);
        }
        List<Article> articleList = getArticlesFromJson(jsonResponse);
        return articleList;
    }


    public static URL buildUrl(String stringUrl) {
        URL returnUrl = null;
        try {
            returnUrl = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return returnUrl;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
                Log.v(LOG_TAG,"WE HAVE CONNECTION !!!!!!!!!!!!");
                inputStream = httpURLConnection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\A");
                if (scanner.hasNext()) {
                    jsonResponse = scanner.next();
                    return jsonResponse;
                } else {
                    return null;
                }
            } else {
                Log.v(LOG_TAG, "HTTP response is " + httpURLConnection.getResponseCode());
            }
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Method for extracting the Json response directly to Db.
     *
     * @param jsonResponse
     * @param category
     * @return
     */
    public static ContentValues[] getArticleValuesFromJsonResponse(String jsonResponse, String category) {

        ContentValues[] articleValues = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray articlesArray = jsonObject.getJSONArray(ARTICLES_ARRAY);
            articleValues = new ContentValues[articlesArray.length()];
            for (int i = 0; i < articlesArray.length(); i++) {
                JSONObject article = articlesArray.getJSONObject(i);
                JSONObject source = article.getJSONObject(ARTICLE_SOURCE);
                String sourceId = source.optString(SOURCE_ID);
                String sourceName = source.optString(SOURCE_NAME);
                String author = article.optString(ARTICLE_AUTHOR);
                String title = article.optString(ARTICLE_TITLE);
                String description = article.optString(ARTICLE_DESCRIPTION);
                String url = article.optString(ARTICLE_URL);
                String image = article.optString(ARTICLE_IMAGE);
                String date = article.optString(ARTICLE_DATE);

                ContentValues contentValues = new ContentValues();
                contentValues.put(NewsEntry.COLUMN_SOURCE_ID, sourceId);
                contentValues.put(NewsEntry.COLUMN_SOURCE_NAME, sourceName);
                contentValues.put(NewsEntry.COLUMN_ARTICLE_AUTHOR, author);
                contentValues.put(NewsEntry.COLUMN_ARTICLE_TITLE, title);
                contentValues.put(NewsEntry.COLUMN_ARTICLE_DESCRIPTION, description);
                contentValues.put(NewsEntry.COLUMN_ARTICLE_URL, url);
                contentValues.put(NewsEntry.COLUMN_ARTICLE_IMAGE, image);
                contentValues.put(NewsEntry.COLUMN_ARTICLE_DATE, date);
                contentValues.put(NewsEntry.COLUMN_ARTICLE_CATEGORY, category);//for testing purposes

                articleValues[i] = contentValues;
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error with Json Response !!!");
        }
        return articleValues;
    }

    /**
     * method used to extract the Json response in an Article List for using in My News Fragment
     *
     * @param jsonResponse
     * @return
     */

    public static List<Article> getArticlesFromJson(String jsonResponse) {
        String author = "";
        String title = "";
        String description = "";
        String url = "";
        String image = "";
        String date = "";
        ArticleSource articleSource = null;
        String sourceId = "";
        String sourceName = "";
        Article returnedArticle = null;
        List<Article> articleList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray articlesArray = jsonObject.getJSONArray(ARTICLES_ARRAY);

            for (int i = 0; i < articlesArray.length(); i++) {
                JSONObject article = articlesArray.getJSONObject(i);
                JSONObject source = article.getJSONObject(ARTICLE_SOURCE);
                sourceId = source.optString(SOURCE_ID);
                sourceName = source.optString(SOURCE_NAME);

                articleSource = new ArticleSource(sourceId,sourceName);

                author = article.optString(ARTICLE_AUTHOR);
                title = article.optString(ARTICLE_TITLE);
                description = article.optString(ARTICLE_DESCRIPTION);
                url = article.optString(ARTICLE_URL);
                image = article.optString(ARTICLE_IMAGE);
                date = article.optString(ARTICLE_DATE);
                returnedArticle = new Article(articleSource, author, title, description, url, image, date);
                articleList.add(returnedArticle);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error with Json Response !!!");
        }
        return articleList;
    }
}
