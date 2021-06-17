package com.example.comimakerv2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comimakerv2.R;
import com.example.comimakerv2.myClasses.Template;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavouriteTemplatesAdapter extends RecyclerView.Adapter<FavouriteTemplatesAdapter.ViewHolder> {

    ArrayList<Template> favouriteTemplates;
    private onFavouriteTemplateListener deleteFromFavouritesListener;

    public FavouriteTemplatesAdapter(){}

    public FavouriteTemplatesAdapter(ArrayList<Template> myTemplates,  onFavouriteTemplateListener onFavouriteTemplateListener){
        this.favouriteTemplates = myTemplates;
        this.deleteFromFavouritesListener = onFavouriteTemplateListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_template_item, parent, false);
        return new FavouriteTemplatesAdapter.ViewHolder(view, deleteFromFavouritesListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(favouriteTemplates.get(position).getImageLink()).into(holder.icon);
        holder.description.setText(favouriteTemplates.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return favouriteTemplates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

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
                    if(onFavouriteTemplateListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onFavouriteTemplateListener.deleteFromFavourites(position);
                        }
                    }
                }
            });
        }
    }

    public interface onFavouriteTemplateListener{
        public void deleteFromFavourites(int position);
    }
}
