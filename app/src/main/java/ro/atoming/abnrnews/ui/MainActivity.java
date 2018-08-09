package ro.atoming.abnrnews.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.stetho.Stetho;

import ro.atoming.abnrnews.R;
import ro.atoming.abnrnews.network.ArticleSyncUtils;
import ro.atoming.abnrnews.ui.adapters.CategoryAdapter;
import ro.atoming.abnrnews.widget.ListViewRemoteFactory;

public class MainActivity extends AppCompatActivity {
    public static final String TEST_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=c8600b24321c416db9225ea91647f69c";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        Toolbar myToolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ViewPager viewPager = findViewById(R.id.viewPager);
        CategoryAdapter fragmentAdapter = new CategoryAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(fragmentAdapter);
        TabLayout tabs = findViewById(R.id.sliding_tabs);
        tabs.setupWithViewPager(viewPager);

        ArticleSyncUtils.initialize(this);
        ListViewRemoteFactory.updateAllWidgets(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings_menu_id) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
