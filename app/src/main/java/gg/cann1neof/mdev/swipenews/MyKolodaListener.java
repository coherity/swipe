package gg.cann1neof.mdev.swipenews;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kwabenaberko.newsapilib.models.Article;
import com.yalantis.library.Koloda;
import com.yalantis.library.KolodaListener;

import java.util.List;

public class MyKolodaListener implements KolodaListener{
    private Context context;
    private List<Article> articleList;

    public MyKolodaListener(Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @Override
    public void onCardDoubleTap(int i) {
        KolodaListener.DefaultImpls.onCardDoubleTap(this, i);
    }

    @Override
    public void onCardDrag(int i, @NonNull View view, float v) {
        KolodaListener.DefaultImpls.onCardDrag(this, i, view, v);
    }

    @Override
    public void onCardLongPress(int i) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(articleList.get(i+1).getUrl()));
            context.startActivity(browserIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No application can handle this request."
                    + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onCardSingleTap(int i) {
        KolodaListener.DefaultImpls.onCardSingleTap(this, i);
    }

    @Override
    public void onCardSwipedLeft(int i) {
        KolodaListener.DefaultImpls.onCardSwipedLeft(this, i);

    }

    @Override
    public void onCardSwipedRight(int i) {
        KolodaListener.DefaultImpls.onCardSwipedRight(this, i);
    }

    @Override
    public void onClickLeft(int i) {
        KolodaListener.DefaultImpls.onClickLeft(this, i);
    }

    @Override
    public void onClickRight(int i) {
        KolodaListener.DefaultImpls.onClickRight(this, i);
    }

    @Override
    public void onEmptyDeck() {
        KolodaListener.DefaultImpls.onEmptyDeck(this);
    }

    @Override
    public void onNewTopCard(int i) {
        KolodaListener.DefaultImpls.onNewTopCard(this, i);
    }
}