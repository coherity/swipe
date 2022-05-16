package gg.cann1neof.mdev.swipenews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.yalantis.library.Koloda;

import java.time.Instant;
import java.util.List;

import com.kwabenaberko.newsapilib.NewsApiClient;

public class MainActivity extends AppCompatActivity {
    private SwipeAdapter adapter;
    private List<Article> articleList;
    private NewsApiClient newsApiClient;
    Koloda koloda;
    MaterialToolbar topAppBar;
    SharedPreferences sharedPreferences;
    final String SAVED_TEXT = "gg.cann1neof.mdev.swipenews.ClientSources2";
    Instant instant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsApiClient = new NewsApiClient("59c439c397464c1a80edfa391844d86a");

        topAppBar = (MaterialToolbar) findViewById(R.id.topAppBar);
        koloda = (Koloda) findViewById(R.id.koloda);

        koloda.setAdapter(adapter);
        koloda.setKolodaListener(new MyKolodaListener(MainActivity.this, articleList));

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchNews();
            }
        });
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.add){
                    Intent intent = new Intent(MainActivity.this, AddSourceActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        fetchNews();
    }

    private void fetchNews(){
        String domains = getSources(SAVED_TEXT, this);

        if(domains != null){
            getLatestNews(domains);
        } else {
            getTopHeadlinesNews();
        }
    }

    private void getTopHeadlinesNews(){
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .category("business")
                        .country("ru")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        articleList = response.getArticles();
                        adapter = new SwipeAdapter(MainActivity.this, articleList);
                        koloda.setAdapter(adapter);
                        koloda.setKolodaListener(new MyKolodaListener(MainActivity.this, articleList));
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.v("cnne", throwable.getMessage());
                    }
                }
        );
    }

    private void getLatestNews(String domains){
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .domains(domains)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        articleList = response.getArticles();
                        adapter = new SwipeAdapter(MainActivity.this, articleList);
                        koloda.setAdapter(adapter);
                        koloda.setKolodaListener(new MyKolodaListener(MainActivity.this, articleList));
                        if(articleList == null || articleList.size() == 0){
                            getTopHeadlinesNews();
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.v("cnne", throwable.getMessage());
                    }
                }
        );
    }

    public static String getSources(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}

// kommersant.ru
// fontanka.ru
// investing.com