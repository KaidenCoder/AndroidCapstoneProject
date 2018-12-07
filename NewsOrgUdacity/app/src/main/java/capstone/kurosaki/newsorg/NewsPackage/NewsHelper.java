package capstone.kurosaki.newsorg.NewsPackage;

import capstone.kurosaki.newsorg.Retrofit.NewsApi;

public class NewsHelper {

    private String q;

    private String name = null;

    public String getQ() {
        return q;
    }

    public void setQ(NewsApi.QuerySearch q) {
        this.q = q.name();
    }

    public String getName(){ return name;}

    public void setName(String name){this.name = name;}

}

