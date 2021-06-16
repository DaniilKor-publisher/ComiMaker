package com.example.comimakerv2;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.comimakerv2.adapters.SettingAdapter;
import com.example.comimakerv2.myClasses.MyAdjusts;
import com.example.comimakerv2.myClasses.Template;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.comimakerv2.LoginActivity.APP_PREFERENCES;
import static com.example.comimakerv2.LoginActivity.APP_PREFERENCES_NAME;


public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ListView lv;

    SharedPreferences sPref;

    TextView txtName;
    ImageView imgAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.category:
                        startActivity(new Intent(getApplicationContext(), TemplatesActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.work:
                        startActivity(new Intent(getApplicationContext(), CreatureActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });

        txtName = (TextView) findViewById(R.id.profile_name);
        imgAvatar = (ImageView) findViewById(R.id.profile_image);

        sPref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        if(sPref.contains(APP_PREFERENCES_NAME)) {
            txtName.setText(sPref.getString(APP_PREFERENCES_NAME, ""));
        }

        SettingAdapter adapter = new SettingAdapter(this, makeSettings());
        lv = (ListView) findViewById(R.id.settingList);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        Intent i = new Intent(ProfileActivity.this, ListTemplatesActivity.class);
                        i.putExtra("kind", "Любимые");
                        startActivity(i);
                }
            }
        });
    }

    MyAdjusts[] makeSettings() {
        MyAdjusts[] arr = new MyAdjusts[3];

        String[] titlesArr = {"Мои комиксы", "Любимые шаблоны", "Настройки"};

        int[] imageResources = {R.drawable.ic_recent, R.drawable.ic_favorite, R.drawable.ic_adjust};


        for (int i = 0; i < arr.length; i++) {
            MyAdjusts titles = new MyAdjusts(titlesArr[i], imageResources[i]);
            titles.name = titlesArr[i];
            titles.drawableId = imageResources[i];
            arr[i] = titles;
        }
        return arr;
    }

    @Override
    public void onBackPressed() {
        
    }
}