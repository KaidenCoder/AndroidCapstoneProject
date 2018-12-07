package capstone.kurosaki.newsorg.NewsPackage;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import capstone.kurosaki.newsorg.News;
import capstone.kurosaki.newsorg.databinding.NewsItemBinding;
import capstone.kurosaki.newsorg.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private final NewsAdapterListener listener;
    private List<News> news;
    private LayoutInflater layoutInflater;

    public NewsAdapter(List<News> news, NewsAdapterListener listener) {
        this.news = news;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        NewsItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.news_item, parent, false);
        return new NewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int i) {
        newsViewHolder.binding.setNews(news.get(i));
        newsViewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return news == null ? 0 : news.size();
    }

    public void setNews(List<News> news) {
        if (news != null) {
            this.news = news;
            notifyDataSetChanged();
        }
    }

    public interface NewsAdapterListener {
        void onNewsItemClicked(News news);

        void onItemOptionsClicked(News news);
    }

    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final NewsItemBinding binding;

        public NewsViewHolder(final NewsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.ivOptions.setOnClickListener(this);
            this.binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = this.getAdapterPosition();
            if (v instanceof ImageView) {
                listener.onItemOptionsClicked(news.get(index));
            } else {
                listener.onNewsItemClicked(news.get(index));
            }
        }
    }
}


