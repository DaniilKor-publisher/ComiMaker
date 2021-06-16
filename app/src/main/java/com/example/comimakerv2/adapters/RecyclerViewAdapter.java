package com.example.comimakerv2.adapters;

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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ArrayList<Template> templates = new ArrayList<>();
    private onTemplateListener mTemplateListener;

    public RecyclerViewAdapter(){}

    public RecyclerViewAdapter(ArrayList<Template> myTemplates, onTemplateListener onTemplateListener) {
        this.templates = myTemplates;
        this.mTemplateListener = onTemplateListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.templaterecyclerview_item, parent, false);
        return new ViewHolder(view, mTemplateListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.myTitle.setText(templates.get(position).getTitle());

        Picasso.get().load(templates.get(position).getImageLink()).into(holder.myIcon);
    }

    @Override
    public int getItemCount() {
        return templates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView myTitle;
        ImageView myIcon;
        RecyclerViewAdapter.onTemplateListener onTemplateListener;

        public ViewHolder(@NonNull View itemView, onTemplateListener onTemplateListener) {
            super(itemView);

            myTitle = itemView.findViewById(R.id.title_title);
            myIcon = itemView.findViewById(R.id.title_picture);
            this.onTemplateListener = onTemplateListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onTemplateListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onTemplateListener.onTemplateClick(position);
                        }
                    }
                }
            });
        }
    }

    public interface onTemplateListener {
        void onTemplateClick(int position);
    }
}
