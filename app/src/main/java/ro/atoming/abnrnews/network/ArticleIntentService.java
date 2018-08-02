package ro.atoming.abnrnews.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class ArticleIntentService extends IntentService {
    public ArticleIntentService() {
        super("ArticleIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ArticleSyncTask.syncNews(this, QueryUtils.CATEGORY_SPORTS);
        ArticleSyncTask.syncNews(this, QueryUtils.CATEGORY_BUSINESS);
        ArticleSyncTask.syncNews(this, QueryUtils.CATEGORY_SCIENCE);
        ArticleSyncTask.syncNews(this, QueryUtils.CATEGORY_ENTERTAINMENT);
        ArticleSyncTask.syncNews(this, QueryUtils.CATEGORY_HEALTH);
        ArticleSyncTask.syncNews(this, QueryUtils.CATEGORY_TECHNOLOGY);
        ArticleSyncTask.syncNews(this, QueryUtils.CATEGORY_GENERAL);
    }

}
