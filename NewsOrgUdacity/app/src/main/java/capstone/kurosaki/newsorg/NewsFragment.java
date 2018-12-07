package capstone.kurosaki.newsorg;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import java.util.List;

import capstone.kurosaki.newsorg.NewsPackage.NewsAdapter;
import capstone.kurosaki.newsorg.NewsPackage.NewsHelper;
import capstone.kurosaki.newsorg.NewsPackage.NewsViewModel;
import capstone.kurosaki.newsorg.Retrofit.NewsApi;
import capstone.kurosaki.newsorg.databinding.NewsFragmentBinding;
import capstone.kurosaki.newsorg.utils.OptionsBottomSheet;

public class NewsFragment extends Fragment implements NewsAdapter.NewsAdapterListener{
    public static final String PARAM_CATEGORY = "param-category";
    public static final String PARAM_LIST_STATE = "param-state";
    private final NewsAdapter newsAdapter = new NewsAdapter(null, this);
    private NewsApi.QuerySearch newsCategory;
    private NewsFragmentBinding binding;
    private boolean showSaved = false;
    private Parcelable listState;

    public static NewsFragment newInstance(NewsApi.QuerySearch category){
        NewsFragment fragment = new NewsFragment();
        if(category == null){
            return fragment;
        }
        Bundle args = new Bundle();
        args.putString(PARAM_CATEGORY, category.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            newsCategory = NewsApi.QuerySearch
                    .valueOf(getArguments().getString(PARAM_CATEGORY));
        } else {
            showSaved = true;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        binding = DataBindingUtil
                .inflate(inflater, R.layout.news_fragment, container, false);
        RecyclerView recyclerView = binding.rvNewsPosts;
        recyclerView.setAdapter(newsAdapter);
        if(getContext() != null){
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_view_divider));
            recyclerView.addItemDecoration(dividerItemDecoration);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(PARAM_LIST_STATE);
        }
        NewsViewModel viewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        if (showSaved) {
            viewModel.getAllSaved().observeForever(new Observer<List<News>>() {
                @Override
                public void onChanged(@Nullable List<News> news) {
                    if (news != null) {
                        newsAdapter.setNews(news);
                        restoreRecyclerViewState();
                    } else {
                        newsAdapter.notifyDataSetChanged();
                        restoreRecyclerViewState();
                    }
                }
            });
        } else {
            NewsHelper specs = new NewsHelper();
            specs.setQ(newsCategory);
            viewModel.getNewsHeadlines(specs).observe(this, new Observer<List<News>>() {
                @Override
                public void onChanged(@Nullable List<News> news) {
                    if (news != null) {
                        newsAdapter.setNews(news);
                        restoreRecyclerViewState();
                    }
                }
            });
        }
    }

    @Override
    public void onNewsItemClicked(News news) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.PARAM_NEWS, news);
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);
        binding.rvNewsPosts.setLayoutAnimation(controller);
        binding.rvNewsPosts.scheduleLayoutAnimation();
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().overridePendingTransition(R.anim.slide_up_animation, R.anim.fade_exit_transition);
        }
    }

    @Override
    public void onItemOptionsClicked(News news) {
        OptionsBottomSheet bottomSheet = OptionsBottomSheet.getInstance(news.getTitle(), news.getUrl(), news.getId(), showSaved);
        if (getActivity() != null) {
            bottomSheet.show(getActivity().getSupportFragmentManager(), bottomSheet.getTag());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (binding.rvNewsPosts.getLayoutManager() != null) {
            listState = binding.rvNewsPosts.getLayoutManager().onSaveInstanceState();
            outState.putParcelable(PARAM_LIST_STATE, listState);
        }
    }

    private void restoreRecyclerViewState() {
        if (binding.rvNewsPosts.getLayoutManager() != null) {
            binding.rvNewsPosts.getLayoutManager().onRestoreInstanceState(listState);
        }
    }
}
