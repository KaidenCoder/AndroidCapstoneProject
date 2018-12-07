package capstone.kurosaki.newsorg.NewsPackage;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;

import java.util.List;

import capstone.kurosaki.newsorg.Bookmark.BookmarkDao;
import capstone.kurosaki.newsorg.Bookmark.BookmarkNews;
import capstone.kurosaki.newsorg.Database.NewsDatabase;
import capstone.kurosaki.newsorg.News;
import capstone.kurosaki.newsorg.Retrofit.NewsRetrofit;
import capstone.kurosaki.newsorg.utils.AppExecutors;

public class NewsRepository {
    private static final Object LOCK = new Object();
    private static NewsRepository sInstance;

    private final NewsRetrofit newsApiService;
    private final NewsDao newsDao;
    private final BookmarkDao bookmarkDao;
    private final AppExecutors mExecutor;
    private final MutableLiveData<List<News>> networkNewsLiveData;

    private NewsRepository(Context context) {
        newsApiService = NewsRetrofit.getInstance(context);
        newsDao = NewsDatabase.getInstance(context).newsDao();
        bookmarkDao = NewsDatabase.getInstance(context).bookmarkDao();
        mExecutor = AppExecutors.getsInstance();
        networkNewsLiveData = new MutableLiveData<>();
        networkNewsLiveData.observeForever(new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable final List<News> news) {
                if (news != null) {
                    mExecutor.getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            newsDao.bulkInsert(news);
                        }
                    });
                }
            }
        });
    }

    public synchronized static NewsRepository getInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new NewsRepository(context);
            }
        }
        return sInstance;
    }

    public LiveData<List<News>> getHeadlines(final NewsHelper newsHelper) {
        final LiveData<List<News>> networkData = newsApiService.getHeadlines(newsHelper);
        networkData.observeForever(new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> news) {
                if (news != null) {
                    networkNewsLiveData.setValue(news);
                    networkData.removeObserver(this);
                }
            }
        });
        return newsDao.getNewsByCategory(newsHelper.getQ());
    }

    public LiveData<List<News>> getSaved() {
        return bookmarkDao.getAllSaved();
    }

    public LiveData<Boolean> isSaved(int articleId) {
        return bookmarkDao.isFavorite(articleId);
    }

    public void removeSaved(final int articleId) {
        mExecutor.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                bookmarkDao.removeSaved(articleId);
            }
        });
    }

    public void save(final int newsId) {
        mExecutor.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                BookmarkNews bookmarkNews = new BookmarkNews(newsId);
                bookmarkDao.insert(bookmarkNews);
            }
        });
    }
}

