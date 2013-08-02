package com.rocksauce.redditsearchandshare.model;

import android.graphics.Bitmap;

/**
 * Created by Jake on 8/1/13.
 */
public class RedditPost {
    public String userId;
    public String postContents;
    public String thumbnail;
    public Bitmap thumbnailBitmap;

    public RedditPost(String author, String title, String thumbnail) {
        userId = author;
        this.postContents = title;
        this.thumbnail = thumbnail;
    }
}

