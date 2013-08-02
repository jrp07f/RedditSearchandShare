package com.rocksauce.redditsearchandshare;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.rocksauce.redditsearchandshare.model.RedditPost;
import com.rocksauce.redditsearchandshare.utilities.ConstantsUtil;
import com.rocksauce.redditsearchandshare.utilities.HTTPUtilities;
import com.rocksauce.redditsearchandshare.utilities.JSONParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class RedditList extends Activity {

    ListView redditListView;
    RedditPostArrayAdapter redditPostArrayAdapter;
    JSONParser parser;
    ArrayList<RedditPost> postList;
    Typeface bebasNeue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reddit_list);

        bebasNeue = Typeface.createFromAsset(getAssets(), "fonts/bebasneue.ttf");

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).build();
        ImageLoader.getInstance().init(config);

        redditListView = (ListView) findViewById(R.id.redditListView);

        parser = new JSONParser();
        new getRedditJSON().execute();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reddit_list, menu);
        return true;
    }

    private class getRedditJSON extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            JSONObject homeNodeObject = parser.getJSONFromUrl(ConstantsUtil.FUNNY_REDDIT_URL);
            postList = HTTPUtilities.ParseRedditPosts(homeNodeObject);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            RedditPost[] redditPostArray = new RedditPost[postList.size()];
            redditPostArray = postList.toArray(redditPostArray);
            redditPostArrayAdapter = new RedditPostArrayAdapter(getApplicationContext(),
                    redditPostArray);

            redditListView.setAdapter(redditPostArrayAdapter);
        }
    }

    public class RedditPostArrayAdapter extends ArrayAdapter<RedditPost> {
        private final Context context;
        private final ArrayList<RedditPost> values;

        public RedditPostArrayAdapter(Context context, RedditPost[] values) {
            super(context, R.layout.reddit_post, values);
            this.context = context;
            this.values = new ArrayList<RedditPost>(Arrays.asList(values));
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.reddit_post, parent, false);
            TextView postUsername = (TextView) rowView.findViewById(R.id.userName);
            postUsername.setTypeface(bebasNeue);
            TextView postContents = (TextView) rowView.findViewById(R.id.postContents);
            final ImageView avatar = (ImageView) rowView.findViewById(R.id.redditPostAvatar);
            postUsername.setText(values.get(position).userId);
            postContents.setText(values.get(position).postContents);

            if (values.get(position).thumbnailBitmap != null)
                avatar.setImageBitmap(values.get(position).thumbnailBitmap);
            else {
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.loadImage(values.get(position).thumbnail, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        avatar.setImageBitmap(loadedImage);
                        values.get(position).thumbnailBitmap = loadedImage;
                    }
                });
            }

            return rowView;
        }
    }

}
