package com.abanoob_samy.pexelswallpaper.model;

import java.io.Serializable;

public class Photo implements Serializable {

    private int id;
    private int width;
    private int height;
    private String url;
    private String photographer;
    private String photographer_url;
    private int photographer_id;
    private String avg_color;
    private Src src;
    private boolean liked;
    private String alt;

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getUrl() {
        return url;
    }

    public String getPhotographer() {
        return photographer;
    }

    public String getPhotographer_url() {
        return photographer_url;
    }

    public int getPhotographer_id() {
        return photographer_id;
    }

    public String getAvg_color() {
        return avg_color;
    }

    public Src getSrc() {
        return src;
    }

    public boolean isLiked() {
        return liked;
    }

    public String getAlt() {
        return alt;
    }
}
