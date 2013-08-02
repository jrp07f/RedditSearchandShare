package com.rocksauce.redditsearchandshare.utilities;

import android.util.Log;

import com.rocksauce.redditsearchandshare.model.RedditPost;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jake on 8/1/13.
 */
public class HTTPUtilities {

    public static String httpGetRequest(String request) {
        String response = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(request);
            HttpResponse responseGet = client.execute(get);
            HttpEntity resEntityGet = responseGet.getEntity();
            if (resEntityGet != null) {
                response = EntityUtils.toString(resEntityGet);
                Log.i("GET RESPONSE", response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static ArrayList<RedditPost> ParseRedditPosts(JSONObject homeNodeObject) {
        // creating new HashMap
        ArrayList<RedditPost> posts = new ArrayList<RedditPost>();

        try {
            // Getting Array of Contacts
            JSONArray contacts = homeNodeObject.getJSONObject(ConstantsUtil.DATA)
                    .getJSONArray(ConstantsUtil.CHILDREN);

            // looping through All Contacts
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject data = contacts.getJSONObject(i)
                        .getJSONObject(ConstantsUtil.DATA);

                // Storing each json item in variable
                String author = data.getString(ConstantsUtil.AUTHOR);
                String title = data.getString(ConstantsUtil.TITLE);
                String thumbnail = data.getString(ConstantsUtil.THUMBNAIL);


                // adding each child node to HashMap key => value
                posts.add(new RedditPost(author, title, thumbnail));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return posts;
    }
}
