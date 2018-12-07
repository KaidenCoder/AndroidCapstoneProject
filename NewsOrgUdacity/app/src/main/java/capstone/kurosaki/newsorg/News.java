package capstone.kurosaki.newsorg;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.sql.Timestamp;

import capstone.kurosaki.newsorg.NewsPackage.NewsSource;

@Entity(tableName = "articles", indices = {@Index(value = "title", unique = true)})
public class News implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @Expose(serialize = false, deserialize = false)
    public int id;
    @ColumnInfo(name = "author")
    private final String author;
    @ColumnInfo(name = "title")
    private final String title;
    @ColumnInfo(name = "description")
    private final String description;
    @ColumnInfo(name = "url")
    private final String url;
    @ColumnInfo(name = "published_at")
    private final Timestamp publishedAt;
    @ColumnInfo(name = "image_url")
    private final String urlToImage;
    @Embedded(prefix = "source_")
    private final NewsSource source;
    @ColumnInfo(name = "content")
    private final String content;
    @ColumnInfo(name = "q")
    @Expose(serialize = false, deserialize = false)
    private String q;
    @ColumnInfo(name = "save_date")
    @Expose(serialize = false, deserialize = false)
    private Timestamp saveDate = new Timestamp(System.currentTimeMillis());


    public News(String author, String title, String description, String url, Timestamp publishedAt, String urlToImage, NewsSource source, String content) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.publishedAt = publishedAt;
        this.urlToImage = urlToImage;
        this.source = source;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public Timestamp getPublishedAt() {
        return publishedAt;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public NewsSource getSource() {
        return source;
    }

    public String getContent() {
        return content;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public Timestamp getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(Timestamp saveDate) {
        this.saveDate = saveDate;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", publishedAt=" + publishedAt +
                ", urlToImage='" + urlToImage + '\'' +
                ", source=" + source +
                ", content='" + content + '\'' +
                ", q='" + q + '\'' +
                ", saveDate=" + saveDate +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.author);
        parcel.writeString(this.title);
        parcel.writeString(this.description);
        parcel.writeString(this.url);
        parcel.writeSerializable(this.publishedAt);
        parcel.writeString(this.urlToImage);
        parcel.writeParcelable(this.source, i);
        parcel.writeString(this.content);
        parcel.writeString(this.q);
        parcel.writeSerializable(this.saveDate);
    }

    protected News(Parcel in){
        this.id = in.readInt();
        this.author = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.url = in.readString();
        this.publishedAt = (Timestamp) in.readSerializable();
        this.urlToImage = in.readString();
        this.source = in.readParcelable(NewsSource.class.getClassLoader());
        this.content = in.readString();
        this.q = in.readString();
        this.saveDate = (Timestamp) in.readSerializable();
    }

    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel parcel) {
            return new News(parcel);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
}

