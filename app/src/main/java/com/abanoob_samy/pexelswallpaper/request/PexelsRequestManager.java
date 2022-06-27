package com.abanoob_samy.pexelswallpaper.request;

import android.content.Context;
import android.widget.Toast;

import com.abanoob_samy.pexelswallpaper.api.PexelsApi;
import com.abanoob_samy.pexelswallpaper.api.PexelsSearchApi;
import com.abanoob_samy.pexelswallpaper.listener.CuratedResponseListener;
import com.abanoob_samy.pexelswallpaper.listener.SearchResponseListener;
import com.abanoob_samy.pexelswallpaper.response.CuratedResponse;
import com.abanoob_samy.pexelswallpaper.response.SearchResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PexelsRequestManager {

    private static final String BASE_URL = "https://api.pexels.com/v1/";

    private Context context;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHttp())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public PexelsRequestManager(Context context) {
        this.context = context;
    }

    private static Interceptor provideLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    private static OkHttpClient provideOkHttp() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(provideLoggingInterceptor())
                .build();
    }

    public void getCuratedWallpapers(CuratedResponseListener listener, String page) {

        PexelsApi pexelsApi = retrofit.create(PexelsApi.class);

        Call<CuratedResponse> responseCall = pexelsApi.getWallpapers(page, "20");

        responseCall.enqueue(new Callback<CuratedResponse>() {
            @Override
            public void onResponse(Call<CuratedResponse> call, Response<CuratedResponse> response) {

                if (!response.isSuccessful()) {

                    Toast.makeText(context, "An Error Occurred!", Toast.LENGTH_SHORT).show();
                    return;

                }

                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<CuratedResponse> call, Throwable t) {

                listener.onError(t.getLocalizedMessage());
            }
        });
    }

    public void getSearchWallpapers(SearchResponseListener listener, String query, String page) {

        PexelsSearchApi pexelsApi = retrofit.create(PexelsSearchApi.class);

        Call<SearchResponse> responseCall = pexelsApi.getSearchPhoto(query, page, "20");

        responseCall.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {

                if (!response.isSuccessful()) {

                    Toast.makeText(context, "An Error Occurred!", Toast.LENGTH_SHORT).show();
                    return;

                }

                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {

                listener.onError(t.getLocalizedMessage());
            }
        });
    }
}
