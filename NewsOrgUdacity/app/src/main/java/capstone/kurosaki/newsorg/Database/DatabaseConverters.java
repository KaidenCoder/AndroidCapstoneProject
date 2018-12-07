package capstone.kurosaki.newsorg.Database;

import android.arch.persistence.room.TypeConverter;

import java.sql.Timestamp;

public class DatabaseConverters {
    @TypeConverter
    public static Timestamp toJavaTimeStamp(Long l){
        return l == null ? null : new Timestamp(l);
    }

    @TypeConverter
    public static long toDatabaseTimeStamp(Timestamp l){
        return l == null ? new Timestamp(System.currentTimeMillis()).getTime() : l.getTime();
    }

}