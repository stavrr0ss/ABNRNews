package ro.atoming.abnrnews.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import ro.atoming.abnrnews.R;
import ro.atoming.abnrnews.ui.DetailActivity;

public class ArticleDetailFragment extends Fragment {

    @BindView(R.id.webview)
    WebView mWebview;
    private String articleUrl;

    public ArticleDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.article_detail_fragment, container, false);
        ButterKnife.bind(this, rootView);
        Intent openWebIntent = getActivity().getIntent();
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

        return rootView;
    }
}
