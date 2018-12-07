package capstone.kurosaki.newsorg.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.List;

import capstone.kurosaki.newsorg.News;
import capstone.kurosaki.newsorg.NewsPackage.NewsRepository;

public class BookmarkNewsService extends IntentService {

    public static final String ACTION_GET_NEXT = "newsorg.kurosaki.newsorg.widget.action.saved.next";
    public static final String ACTION_GET_PREVIOUS = "newsorg.kurosaki.newsorg.widget.action.saved.previous";
    private static final String ACTION_UPDATE_WIDGETS = "newsorg.kurosaki.newsorg.widget.action.update_widgets";

    public static final String PARAM_CURRENT = "newsorg.kurosaki.newsorg.widget.extra.current_selected";

    public BookmarkNewsService() {
        super("BookmarkNewsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_NEXT.equals(action)) {
                final int param1 = intent.getIntExtra(PARAM_CURRENT, 0);
                handleActionNext(param1);
            } else if (ACTION_GET_PREVIOUS.equals(action)) {
                final int param1 = intent.getIntExtra(PARAM_CURRENT, 0);
                handleActionPrevious(param1);
            } else if (ACTION_UPDATE_WIDGETS.equals(action)) {
                final LiveData<List<News>> liveData = NewsRepository.getInstance(getApplicationContext()).getSaved();
                liveData.observeForever(new Observer<List<News>>() {
                    @Override
                    public void onChanged(@Nullable List<News> news) {
                        if (news != null && news.size() > 0) {
                            handleUpdateWidgets(news, 0);
                            liveData.removeObserver(this);
                        }
                    }
                });
            }
        }
    }

    private void handleActionNext(final int currentIndex) {
        final LiveData<List<News>> liveData = NewsRepository.getInstance(getApplicationContext()).getSaved();
        liveData.observeForever(new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> news) {
                if (news != null && news.size() > currentIndex + 1) {
                    handleUpdateWidgets(news, currentIndex + 1);
                    liveData.removeObserver(this);
                }
            }
        });
    }

    private void handleActionPrevious(final int currentIndex) {
        final LiveData<List<News>> liveData = NewsRepository.getInstance(getApplicationContext()).getSaved();
        liveData.observeForever(new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> news) {
                if (news != null && news.size() > 0 && currentIndex > 0) {
                    handleUpdateWidgets(news, currentIndex - 1);
                    liveData.removeObserver(this);
                }
            }
        });
    }

    private void handleUpdateWidgets(List<News> articles, int selected) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), BookmarkNewsWidget.class));
        BookmarkNewsWidget.updateNewsWidgets(getApplicationContext(), appWidgetManager, articles, selected, appWidgetIds);
    }
}
