package com.abanoob_samy.pexelswallpaper.listener;

import com.abanoob_samy.pexelswallpaper.response.SearchResponse;

public interface SearchResponseListener {
    void onFetch(SearchResponse response, String message);
    void onError(String message);
}
