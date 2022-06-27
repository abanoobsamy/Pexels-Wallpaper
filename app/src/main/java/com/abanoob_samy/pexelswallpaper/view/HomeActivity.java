package com.abanoob_samy.pexelswallpaper.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.abanoob_samy.pexelswallpaper.R;
import com.abanoob_samy.pexelswallpaper.adapter.PexelsAdapter;
import com.abanoob_samy.pexelswallpaper.databinding.ActivityHomeBinding;
import com.abanoob_samy.pexelswallpaper.listener.CuratedResponseListener;
import com.abanoob_samy.pexelswallpaper.listener.OnPhotoClickListener;
import com.abanoob_samy.pexelswallpaper.listener.SearchResponseListener;
import com.abanoob_samy.pexelswallpaper.model.Photo;
import com.abanoob_samy.pexelswallpaper.request.PexelsRequestManager;
import com.abanoob_samy.pexelswallpaper.response.CuratedResponse;
import com.abanoob_samy.pexelswallpaper.response.SearchResponse;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnPhotoClickListener {

    public static final String EXTRA_PHOTO = "photo";

    private ActivityHomeBinding binding;

    private PexelsAdapter adapter;
    private PexelsRequestManager manager;
    private ProgressDialog dialog;

    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupActionBar();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.create();
        dialog.show();

        manager = new PexelsRequestManager(this);
        manager.getCuratedWallpapers(curatedResponseListener, "1");

        goToPage();
    }

    private void setupActionBar() {

        setSupportActionBar(binding.toolbarHome);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Home");
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private final CuratedResponseListener curatedResponseListener = new CuratedResponseListener() {
        @Override
        public void onFetch(@NonNull CuratedResponse response, String message) {

            dialog.dismiss();

            if (response.getPhotos().isEmpty()) {

                Toast.makeText(getApplicationContext(), "No Image Found!", Toast.LENGTH_SHORT).show();
                return;
            }

            page = response.getPage();

            showData(response.getPhotos());
        }

        @Override
        public void onError(String message) {

            dialog.dismiss();
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    };

    private final SearchResponseListener searchResponseListener = new SearchResponseListener() {
        @Override
        public void onFetch(@NonNull SearchResponse response, String message) {

            dialog.dismiss();

            if (response.getPhotos().isEmpty()) {

                Toast.makeText(getApplicationContext(), "No Image Found!", Toast.LENGTH_SHORT).show();
                return;
            }

            page = response.getPage();

            showData(response.getPhotos());
        }

        @Override
        public void onError(String message) {

            dialog.dismiss();
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    };

    private void showData(List<Photo> photos) {

        adapter = new PexelsAdapter(this, photos, this);

        binding.recyclerHome.setHasFixedSize(true);
        binding.recyclerHome.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerHome.setAdapter(adapter);
    }

    private void goToPage() {

        binding.fabNext.setOnClickListener(view -> {

            String pageNext = String.valueOf(page + 1);

            manager.getCuratedWallpapers(curatedResponseListener, pageNext);



            dialog.setTitle("Loading Next Page...");
            dialog.show();
        });

        binding.fabPrev.setOnClickListener(view -> {

            if (page > 1) {

                String pagePrev = String.valueOf(page - 1);

                manager.getCuratedWallpapers(curatedResponseListener, pagePrev);

                dialog.setTitle("Loading Previous Page...");
                dialog.show();
            } else
                Toast.makeText(getApplicationContext(), "You are in the first page.", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onClick(@NonNull Photo photo) {

        startActivity(new Intent(HomeActivity.this, WallpaperActivity.class)
                .putExtra(EXTRA_PHOTO, photo));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        MenuItem menuItem = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                manager.getSearchWallpapers(searchResponseListener, query, "1");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                manager.getSearchWallpapers(searchResponseListener, newText, "1");
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}