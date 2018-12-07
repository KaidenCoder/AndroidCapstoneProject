package capstone.kurosaki.newsorg.NewsPackage;

import android.arch.persistence.room.ColumnInfo;
import android.os.Parcel;
import android.os.Parcelable;

public class NewsSource implements Parcelable {
    @ColumnInfo(name = "id")
    private final String id;
    @ColumnInfo(name = "name")
    private final String name;

    public NewsSource(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "NewsSource{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.name);
    }

    protected NewsSource(Parcel in){
        this.id = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<NewsSource> CREATOR = new Parcelable.Creator<NewsSource>(){
        @Override
        public  NewsSource createFromParcel(Parcel parcel){
            return new NewsSource(parcel);
        }

        @Override
        public NewsSource[] newArray(int size){
            return new NewsSource[size];
        }
    };
}