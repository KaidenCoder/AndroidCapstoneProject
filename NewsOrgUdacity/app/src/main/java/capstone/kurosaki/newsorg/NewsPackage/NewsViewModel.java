package capstone.kurosaki.newsorg.NewsPackage;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import capstone.kurosaki.newsorg.News;

public class NewsViewModel extends AndroidViewModel {
    private final NewsRepository newsRepository;

    public NewsViewModel(@NonNull Application application){
        super(application);
        newsRepository = NewsRepository.getInstance(application.getApplicationContext());
    }

    public LiveData<List<News>> getNewsHeadlines(NewsHelper specification) {
        return newsRepository.getHeadlines(specification);
    }

    public LiveData<List<News>> getAllSaved() {
        return newsRepository.getSaved();
    }

}
