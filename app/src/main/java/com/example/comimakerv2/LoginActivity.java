package com.example.comimakerv2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements View.OnClickListener {
    EditText username;
    Button enter;

    SharedPreferences myPreferences;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NAME = "Nickname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(myPreferences.contains(APP_PREFERENCES_NAME)) {
            startActivity(new Intent(LoginActivity.this, CreatureActivity.class));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.etName);
        enter = (Button) findViewById(R.id.enterButton);
        enter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enterButton:
                String name = username.getText().toString().trim();

                if (name.isEmpty()) {
                    username.setError("Введите имя");
                    return;
                }

                if (name.length() < 3) {
                    username.setError("Имя должно содержать более 3 символов");
                    return;
                }


                myPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = myPreferences.edit();
                editor.putString(APP_PREFERENCES_NAME, name);
                editor.apply();

                Intent i = new Intent(LoginActivity.this, CreatureActivity.class);
                startActivity(i);
        }
    }
}