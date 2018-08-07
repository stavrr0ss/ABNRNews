package ro.atoming.abnrnews.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import ro.atoming.abnrnews.R;
import ro.atoming.abnrnews.data.NewsContract;

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

    @Override
    public void onDataSetChanged() {
        Uri NEWS_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ARTICLE).build();
        if (mCursor != null) mCursor.close();
        mCursor = mContext.getContentResolver().query(
                NEWS_URI,
                null,
                null,
                null,
                NewsContract.NewsEntry.COLUMN_ARTICLE_DATE
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

        String articleTitle = mCursor.getString(articleTitleIndex);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.list_item_widget);

        views.setTextViewText(R.id.widget_article_name, articleTitle);


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
