package com.abanoob_samy.pexelswallpaper.listener;

import com.abanoob_samy.pexelswallpaper.response.CuratedResponse;

public interface CuratedResponseListener {
    void onFetch(CuratedResponse response, String message);
    void onError(String message);
}
