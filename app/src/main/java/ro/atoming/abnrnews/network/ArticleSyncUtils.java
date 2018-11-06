package ro.atoming.abnrnews.network;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import ro.atoming.abnrnews.data.NewsContract;

public class ArticleSyncUtils {

    private static final int SYNC_INTERVAL_HOURS = 12;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;
    private static final String NEWS_SYNC_TAG = "articles-sync";
    private static boolean sInitialized;

    static void scheduleJobSync(@NonNull final Context context) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job syncArticlesJob = dispatcher.newJobBuilder()
                .setService(ArticleJobService.class)
                .setTag(NEWS_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(syncArticlesJob);
    }

    synchronized public static void initialize(@NonNull final Context context) {

        if (sInitialized) return;

        sInitialized = true;

        scheduleJobSync(context);

        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {

                String[] projectionColumns = {NewsContract.NewsEntry._ID};

                Cursor cursor = context.getContentResolver().query(
                        NewsContract.NewsEntry.CONTENT_URI,
                        projectionColumns,
                        null,
                        null,
                        null);

                if (cursor == null || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }

                cursor.close();
            }
        });

        checkForEmpty.start();
    }

    public static void startImmediateSync(final Context context){
        Intent intentToSyncImmediately = new Intent(context, ArticleIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
