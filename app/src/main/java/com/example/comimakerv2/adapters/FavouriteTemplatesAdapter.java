package com.example.comimakerv2.adapters;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comimakerv2.R;
import com.example.comimakerv2.myClasses.Template;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FavouriteTemplatesAdapter extends RecyclerView.Adapter<FavouriteTemplatesAdapter.ViewHolder> {

    ArrayList<Template> favouriteTemplates;
    private onFavouriteTemplateListener deleteFromFavouritesListener;
    Bitmap bitmap;
    ContentResolver contentResolver;

    public FavouriteTemplatesAdapter() {}

    public FavouriteTemplatesAdapter(ArrayList<Template> myTemplates, onFavouriteTemplateListener onFavouriteTemplateListener, ContentResolver contentResolver) {
        this.favouriteTemplates = myTemplates;
        this.deleteFromFavouritesListener = onFavouriteTemplateListener;
        this.contentResolver = contentResolver;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_template_item, parent, false);
        return new FavouriteTemplatesAdapter.ViewHolder(view, deleteFromFavouritesListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (favouriteTemplates.get(position).getImageLink().startsWith("https")) {
            Picasso.get().load(favouriteTemplates.get(position).getImageLink()).into(holder.icon);
        } else {
            Uri uri = Uri.parse(favouriteTemplates.get(position).getImageLink());
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
                holder.icon.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        holder.description.setText(favouriteTemplates.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return favouriteTemplates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView description;
        ImageView icon;
        onFavouriteTemplateListener onFavouriteTemplateListener;

        public ViewHolder(@NonNull View itemView, onFavouriteTemplateListener onFavouriteTemplateListener) {
            super(itemView);

            description = itemView.findViewById(R.id.favouriteDescription);
            icon = itemView.findViewById(R.id.favouriteIcon);

            this.onFavouriteTemplateListener = onFavouriteTemplateListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onFavouriteTemplateListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onFavouriteTemplateListener.deleteFromFavourites(position);
                        }
                    }
                }
            });
        }
    }

    public interface onFavouriteTemplateListener {
        public void deleteFromFavourites(int position);
    }
}
