package com.abanoob_samy.pexelswallpaper.api;

import com.abanoob_samy.pexelswallpaper.response.CuratedResponse;
import com.abanoob_samy.pexelswallpaper.response.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface PexelsApi {

    @Headers({
            "Accept: application/json",
            "Authorization: 563492ad6f917000010000016c4a787d0a7b4073a880b8bdd6eca6a9"
    })

    //per_page mean how many photo you need to appear.
    //https://api.pexels.com/v1/curated?per_page=1

    @GET("curated")
    Call<CuratedResponse> getWallpapers(@Query("page") String page, @Query("per_page") String per_page);

}
