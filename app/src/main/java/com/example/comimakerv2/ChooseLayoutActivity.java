package com.example.comimakerv2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class ChooseLayoutActivity extends AppCompatActivity implements View.OnClickListener{
    PhotoEditorView photoEditorView1, photoEditorView2, photoEditorView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_layout);

        photoEditorView1 = findViewById(R.id.editImage1);
        photoEditorView2 = findViewById(R.id.editImage2);
        photoEditorView3 = findViewById(R.id.editImage3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editImage1:
                Toast.makeText(this, "В разработке", Toast.LENGTH_SHORT).show();
            case R.id.editImage2:
                Toast.makeText(this, "В разработке", Toast.LENGTH_SHORT).show();
            case R.id.editImage3:
                Toast.makeText(this, "В разработке", Toast.LENGTH_SHORT).show();
        }
    }
}