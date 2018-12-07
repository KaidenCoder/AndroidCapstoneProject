package capstone.kurosaki.newsorg.NewsPackage;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import capstone.kurosaki.newsorg.News;

@Dao
public interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void bulkInsert(List<News> news);

    @Query("SELECT * FROM articles")
    LiveData<List<News>> getAllArticles();

    @Query("SELECT * FROM articles WHERE q=:q ORDER BY published_at DESC")
    LiveData<List<News>> getNewsByCategory(String q);
}
