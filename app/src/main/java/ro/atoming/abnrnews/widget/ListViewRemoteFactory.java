package ro.atoming.abnrnews.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import ro.atoming.abnrnews.R;
import ro.atoming.abnrnews.data.NewsContract;
import ro.atoming.abnrnews.ui.adapters.NewsAdapter;

import static ro.atoming.abnrnews.data.NewsContract.BASE_CONTENT_URI;
import static ro.atoming.abnrnews.data.NewsContract.PATH_ARTICLE;

public class ListViewRemoteFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor mCursor;

    public ListViewRemoteFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    public static void updateAllWidgets(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, NewsWidget.class));
        if (appWidgetIds.length > 0) {
            new NewsWidget().onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public void onDataSetChanged() {
        Uri NEWS_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ARTICLE).build();
        if (mCursor != null) mCursor.close();
        String sortOrder = NewsContract.NewsEntry.COLUMN_ARTICLE_DATE + " DESC";
        mCursor = mContext.getContentResolver().query(
                NEWS_URI,
                null,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(position);

        int articleTitleIndex = mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_ARTICLE_TITLE);
        int articleDateIndex = mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_ARTICLE_DATE);

        String articleTitle = mCursor.getString(articleTitleIndex);
        String articleDate = mCursor.getString(articleDateIndex);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.list_item_widget);
//TODO: take this methods and also from NewsAdapter and move it to a Util class to use where you want
        views.setTextViewText(R.id.widget_article_name, NewsAdapter.getShortString(articleTitle));
        try {
            String modifiedDate = NewsAdapter.dateToString(articleDate);
            views.setTextViewText(R.id.widget_article_date, NewsAdapter.getShortDate(modifiedDate));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
