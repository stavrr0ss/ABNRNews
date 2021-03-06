package ro.atoming.abnrnews.network;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import ro.atoming.abnrnews.utils.NotificationUtils;

public class ArticleJobService extends JobService {

    private static AsyncTask<Void, Void, Void> mGetArticlesTask;

    @Override
    public boolean onStartJob(final JobParameters jobparameters) {
        mGetArticlesTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                ArticleSyncTask.syncNews(context);
                if (NotificationUtils.areNotificationsEnabled(context)) {
                    NotificationUtils.notifyUserOfUpdate(context);
                }
                jobFinished(jobparameters, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(jobparameters, false);
            }
        };
        mGetArticlesTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mGetArticlesTask != null) {
            mGetArticlesTask.cancel(true);
        }
        return true;
    }
}
