package capstone.kurosaki.newsorg;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import capstone.kurosaki.newsorg.NewsPackage.NewsRepository;
import capstone.kurosaki.newsorg.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {
    public static final String PARAM_NEWS = "param_news";
    private ActivityDetailBinding binding;
    private News news;
    private boolean isSaved;
    private NewsRepository newsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail);

        makeUiFullScreen();
        setUpToolbar();
        setupNewsAndListener();
        newsRepository = NewsRepository.getInstance(this);

        getSavedState();

        MobileAds.initialize(this, getString(R.string.admob_app_id));
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        binding.adView.loadAd(adRequest);
        binding.ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSaved) {
                    newsRepository.removeSaved(news.id);
                } else {
                    newsRepository.save(news.id);
                }
            }
        });
    }

    private void getSavedState() {
        if (news != null) {
            newsRepository.isSaved(news.id).observe(this, new android.arch.lifecycle.Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable Boolean aBoolean) {
                    if (aBoolean != null) {
                        isSaved = aBoolean;
                        if (isSaved) {
                            binding.ivSave.setImageResource(R.drawable.ic_saved_item);
                        } else {
                            binding.ivSave.setImageResource(R.drawable.ic_save);
                        }
                    }
                }
            });
        }
    }

    private void setUpToolbar(){
        setSupportActionBar(binding.toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void makeUiFullScreen(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            binding.getRoot().setFitsSystemWindows(true);
        }

        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    private void setupNewsAndListener(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey(PARAM_NEWS)){
            final News news = bundle.getParcelable(PARAM_NEWS);
            if(news != null){
                this.news = news;
                binding.setArticle(news);
                setupShareButton(news);
                setupButtonClickListener(news);
            }
        }
    }

    private void setupShareButton(final News news){
        binding.ivShare.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Intent.ACTION_SEND);
                String shareText = news.getTitle() + "\n" + news.getUrl();
                intent.putExtra(Intent.EXTRA_TEXT, shareText);
                intent.setType("text/plain");

                startActivity(intent);
            }
        });
    }

    private void setupButtonClickListener(final News news) {
        binding.btnReadFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getUrl()));
                startActivity(intent);
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.fade_enter_transition, R.anim.slide_down_animation);
    }
}

