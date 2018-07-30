package ro.atoming.abnrnews.network;

import android.net.Uri;
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

import ro.atoming.abnrnews.BuildConfig;
import ro.atoming.abnrnews.model.Article;
import ro.atoming.abnrnews.model.ArticleSource;

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
    public static final String SOURCE_BASE_URL = "https://newsapi.org/v2/sources";
    public static final String API_KEY = BuildConfig.API_KEY;
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
    public static final String COUNTRY_TEST_US = "us";


    //helper method to build the desired url by category and country(later)
    public static String buildNewsUri(String category) {
        Uri buildUri = Uri.parse(HEADLINE_BASE_URL).buildUpon()
                .appendQueryParameter(CATEGORY,category)
                .appendQueryParameter(COUNTRY,COUNTRY_TEST_US)
                .appendQueryParameter(api_key, API_KEY)
                .build();
        return buildUri.toString();
    }

    public static List<Article> fetchNews(String requestUrl) {
        URL url = buildUrl(requestUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG,"Problem with HTTP response !",e);
        }
        List<Article> articleList = extractJsonResponse(jsonResponse);
        return articleList;
    }

    private static URL buildUrl(String stringUrl) {
        URL returnUrl = null;
        try {
            returnUrl = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return returnUrl;
    }

    private static String makeHttpRequest(URL url) throws IOException {
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

    private static List<Article> extractJsonResponse(String jsonResponse) {
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
        try{
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray articlesArray = jsonObject.getJSONArray(ARTICLES_ARRAY);
            for (int i = 0; i<articlesArray.length();i++){
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
                returnedArticle = new Article(articleSource,author,title,description,url,image,date);
                articleList.add(returnedArticle);
            }
        }catch (JSONException e){
            Log.e(LOG_TAG,"Error with Json Response !!!");
        }
        return articleList;
    }
}
