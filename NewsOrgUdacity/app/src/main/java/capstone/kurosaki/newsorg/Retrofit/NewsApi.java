package capstone.kurosaki.newsorg.Retrofit;

import capstone.kurosaki.newsorg.BuildConfig;
import capstone.kurosaki.newsorg.NewsPackage.NewsWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface NewsApi {
    String API_KEY = BuildConfig.NewsApiKey;

    @Headers("X-Api-Key:" + API_KEY)
    @GET("/v2/everything")
    Call<NewsWrapper> getHeadlines(
            @Query("q") String query

    );

    enum QuerySearch{
        chess("Chess"),
        sudoku("Sudoku"),
        android("Android"),
        ios("IOS"),
        java("Java"),
        kotlin("Kotlin"),
        football("Football"),
        technology("Technology"),
        travel("Travel");


        public final String title;

        QuerySearch(String title){
            this.title = title;
        }
    }
}