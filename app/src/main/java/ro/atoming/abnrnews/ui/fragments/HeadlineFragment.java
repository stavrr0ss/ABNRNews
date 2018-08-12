package ro.atoming.abnrnews.ui.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import ro.atoming.abnrnews.R;
import ro.atoming.abnrnews.network.QueryUtils;
import ro.atoming.abnrnews.ui.DetailActivity;
import ro.atoming.abnrnews.ui.adapters.NewsAdapter;

import static ro.atoming.abnrnews.data.NewsProvider.LOG_TAG;

public class HeadlineFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>,
        NewsAdapter.ArticleAdapterOnClickHandler {

    private static final int LOADER_ID = 33;
    @BindView(R.id.article_recyclerview)
    RecyclerView mRecyclerview;
    private NewsAdapter mAdapter;

    public HeadlineFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.news_fragment, container , false);
        ButterKnife.bind(this, rootView);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mAdapter = new NewsAdapter(getActivity(), this);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(layoutManager);
        mRecyclerview.setHasFixedSize(true);

        if (mAdapter == null) {
            Log.d(LOG_TAG, "THE RECYCLERVIEW ADAPTER IS NULL!!!!");
        } else if (getLoaderManager() == null) {
            Log.d(LOG_TAG, "THE LOADER MANAGER IS NULL !!!!");
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return FragmentUtils.fragmentLoader(getActivity(), QueryUtils.CATEGORY_GENERAL);
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String articleUrl, Bundle bundle) {
        Intent openArticleIntent = new Intent(getActivity(), DetailActivity.class);
        openArticleIntent.putExtra(DetailActivity.ARTICLE_URL_PATH, articleUrl);
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
        //    bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
        //}
        startActivity(openArticleIntent, bundle);
    }
}
