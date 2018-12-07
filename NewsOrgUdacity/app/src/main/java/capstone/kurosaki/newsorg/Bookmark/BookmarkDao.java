package capstone.kurosaki.newsorg.Bookmark;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import capstone.kurosaki.newsorg.News;

@Dao
public interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BookmarkNews newsArticle);

    @Query("SELECT COUNT(news_id) > 0 FROM saved WHERE news_id = :newsId")
    LiveData<Boolean> isFavorite(int newsId);

    @Query("DELETE FROM saved WHERE news_id=:newsId")
    void removeSaved(int newsId);

    @Query("SELECT articles.* FROM articles, saved " +
            "WHERE articles.id == saved.news_id " +
            "ORDER BY saved.time_saved")
    LiveData<List<News>> getAllSaved();
}