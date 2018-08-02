package ro.atoming.abnrnews.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.facebook.stetho.Stetho;

import ro.atoming.abnrnews.R;
import ro.atoming.abnrnews.network.ArticleSyncUtils;
import ro.atoming.abnrnews.ui.adapters.CategoryAdapter;

public class MainActivity extends AppCompatActivity {
    public static final String TEST_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=c8600b24321c416db9225ea91647f69c";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);

        ViewPager viewPager = findViewById(R.id.viewPager);
        CategoryAdapter fragmentAdapter = new CategoryAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(fragmentAdapter);
        TabLayout tabs = findViewById(R.id.sliding_tabs);
        tabs.setupWithViewPager(viewPager);

        ArticleSyncUtils.startImmediateSync(this);
    }

}
