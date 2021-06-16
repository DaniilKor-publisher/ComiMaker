package com.example.comimakerv2.adapters;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comimakerv2.R;
import com.example.comimakerv2.helpers.TemplateDatabaseHelper;
import com.example.comimakerv2.myClasses.Template;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class DetailRecyclerViewAdapter extends RecyclerView.Adapter<DetailRecyclerViewAdapter.ViewHolder>{
    ArrayList<Template> myTemplates;
    private DetailRecyclerViewAdapter.addToFavouritesListener addToFavouritesListener;

    //private TemplateDatabaseHelper mDBHelper;
    //private SQLiteDatabase mDb;

    public DetailRecyclerViewAdapter(){}

    public DetailRecyclerViewAdapter(ArrayList<Template> myTemplates, DetailRecyclerViewAdapter.addToFavouritesListener addToFavouritesListener, Context context){
        this.myTemplates = myTemplates;
        this.addToFavouritesListener = addToFavouritesListener;

        /*mDBHelper = new TemplateDatabaseHelper(context);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
         */
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_item, parent, false);
        return new DetailRecyclerViewAdapter.ViewHolder(view, addToFavouritesListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.myTitle.setText(myTemplates.get(position).getTitle());

        Picasso.get().load(myTemplates.get(position).getImageLink()).into(holder.myIcon);
    }

    @Override
    public int getItemCount() {
        return myTemplates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView myTitle;
        ImageView myIcon, isFavorite;
        DetailRecyclerViewAdapter.addToFavouritesListener addToFavouritesListener;

        public ViewHolder(@NonNull View itemView, DetailRecyclerViewAdapter.addToFavouritesListener addToFavouritesListener) {
            super(itemView);

            myTitle = itemView.findViewById(R.id.description);
            myIcon = itemView.findViewById(R.id.icon);
            isFavorite = itemView.findViewById(R.id.favouriteIcon);

            this.addToFavouritesListener = addToFavouritesListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(addToFavouritesListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            addToFavouritesListener.addToFavourites(position);
                        }
                    }
                }
            });
        }
    }

    public interface addToFavouritesListener {
        void addToFavourites(int position);
    }
}
