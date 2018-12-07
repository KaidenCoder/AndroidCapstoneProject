package capstone.kurosaki.newsorg.Bookmark;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.sql.Timestamp;

import capstone.kurosaki.newsorg.News;

@Entity(foreignKeys = @ForeignKey(
        entity = News.class,
        parentColumns = "id",
        childColumns = "news_id"),
        indices = {@Index(value = "news_id", unique = true)},
        tableName = "saved"
)
public class BookmarkNews {
    @ColumnInfo(name = "news_id")
    private final int newsId;
    @PrimaryKey
    @ColumnInfo(name = "time_saved")
    private Timestamp timestamp;

    public BookmarkNews(int newsId){
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.newsId = newsId;
    }

    public int getNewsId() {
        return newsId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
