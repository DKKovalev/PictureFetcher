package com.playground.dkkovalev.picturefetcher.Model;

/**
 * Created by DKKovalev on 11.04.2016.
 */
public class FlickrPhotoObject {
    private String caption;
    private String id;
    private String url;

    public FlickrPhotoObject(String caption, String id, String url) {
        this.caption = caption;
        this.id = id;
        this.url = url;
    }

    public FlickrPhotoObject() {
    }

    @Override
    public String toString() {
        return caption;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String server, String secret) {
        this.url = "https://farm2.static.flickr.com/" + server + "/" + id + "_" + secret + ".jpg";
    }
}
