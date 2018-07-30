package ro.atoming.abnrnews.network;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ro.atoming.abnrnews.model.Article;

public class ArticleSyncTask {
    private static final String LOG_TAG = ArticleSyncTask.class.getSimpleName();

    synchronized public static void syncHeadlineNews(){
        List<Article> articleList = new ArrayList<>();
        try{
            articleList = QueryUtils.fetchNews(QueryUtils.buildNewsUri(QueryUtils.CATEGORY_GENERAL));
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.v(LOG_TAG,"THIS IS THE RETURNED LIST BY INTENT SERVICE !!!!!" + articleList.toString());
    }
}
