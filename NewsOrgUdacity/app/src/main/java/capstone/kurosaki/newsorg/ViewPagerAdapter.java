package capstone.kurosaki.newsorg;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import capstone.kurosaki.newsorg.Retrofit.NewsApi;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final NewsFragment[] newsFragments;

    public ViewPagerAdapter(FragmentManager fragmentManager, String[] strings){
        super(fragmentManager);
        newsFragments = new NewsFragment[strings.length];
        for(int i = 0; i < strings.length; i++){
            newsFragments[i] = NewsFragment.newInstance(NewsApi.QuerySearch.valueOf(strings[i]));
        }
    }

    @Override
    public Fragment getItem(int i){
        return newsFragments[i];
    }

    @Override
    public int getCount(){
        return newsFragments == null ? 0 : newsFragments.length;
    }
}
