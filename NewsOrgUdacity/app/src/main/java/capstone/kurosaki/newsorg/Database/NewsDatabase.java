package capstone.kurosaki.newsorg.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import capstone.kurosaki.newsorg.Bookmark.BookmarkDao;
import capstone.kurosaki.newsorg.Bookmark.BookmarkNews;
import capstone.kurosaki.newsorg.News;
import capstone.kurosaki.newsorg.NewsPackage.NewsDao;

@Database(entities = {News.class, BookmarkNews.class},
        version = 1,
        exportSchema = false)
@TypeConverters(DatabaseConverters.class)
public abstract class NewsDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "news";
    private static NewsDatabase sInstance;

    public static NewsDatabase getInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        NewsDatabase.class,
                        DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract NewsDao newsDao();

    public abstract BookmarkDao bookmarkDao();
}