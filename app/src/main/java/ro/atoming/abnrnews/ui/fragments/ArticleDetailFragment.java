package ro.atoming.abnrnews.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ro.atoming.abnrnews.R;
import ro.atoming.abnrnews.ui.DetailActivity;

public class ArticleDetailFragment extends Fragment {

    @BindView(R.id.webview)
    WebView mWebview;
    @BindView(R.id.no_internet_textView)
    TextView mNoInternetText;
    @BindView(R.id.share_Fab)
    FloatingActionButton mFab;

    private String articleUrl;
    public ArticleDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.article_detail_fragment, container, false);
        ButterKnife.bind(this, rootView);
        Intent openWebIntent = getActivity().getIntent();
        if (isConnected()) {
            mNoInternetText.setVisibility(View.GONE);
            if (openWebIntent.hasExtra(DetailActivity.ARTICLE_URL_PATH)) {
                articleUrl = openWebIntent.getStringExtra(DetailActivity.ARTICLE_URL_PATH);
            }
            mWebview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    return false;
                }
            });
            mWebview.loadUrl(articleUrl);
        } else {
            mNoInternetText.setText(getString(R.string.no_internet_connection));
            mNoInternetText.setVisibility(View.VISIBLE);
        }
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, articleUrl);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_link_chooser)));
            }
        });
        return rootView;
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
