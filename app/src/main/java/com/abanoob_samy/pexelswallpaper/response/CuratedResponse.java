package com.abanoob_samy.pexelswallpaper.response;

import com.abanoob_samy.pexelswallpaper.model.Photo;

import java.util.List;

public class CuratedResponse {

    private int page;
    private int per_page;
    private List<Photo> photos;
    private String next_page;

    public int getPage() {
        return page;
    }

    public int getPer_page() {
        return per_page;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public String getNext_page() {
        return next_page;
    }
}
