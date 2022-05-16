package gg.cann1neof.mdev.swipenews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.models.Article;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.util.List;

public class SwipeAdapter extends BaseAdapter {
    private Context context;
    private List<Article> list;
    private TextView textTitle;
    private TextView textAuthor;
    private TextView textDescription;
    private Button butt;

    public SwipeAdapter(Context context, List<Article> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if(list == null){
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View converView, ViewGroup parent) {
        View view;

        if(converView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_koloda, parent, false);
            setData(i, view);
        }else {
            view = converView;
        }
        return view;
    }

    private void setData(int i, View view){
        textTitle = view.findViewById(R.id.title);
        textAuthor = view.findViewById(R.id.author);
        textDescription = view.findViewById(R.id.description);

        new DownloadImageTask((ImageView) view.findViewById(R.id.image))
                .execute(list.get(i).getUrlToImage());

        textTitle.setText(list.get(i).getTitle());
        textAuthor.setText(list.get(i).getSource().getName());
        textDescription.setText(list.get(i).getDescription());
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                Log.e("MalformedURLException", e.getMessage());
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if(result == null){
                bmImage.setVisibility(View.GONE);
            }
            else{
                bmImage.setImageBitmap(result);
            }
        }
    }
}
