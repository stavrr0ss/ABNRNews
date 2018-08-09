package ro.atoming.abnrnews.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ro.atoming.abnrnews.R;
import ro.atoming.abnrnews.model.Article;
import ro.atoming.abnrnews.model.ArticleSource;

import static ro.atoming.abnrnews.ui.adapters.NewsAdapter.dateToString;
import static ro.atoming.abnrnews.ui.adapters.NewsAdapter.getShortDate;
import static ro.atoming.abnrnews.ui.adapters.NewsAdapter.getShortString;

public class MyNewsAdapter extends RecyclerView.Adapter<MyNewsAdapter.MyNewsViewHolder> {

    private OnArticleClickListener mArticleClickListener;
    private Context mContext;
    private List<Article> mArticleList;

    public MyNewsAdapter(Context context, List<Article> articleList, OnArticleClickListener articleClickListener) {
        mArticleClickListener = articleClickListener;
        mArticleList = articleList;
        mContext = context;
    }

    @NonNull
    @Override
    public MyNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        view.setFocusable(true);
        return new MyNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyNewsViewHolder holder, int position) {
        Article article = mArticleList.get(position);

        String articleTitle = article.getTitle();
        ArticleSource source = article.getSource();
        String sourceName = source.getSourceName();
        String description = article.getDescription();
        String date = article.getDate();
        String imageUrl = article.getImage();
        String articleUrl = article.getUrl();

        holder.mSourceName.setText(sourceName);
        holder.mArticleName.setText(getShortString(articleTitle));
        if (holder.mArticleDescription != null) {
            if (description != null) {
                holder.mArticleDescription.setText(description);
            } else {
                holder.mArticleDescription.setText("No description available !");
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
        if (mArticleList == null) {
            return 0;
        } else {
            return mArticleList.size();
        }
    }

    public void setData(List<Article> newsData) {
        mArticleList = newsData;
        notifyDataSetChanged();
    }

    public interface OnArticleClickListener {
        void onClick(String articleUrl);
    }


    public class MyNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

        public MyNewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Article article = mArticleList.get(adapterPosition);
            String articleUrl = article.getUrl();
            mArticleClickListener.onClick(articleUrl);
        }
    }
}
