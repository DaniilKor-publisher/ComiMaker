package com.example.comimakerv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import com.example.comimakerv2.adapters.DetailRecyclerViewAdapter;
import com.example.comimakerv2.helpers.TemplateDatabaseHelper;
import com.example.comimakerv2.myClasses.Template;

public class ListTemplatesActivity extends AppCompatActivity implements DetailRecyclerViewAdapter.addToFavouritesListener {
    ArrayList<Template> templates;
    private TemplateDatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    RecyclerView listTemplates;
    DetailRecyclerViewAdapter adapter;
    String kindOfTemplates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_templates);

        templates = new ArrayList<>();
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

        Intent i = getIntent();
        kindOfTemplates = i.getStringExtra("kind");

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + translateToEnglish(kindOfTemplates), null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Template template = new Template(cursor.getString(1), cursor.getString(2));

            templates.add(template);
            cursor.moveToNext();
        }
        cursor.close();

        listTemplates = findViewById(R.id.rv);
        listTemplates.setLayoutManager(new LinearLayoutManager(this));
        listTemplates.setHasFixedSize(true);
        adapter = new DetailRecyclerViewAdapter(templates, this);
        listTemplates.setAdapter(adapter);
    }

    public static String translateToEnglish(String title){
        switch (title) {
            case "????????":
                return "people";
            case "????????????????":
                return "animals";
            case "??????????????????":
                return "transport";
            case "????????":
                return "backgrounds";
            case "????????????????":
                return "tools";
            case "????????????????":
                return "layout";
            case "??????????????":
                return "nature";
            case "???????????????? ????????????????":
                return "comics_elements";
            case "??????????????":
                return "favourites";
        }
        return null;
    }

    @Override
    public void addToFavourites(int position) {
        Template template = new Template();
        template.title = templates.get(position).getTitle();
        template.imageLink = templates.get(position).getImageLink();
        template.category = translateToEnglish(kindOfTemplates);

        ContentValues cv = new ContentValues();
        cv.put("description", template.getTitle());
        cv.put("image_link", template.getImageLink());
        cv.put("category", template.getCategory());

        if (getCount(position) == 0) {
            long res = mDb.insert("favourites", null, cv);

            if (res > -1) {
                Toast.makeText(this, "???????????? ???????????????? ?? ??????????????", Toast.LENGTH_SHORT).show();

                mDb.delete(template.getCategory(), "description" + " = ?", new String[]{template.getTitle()});
            } else {
                Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "???????????? ?????? ?? ??????????????", Toast.LENGTH_SHORT).show();
        }
    }

    public int getCount(int position) {
        Cursor c = null;
        try {
            String query = "select count(*) from favourites where description = ?";
            c = mDb.rawQuery(query, new String[]{templates.get(position).getTitle()});
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