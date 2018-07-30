package ro.atoming.abnrnews.network;

import android.content.Context;
import android.content.Intent;

public class ArticleSyncUtils {

    public static void startImmediateSync(final Context context){
        Intent intentToSyncImmediately = new Intent(context, ArticleIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
