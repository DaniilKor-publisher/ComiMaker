package com.example.comimakerv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.comimakerv2.adapters.RecyclerViewAdapter;
import com.example.comimakerv2.helpers.TemplateDatabaseHelper;
import com.example.comimakerv2.myClasses.Template;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;

public class TemplatesActivity extends AppCompatActivity implements RecyclerViewAdapter.onTemplateListener{

    BottomNavigationView bottomNavigationView;
    RecyclerView rv;
    private RecyclerViewAdapter adapter;
    ArrayList<Template> templates;
    private TemplateDatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.category);
        templates = new ArrayList<>();
        mDBHelper = new TemplateDatabaseHelper(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.category:
                        return true;

                    case R.id.work:
                        startActivity(new Intent(getApplicationContext(), CreatureActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        rv = (RecyclerView) findViewById(R.id.settingList);

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

        Cursor cursor = mDb.rawQuery("SELECT * FROM titles", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Template template = new Template(cursor.getString(1), cursor.getString(2));

            templates.add(template);
            cursor.moveToNext();
        } cursor.close();

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new RecyclerViewAdapter(templates, this);
        rv.setAdapter(adapter);
    }

    @Override
    public void onTemplateClick(int position) {
        if(position == 5){
            Toast.makeText(this, "Находится в разработке", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent i = new Intent(TemplatesActivity.this, ListTemplatesActivity.class);
            i.putExtra("kind", templates.get(position).getTitle());
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {

    }
}