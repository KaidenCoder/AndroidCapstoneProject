package capstone.kurosaki.newsorg;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import capstone.kurosaki.newsorg.NewsPackage.NewsRepository;
import capstone.kurosaki.newsorg.databinding.ActivityMainBinding;

import capstone.kurosaki.newsorg.utils.OptionsBottomSheet;
import capstone.kurosaki.newsorg.widget.BookmarkNewsWidget;

public class MainActivity extends AppCompatActivity implements OptionsBottomSheet.OptionsBottomSheetListener {
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private ActivityMainBinding binding;
    private TabularFragment tabularFragment;
    private NewsFragment newsFragment;
    private FirebaseAnalytics mFirebaseAnalytics;
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Bundle bundle = new Bundle();
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, tabularFragment)
                            .commit();
                    bundle.putString(
                            FirebaseAnalytics.Param.ITEM_CATEGORY,
                            getString(R.string.title_news)
                    );
                    return true;
                case R.id.navigation_bookmark:
                    if (newsFragment == null) {
                        newsFragment = NewsFragment.newInstance(null);
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, newsFragment)
                            .commit();
                    bundle.putString(
                            FirebaseAnalytics.Param.ITEM_CATEGORY,
                            getString(R.string.title_bookmark)
                    );
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    return true;

            }

            return false;
        }
    };
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            tabularFragment = TabularFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, tabularFragment)
                    .commit();
        }

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setupToolbar();

        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        final LiveData<List<News>> saved = NewsRepository.getInstance(this).getSaved();
        saved.observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> news) {
                if (news != null) {
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), BookmarkNewsWidget.class));
                    if (news.size() == 0) {
                        BookmarkNewsWidget.updateNewsWidgets(getApplicationContext(), appWidgetManager, news, -1, appWidgetIds);
                    } else {
                        BookmarkNewsWidget.updateNewsWidgets(getApplicationContext(), appWidgetManager, news, 0, appWidgetIds);
                    }
                }
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name));
            //Remove trailing space from toolbar
            binding.toolbar.setContentInsetsAbsolute(10, 10);
        }
    }

    @Override
    public void onSaveToggle(String text) {
        if (snackbar == null) {
            snackbar = Snackbar.make(binding.coordinator, "Hello", Snackbar.LENGTH_SHORT);
            final CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackbar.getView().getLayoutParams();
            params.setMargins(
                    (int) getResources().getDimension(R.dimen.snackbar_margin_vertical),
                    0,
                    (int) getResources().getDimension(R.dimen.snackbar_margin_vertical),
                    (int) getResources().getDimension(R.dimen.snackbar_margin_horizontal)
            );
            snackbar.getView().setLayoutParams(params);
            snackbar.getView().setPadding(
                    (int) getResources().getDimension(R.dimen.snackbar_padding),
                    (int) getResources().getDimension(R.dimen.snackbar_padding),
                    (int) getResources().getDimension(R.dimen.snackbar_padding),
                    (int) getResources().getDimension(R.dimen.snackbar_padding)
            );
        }
        if (snackbar.isShown()) {
            snackbar.dismiss();
        }
        snackbar.setText(text);
        snackbar.show();
    }
}

