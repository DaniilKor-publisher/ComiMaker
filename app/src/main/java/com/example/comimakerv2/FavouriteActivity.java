package com.example.comimakerv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import com.example.comimakerv2.adapters.FavouriteTemplatesAdapter;
import com.example.comimakerv2.helpers.TemplateDatabaseHelper;
import com.example.comimakerv2.myClasses.Template;

import java.io.IOException;
import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity implements FavouriteTemplatesAdapter.onFavouriteTemplateListener {

    ArrayList<Template> favouritesTemplates;
    private TemplateDatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    RecyclerView listFavouriteTemplates;
    FavouriteTemplatesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        favouritesTemplates = new ArrayList<>();
        mDBHelper = new TemplateDatabaseHelper(this);

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
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Template template = new Template(cursor.getString(1), cursor.getString(2));

            favouritesTemplates.add(template);
            cursor.moveToNext();
        }
        cursor.close();

        listFavouriteTemplates = findViewById(R.id.rv);
        listFavouriteTemplates.setLayoutManager(new LinearLayoutManager(this));
        listFavouriteTemplates.setHasFixedSize(true);
        adapter = new FavouriteTemplatesAdapter(favouritesTemplates, this);
        listFavouriteTemplates.setAdapter(adapter);
    }

    @Override
    public void deleteFromFavourites(int position) {
        if(getCount(position) == 0){
            Toast.makeText(this, "Шаблона уже нет в любимых!", Toast.LENGTH_SHORT).show();
        }
        else {
            long res = mDb.delete("favourites", "description = ?", new String[]{favouritesTemplates.get(position).getTitle()});

            if (res > -1) {
                Toast.makeText(this, "Шаблон удалён из любимых", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public int getCount(int position){
        Cursor c = null;
        try {
            String query = "select count(*) from favourites where description = ?";
            c = mDb.rawQuery(query, new String[]{favouritesTemplates.get(position).getTitle()});
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
            return 0;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
}