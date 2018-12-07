package capstone.kurosaki.newsorg.NewsPackage;

import java.util.List;

import capstone.kurosaki.newsorg.News;

public class NewsWrapper {
    private final String status;
    private final int totalResults;
    private final List<News> articles;

    public NewsWrapper(String status, int totalResults, List<News> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<News> getArticles() {
        return articles;
    }
}
