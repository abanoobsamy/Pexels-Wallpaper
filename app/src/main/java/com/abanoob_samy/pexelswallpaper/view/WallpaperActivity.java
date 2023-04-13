package com.abanoob_samy.pexelswallpaper.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.abanoob_samy.pexelswallpaper.R;
import com.abanoob_samy.pexelswallpaper.databinding.ActivityWallpaperBinding;
import com.abanoob_samy.pexelswallpaper.model.Photo;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class WallpaperActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 213;
    private ActivityWallpaperBinding binding;

    private ProgressDialog dialog;

    private Photo photo;

    private boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWallpaperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        photo = (Photo) getIntent().getSerializableExtra(HomeActivity.EXTRA_PHOTO);

        setupActionBar();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.create();
        dialog.show();

        getDataPhoto();
    }

    private void setupActionBar() {

        setSupportActionBar(binding.toolbarWallpaper);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle(photo.getPhotographer());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getDataPhoto() {

        Picasso.get()
                .load(photo.getSrc().getOriginal())
                .placeholder(R.drawable.ic_error_outline)
                .into(binding.imageWallpaper);

        dialog.dismiss();

        binding.fabDownload.setOnClickListener(view -> {

            DownloadManager downloadManager = null;
            downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

            Uri uri = Uri.parse(photo.getSrc().getLarge2x());

            DownloadManager.Request request = new DownloadManager.Request(uri);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                return;
            }

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle("Wallpaper_" + photo.getPhotographer())
                    .setMimeType("image/jpeg")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                            "Wallpaper_" + photo.getPhotographer() + ".jpg");

            downloadManager.enqueue(request);

            Toast.makeText(getApplicationContext(), "Downloaded Completed.", Toast.LENGTH_SHORT).show();
        });

        binding.fabWallpaper.setOnClickListener(view -> {

            onAddButtonClicked();
        });
    }

    private void onAddButtonClicked() {

        Animation rotateOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_open);
        Animation rotateClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_close);
        Animation fromBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.from_bottom_anim);
        Animation toBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.to_bottom_anim);

        if (clicked) {
            // if it clicked = true, make it = false, which make it close
            binding.fabWallpaperHome.setVisibility(View.GONE);
            binding.fabWallpaperLock.setVisibility(View.GONE);

            binding.fabWallpaper.startAnimation(rotateClose);
            binding.fabWallpaperHome.startAnimation(toBottom);
            binding.fabWallpaperLock.startAnimation(toBottom);

//            binding.fabWallpaper.setImageDrawable(getDrawable(R.drawable.ic_wallpaper));

            clicked = false;
        }
        else {
            // if it clicked = false, make it = true, which make it open
            binding.fabWallpaperHome.setVisibility(View.VISIBLE);
            binding.fabWallpaperLock.setVisibility(View.VISIBLE);

            binding.fabWallpaper.startAnimation(rotateOpen);
            binding.fabWallpaperHome.startAnimation(fromBottom);
            binding.fabWallpaperLock.startAnimation(fromBottom);

//            binding.fabWallpaper.setImageDrawable(getDrawable(R.drawable.ic_close));

            setUpWallpaperMethods();

            clicked = true;
        }
    }

    private void setUpWallpaperMethods() {

        binding.fabWallpaperHome.setOnClickListener(view -> {

            onAddButtonClicked();

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
            Bitmap bitmap = ((BitmapDrawable) binding.imageWallpaper.getDrawable()).getBitmap();

            try {
                wallpaperManager.setBitmap(bitmap);
                Toast.makeText(getApplicationContext(), "Wallpaper Added Home Screen Successfully.", Toast.LENGTH_SHORT).show();
            }
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Couldn't add wallpaper!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.fabWallpaperLock.setOnClickListener(view -> {

            onAddButtonClicked();

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
            Bitmap bitmap = ((BitmapDrawable) binding.imageWallpaper.getDrawable()).getBitmap();

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                    Toast.makeText(getApplicationContext(), "Wallpaper Added Lock Screen Successfully.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Android less than Nougat!", Toast.LENGTH_SHORT).show();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Couldn't add wallpaper!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}