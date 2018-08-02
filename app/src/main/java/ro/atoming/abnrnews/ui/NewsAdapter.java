package ro.atoming.abnrnews.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ro.atoming.abnrnews.R;
import ro.atoming.abnrnews.data.NewsContract;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {

    public static final int FIRST_ARTICLE_VIEW = 0;
    public static final int GENERAL_ARTICLE_VIEW = 1;

    private final Context mContext;

    final private ArticleAdapterOnClickHandler mClickHandler;
    private boolean mUseFirstArticleLayout;
    private Cursor mCursor;

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

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        int articleIndex = mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_ARTICLE_TITLE);
        int sourceIndex = mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_SOURCE_NAME);
        int descriptionIndex = mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_ARTICLE_DESCRIPTION);
        int dateIndex = mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_ARTICLE_DATE);
        int imageIndex = mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_ARTICLE_IMAGE);
        int urlIndex = mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_ARTICLE_URL);

        String articleTitle = mCursor.getString(articleIndex);
        String sourceName = mCursor.getString(sourceIndex);
        String description = mCursor.getString(descriptionIndex);
        String date = mCursor.getString(dateIndex);
        String imageUrl = mCursor.getString(imageIndex);
        String articleUrl = mCursor.getString(urlIndex);

        holder.mSourceName.setText(sourceName);
        holder.mArticleName.setText(articleTitle);
        // holder.mArticleDescription.setText(description);
        holder.mArticleDate.setText(date);
        Picasso.get().load(imageUrl).into(holder.mArticleImage);

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
        void onClick(int clickedItem);
    }

    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.image)
        ImageView mArticleImage;
        @BindView(R.id.article_name)
        TextView mArticleName;
        @BindView(R.id.article_date)
        TextView mArticleDate;
        //@BindView(R.id.description)
        //TextView mArticleDescription;
        @BindView(R.id.source_name)
        TextView mSourceName;


        NewsAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
