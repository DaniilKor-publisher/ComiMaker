package com.example.comimakerv2.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comimakerv2.R;
import com.example.comimakerv2.helpers.TemplateDatabaseHelper;
import com.example.comimakerv2.myClasses.Template;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class StickerBSFragment extends BottomSheetDialogFragment {
    ArrayList<String> stickerList;
    private TemplateDatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    String urldisplay;
    ContentResolver contentResolver;
    String uridisplay;

    public StickerBSFragment(Context context, ContentResolver contentResolver) {
        stickerList = new ArrayList<>();
        mDBHelper = new TemplateDatabaseHelper(context);
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

        Cursor cursor = mDb.rawQuery("SELECT * FROM favourites", null);
        cursor.moveToNext();
        while (!cursor.isAfterLast()) {
            Template template = new Template(cursor.getString(1), cursor.getString(2));

            stickerList.add(template.getImageLink());
            cursor.moveToNext();
        }
        cursor.close();

        this.contentResolver = contentResolver;
    }

    private StickerListener mStickerListener;

    public void setStickerListener(StickerListener stickerListener) {
        mStickerListener = stickerListener;
    }

    public interface StickerListener {
        void onStickerClick(Bitmap bitmap, String category);
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sticker_emoji_dialog, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        RecyclerView rvEmoji = contentView.findViewById(R.id.rvEmoji);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rvEmoji.setLayoutManager(gridLayoutManager);
        StickerAdapter stickerAdapter = new StickerAdapter();
        rvEmoji.setAdapter(stickerAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sticker, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (stickerList.get(position).startsWith("https")) {
                Picasso.get().load(stickerList.get(position)).into(holder.imgSticker);
            } else {
                Uri uri = Uri.parse(stickerList.get(position));
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
                    holder.imgSticker.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int getItemCount() {
            return stickerList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgSticker;

            ViewHolder(View itemView) {
                super(itemView);
                imgSticker = itemView.findViewById(R.id.imgSticker);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (stickerList.get(getLayoutPosition()).startsWith("https")) {
                            new DownloadImageWithURLTask(mStickerListener).execute(stickerList.get(getLayoutPosition()));
                        } else {
                            new DownloadImageWithURITask(mStickerListener).execute(stickerList.get(getLayoutPosition()));
                        }
                    }
                });
            }
        }
    }


    private String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    private class DownloadImageWithURLTask extends AsyncTask<String, Void, Bitmap> {
        StickerListener listener;

        public DownloadImageWithURLTask(StickerListener listener) {
            this.listener = listener;
        }

        protected Bitmap doInBackground(String... urls) {
            urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            listener.onStickerClick(result, urldisplay);
        }
    }

    private class DownloadImageWithURITask extends AsyncTask<String, Void, Bitmap> {
        StickerListener listener;

        public DownloadImageWithURITask(StickerListener listener) {
            this.listener = listener;
        }

        protected Bitmap doInBackground(String... uris) {
            uridisplay = uris[0];
            Uri uri = Uri.parse(uridisplay);
            Bitmap mIcon11 = null;
            try {
                 mIcon11 = MediaStore.Images.Media.getBitmap(contentResolver, uri);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            listener.onStickerClick(result, uridisplay);
        }
    }
}
