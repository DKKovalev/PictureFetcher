package com.playground.dkkovalev.picturefetcher.Model;

/**
 * Created by DKKovalev on 11.04.2016.
 */
public class GalleryItem {
    private String caption;
    private String id;
    private String Url;

    public GalleryItem(String caption, String id, String url) {
        this.caption = caption;
        this.id = id;
        Url = url;
    }

    public GalleryItem() {
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
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
