package com.example.comimakerv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreatureActivity extends AppCompatActivity implements View.OnClickListener{

    BottomNavigationView bottomNavigationView;
    TextView tvHello;
    ImageView imgCreate;

    ImageView sheet, sectors;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creature);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.work);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.category:
                        startActivity(new Intent(getApplicationContext(), TemplatesActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.work:
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        tvHello = findViewById(R.id.baseHello);
        imgCreate = findViewById(R.id.imCreate);
        imgCreate.setOnClickListener(this);
        
        setHello();
    }

    @Override
    public void onBackPressed()
    {
    }

    public void setHello(){
        Date currentDate = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        tvHello.setText(timeText.substring(0, 2));

        if(Integer.parseInt(timeText.substring(0, 2)) > 5 && Integer.parseInt(timeText.substring(0, 2)) < 12){
            tvHello.setText("Доброе утро!");
        }
        if(Integer.parseInt(timeText.substring(0, 2)) >= 12 && Integer.parseInt(timeText.substring(0, 2)) < 18){
            tvHello.setText("Добрый день!");
        }
        if(Integer.parseInt(timeText.substring(0, 2)) >= 18 && Integer.parseInt(timeText.substring(0, 2)) < 24){
            tvHello.setText("Добрый вечер!");
        }
        if(Integer.parseInt(timeText.substring(0, 2)) >= 0 && Integer.parseInt(timeText.substring(0, 2)) < 5){
            tvHello.setText("Добрый вечер!");
        }
    }

    public void chooseLayoutDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View choosePoopUpView = getLayoutInflater().inflate(R.layout.choose_layout, null);

        sheet = choosePoopUpView.findViewById(R.id.unifiedLayout);
        sectors = choosePoopUpView.findViewById(R.id.separatedLayout);

        dialogBuilder.setView(choosePoopUpView);
        dialog = dialogBuilder.create();
        dialog.show();

        sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreatureActivity.this, EditImageActivity.class);
                startActivity(i);
            }
        });

        sectors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreatureActivity.this, ChooseLayoutActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imCreate:
                chooseLayoutDialog();
        }
    }
}