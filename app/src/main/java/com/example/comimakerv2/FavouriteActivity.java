package com.example.comimakerv2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.comimakerv2.adapters.FavouriteTemplatesAdapter;
import com.example.comimakerv2.helpers.TemplateDatabaseHelper;
import com.example.comimakerv2.myClasses.Template;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity implements FavouriteTemplatesAdapter.onFavouriteTemplateListener,
        View.OnClickListener {

    ArrayList<Template> favouritesTemplates;
    private TemplateDatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    RecyclerView listFavouriteTemplates;
    FavouriteTemplatesAdapter adapter;
    public static final int KITKAT_VALUE = 1002;

    FloatingActionButton addTemplate;

    ImageView choosePath;
    EditText newTemplateDescription;
    Spinner chooseCategory;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    Button btSave;
    private final int Pick_image = 1;
    String pathToImage;

    String[] categories = {"Люди", "Животные", "Транспорт", "Фон", "Предметы", "Природа", "Элементы комиксов"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        favouritesTemplates = new ArrayList<>();
        mDBHelper = new TemplateDatabaseHelper(this);
        addTemplate = findViewById(R.id.btAddTemplate);
        addTemplate.setOnClickListener(this);

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
        adapter = new FavouriteTemplatesAdapter(favouritesTemplates, this, getContentResolver());
        listFavouriteTemplates.setAdapter(adapter);
    }

    @Override
    public void deleteFromFavourites(int position) {
        if (getCount(position) == 0) {
            Toast.makeText(this, "Шаблона уже нет в любимых!", Toast.LENGTH_SHORT).show();
        } else {
            long res = mDb.delete("favourites", "description = ?", new String[]{favouritesTemplates.get(position).getTitle()});

            if (res > -1) {
                Toast.makeText(this, "Шаблон удалён из любимых", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public int getCount(int position) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btAddTemplate:
                addNewTemplate();
        }
    }

    public void addNewTemplate() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View createTemplate = getLayoutInflater().inflate(R.layout.add_new_template, null);

        choosePath = createTemplate.findViewById(R.id.imageView);
        newTemplateDescription = createTemplate.findViewById(R.id.newTemplateDescription);
        chooseCategory = createTemplate.findViewById(R.id.newTemplateCategory);
        btSave = createTemplate.findViewById(R.id.saveButton);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                categories);
        chooseCategory.setAdapter(spinnerAdapter);

        dialogBuilder.setView(createTemplate);
        dialog = dialogBuilder.create();
        dialog.show();

        choosePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                }
                intent.setType("*/*");
                startActivityForResult(intent, KITKAT_VALUE);
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = newTemplateDescription.getText().toString().trim();
                final String[] category = {""};

                AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        category[0] = (String) parent.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                };

                ContentValues cv = new ContentValues();
                cv.put("description", description);
                cv.put("image_link", pathToImage);
                cv.put("category", category[0]);

                long res = mDb.insert("favourites", null, cv);
                if (res > -1) {
                    Toast.makeText(getApplicationContext(), "Шаблон добавлен в любимые", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (requestCode == KITKAT_VALUE) {
            if (resultCode == Activity.RESULT_OK) {
                final Uri imageUri = imageReturnedIntent.getData();
                pathToImage = imageUri.toString();
                choosePath.setImageResource(R.drawable.ic_check);
            }
        }
    }
}
