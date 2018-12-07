package capstone.kurosaki.newsorg;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import capstone.kurosaki.newsorg.Retrofit.NewsApi;
import capstone.kurosaki.newsorg.databinding.FragmentHeadlinesBinding;

public class TabularFragment extends Fragment {
    private final String[] categories = {
            NewsApi.QuerySearch.chess.name(),
            NewsApi.QuerySearch.sudoku.name(),
            NewsApi.QuerySearch.android.name(),
            NewsApi.QuerySearch.ios.name(),
            NewsApi.QuerySearch.java.name(),
            NewsApi.QuerySearch.kotlin.name(),
            NewsApi.QuerySearch.football.name(),
            NewsApi.QuerySearch.technology.name(),
            NewsApi.QuerySearch.travel.name()

    };

    private final int[] categoryIcons = {
            R.drawable.ic_casino_black_24dp,
            R.drawable.sudoku,
            R.drawable.android,
            R.drawable.ios,
            R.drawable.java,
            R.drawable.kotlin,
            R.drawable.nav_sports,
            R.drawable.nav_tech,
            R.drawable.travel,
    };

    private FragmentHeadlinesBinding binding;

    public TabularFragment(){

    }

    public static TabularFragment newInstance(){
        return new TabularFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
        this.binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_headlines, viewGroup, false);
        ViewCompat.setElevation(binding.tablayoutHeadlines, getResources().getDimension(R.dimen.tab_layout_elevation));

        if(getActivity() != null){
            ViewPagerAdapter viewPager = new ViewPagerAdapter(getChildFragmentManager(), categories);
            binding.viewpagerHeadlines.setAdapter(viewPager);
            binding.tablayoutHeadlines.setupWithViewPager(binding.viewpagerHeadlines);
            setupTabIcons();
        }
        return this.binding.getRoot();
    }

    private void setupTabIcons(){
        TabLayout.Tab tab;
        for(int i = 0; i < categories.length; i++){
            tab = binding.tablayoutHeadlines.getTabAt(i);
            if(tab != null){
                tab.setIcon(categoryIcons[i]).setText(categories[i]);
            }
        }
    }
}
