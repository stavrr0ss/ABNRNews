package ro.atoming.abnrnews.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ro.atoming.abnrnews.R;
import ro.atoming.abnrnews.model.Article;
import ro.atoming.abnrnews.network.QueryUtils;
import ro.atoming.abnrnews.ui.DetailActivity;
import ro.atoming.abnrnews.ui.adapters.MyNewsAdapter;

public class MyNewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Article>>,
        MyNewsAdapter.OnArticleClickListener {

    public MyNewsFragment(){}


    public static final int LOADER_ID = 556;
    @BindView(R.id.myNews_recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.searchView)
    SearchView mSearchView;

    private MyNewsAdapter mAdapter;
    private String mSearchQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_news_fragment, container, false);
        ButterKnife.bind(this, rootView);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchQuery = query;
                getLoaderManager().restartLoader(LOADER_ID, null, MyNewsFragment.this);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSearchQuery = newText;
                return false;
            }
        });

        mAdapter = new MyNewsAdapter(getActivity(), new ArrayList<Article>(), this);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(mAdapter);

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<List<Article>> onCreateLoader(int id, @Nullable Bundle args) {
        return new ArticleLoader(getContext(), mSearchQuery);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Article>> loader, List<Article> data) {
        if (data != null || !data.isEmpty()) {
            mAdapter.setData(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Article>> loader) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(String articleUrl) {
        Intent openArticleIntent = new Intent(getActivity(), DetailActivity.class);
        openArticleIntent.putExtra(DetailActivity.ARTICLE_URL_PATH, articleUrl);
        startActivity(openArticleIntent);
    }


    private static class ArticleLoader extends AsyncTaskLoader<List<Article>> {

        private String mUrl;


        public ArticleLoader(@NonNull Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }

        @Nullable
        @Override
        public List<Article> loadInBackground() {
            List<Article> articleList = QueryUtils.queryArticles(mUrl, getContext());
            return articleList;
        }

    }
}
