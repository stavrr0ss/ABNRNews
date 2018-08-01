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
        ArticleSyncTask.syncHeadlineNews(this);
    }
}
