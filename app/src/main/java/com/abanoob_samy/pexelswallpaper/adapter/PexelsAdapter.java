package com.abanoob_samy.pexelswallpaper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abanoob_samy.pexelswallpaper.R;
import com.abanoob_samy.pexelswallpaper.databinding.HomeListBinding;
import com.abanoob_samy.pexelswallpaper.listener.OnPhotoClickListener;
import com.abanoob_samy.pexelswallpaper.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PexelsAdapter extends RecyclerView.Adapter<PexelsAdapter.PexelsHolder> {

    private List<Photo> photos;
    private Context context;
    private OnPhotoClickListener photoClickListener;

    public PexelsAdapter(Context context, List<Photo> photos, OnPhotoClickListener photoClickListener) {
        this.context = context;
        this.photos = photos;
        this.photoClickListener = photoClickListener;
    }

    @NonNull
    @Override
    public PexelsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new PexelsHolder(LayoutInflater.from(context).inflate(R.layout.home_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PexelsHolder holder, int position) {

        Photo photo = photos.get(position);

        Picasso.get().load(photo.getSrc().getMedium()).placeholder(R.drawable.ic_error_outline).into(holder.binding.imageList);
    }

    @Override
    public int getItemCount() {

        if (photos != null)
            return photos.size();
        else
            return 0;
    }

    public class PexelsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final HomeListBinding binding;

        public PexelsHolder(@NonNull View itemView) {
            super(itemView);

            binding = HomeListBinding.bind(itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            photoClickListener.onClick(photos.get(getAdapterPosition()));
        }
    }
}
