package capstone.kurosaki.newsorg.Retrofit;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import capstone.kurosaki.newsorg.News;
import capstone.kurosaki.newsorg.NewsPackage.NewsHelper;
import capstone.kurosaki.newsorg.NewsPackage.NewsWrapper;
import capstone.kurosaki.newsorg.utils.DateDeserializer;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsRetrofit {
    private static final String NEWS_API_URL = "https://newsapi.org/";
    private static final Object LOCK = new Object();
    private static NewsApi sNewsApi;
    private static NewsRetrofit sInstance;

    private NewsRetrofit(){

    }

    public static NewsRetrofit getInstance(Context context){
        if(sInstance == null || sNewsApi == null){
            synchronized (LOCK){
                Cache cache = new Cache(context.getApplicationContext().getCacheDir(),5 * 1024 * 1024);
                Interceptor networkInterceptor = new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        CacheControl cacheControl = new CacheControl.Builder()
                                .maxAge(1, TimeUnit.HOURS)
                                .maxStale(3,TimeUnit.DAYS)
                                .build();
                        return chain.proceed(chain.request())
                                .newBuilder()
                                .removeHeader("Pragma")
                                .header("Cache-Control", cacheControl.toString())
                                .build();
                    }
                };

                HttpLoggingInterceptor httpLoggingInterceptor =
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder()
                        .cache(cache)
                        .addNetworkInterceptor(networkInterceptor)
                        .addInterceptor(httpLoggingInterceptor)
                        .build();

                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DateDeserializer())
                        .create();

                Retrofit.Builder builder =
                        new Retrofit.Builder()
                                .baseUrl(NEWS_API_URL)
                                .client(client)
                                .addConverterFactory(GsonConverterFactory.create(gson));

                sNewsApi = builder.build().create(NewsApi.class);
                sInstance = new NewsRetrofit();
            }
        }
        return sInstance;
    }

    public LiveData<List<News>> getHeadlines(final NewsHelper specification){
        final MutableLiveData<List<News>> networkArticleLiveData = new MutableLiveData<>();

        Call<NewsWrapper> networkCall = sNewsApi.getHeadlines(
                specification.getQ()

        );

        networkCall.enqueue(new Callback<NewsWrapper>() {
            @Override
            public void onResponse(Call<NewsWrapper> call, retrofit2.Response<NewsWrapper> response) {
                if(response.body() != null){
                    List<News> articles = response.body().getArticles();
                    for(News article : articles){
                        article.setQ(specification.getQ());
                    }
                    networkArticleLiveData.setValue(articles);
                }
            }

            @Override
            public void onFailure(Call<NewsWrapper> call, Throwable t) {

            }
        });
        return networkArticleLiveData;
    }
}
