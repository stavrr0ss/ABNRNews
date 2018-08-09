package ro.atoming.abnrnews.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ro.atoming.abnrnews.R;
import ro.atoming.abnrnews.ui.fragments.ArticleDetailFragment;

public class DetailActivity extends AppCompatActivity {

    public static final String ARTICLE_URL_PATH = "urlPath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        ArticleDetailFragment detailFragment = new ArticleDetailFragment();
        fm.beginTransaction().add(R.id.fragment_container, detailFragment).commit();

    }
}
