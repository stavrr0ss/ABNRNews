package ro.atoming.abnrnews.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ro.atoming.abnrnews.R;
import ro.atoming.abnrnews.data.NewsContract;

import static ro.atoming.abnrnews.data.NewsContract.NewsEntry.COLUMN_ARTICLE_URL;
import static ro.atoming.abnrnews.data.NewsProvider.LOG_TAG;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {

    public static final int FIRST_ARTICLE_VIEW = 0;
    public static final int GENERAL_ARTICLE_VIEW = 1;
    private static final int TITLE_LENGTH = 80;
    private static final int DATE_LENGHT = 10;


    private final Context mContext;

    final private ArticleAdapterOnClickHandler mClickHandler;
    private boolean mUseFirstArticleLayout;
    private Cursor mCursor;
    private Bundle mBundle;

    public NewsAdapter(@NonNull Context context, ArticleAdapterOnClickHandler articleAdapterOnClickHandler) {
        mContext = context;
        mClickHandler = articleAdapterOnClickHandler;
        mUseFirstArticleLayout = mContext.getResources().getBoolean(R.bool.use_first_article_view);
    }

    @NonNull
    @Override
    public NewsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layoutId;
        switch (viewType) {
            case FIRST_ARTICLE_VIEW: {
                layoutId = R.layout.first_article_item;
                break;
            }
            case GENERAL_ARTICLE_VIEW: {
                layoutId = R.layout.list_item;
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        view.setFocusable(true);
        return new NewsAdapterViewHolder(view);
    }

    public static String getShortString(String title) {
        String shortTitle = "";
        if (title.length() > TITLE_LENGTH) {
            shortTitle = title.substring(0, TITLE_LENGTH) + "...";
        } else {
            shortTitle = title;
        }
        return shortTitle;
    }

    public static String dateToString(String date) throws Exception {

        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        Date d = sdf.parse(date);
        Log.d(LOG_TAG, "THIS IS THE DATE RETURNED " + d.toString());
        return d.toString();

    }

    public static String getShortDate(String date) {
        String shortDate = "";
        if (date.length() > DATE_LENGHT) {
            shortDate = date.substring(0, DATE_LENGHT);
        }
        return shortDate;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        int articleIndex = mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_ARTICLE_TITLE);
        int sourceIndex = mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_SOURCE_NAME);
        int descriptionIndex = mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_ARTICLE_DESCRIPTION);
        int dateIndex = mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_ARTICLE_DATE);
        int imageIndex = mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_ARTICLE_IMAGE);
        int urlIndex = mCursor.getColumnIndex(COLUMN_ARTICLE_URL);

        String articleTitle = mCursor.getString(articleIndex);
        String sourceName = mCursor.getString(sourceIndex);
        String description = mCursor.getString(descriptionIndex);
        String date = mCursor.getString(dateIndex);
        String imageUrl = mCursor.getString(imageIndex);
        String articleUrl = mCursor.getString(urlIndex);

        holder.mSourceName.setText(sourceName);
        holder.mArticleName.setText(getShortString(articleTitle));
        if (holder.mArticleDescription != null) {
            if (description != null) {
                holder.mArticleDescription.setText(description);
            } else {
                holder.mArticleDescription.setText(mContext.getString(R.string.no_article_description_text));
            }
        }
        try {
            String modifiedDate = dateToString(date);
            holder.mArticleDate.setText(getShortDate(modifiedDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.newspaper_resized)
                    .into(holder.mArticleImage);
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        } else {
            return mCursor.getCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mUseFirstArticleLayout && position == 0) {
            return FIRST_ARTICLE_VIEW;
        } else {
            return GENERAL_ARTICLE_VIEW;
        }
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface ArticleAdapterOnClickHandler {
        void onClick(String articleUrl, Bundle bundle);
    }

    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.image)
        ImageView mArticleImage;
        @BindView(R.id.article_name)
        TextView mArticleName;
        @BindView(R.id.article_date)
        TextView mArticleDate;

        @Nullable
        @BindView(R.id.description)
        TextView mArticleDescription;
        @BindView(R.id.source_name)
        TextView mSourceName;


        NewsAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String articleUrl = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_ARTICLE_URL));
            mClickHandler.onClick(articleUrl, mBundle);
        }
    }
}
