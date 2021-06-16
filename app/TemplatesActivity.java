import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TemplatesActivity extends AppCompatActivity implements View.OnClickListener{

    BottomNavigationView bottomNavigationView;
    RecyclerView templatesRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.category);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.category:
                        return true;

                    case R.id.work:
                        startActivity(new Intent(getApplicationContext(), CreatureActivity.class));
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        return true;
                }
                return false;
            }
        });

        String[] templatesTitles = {"Люди", "Транспорт", "Мебель", "Фоны", "Предметы", "Животные", "Природа"};
        int[] iconsIDs = {R.drawable.ic_people, R.drawable.ic_car, R.drawable.ic_furniture, R.drawable.ic_background,R.drawable.ic_tools, R.drawable.ic_animal, R.drawable.ic_nature};

        templatesRecycler = (RecyclerView) findViewById(R.id.templatesListView);
        int numberOfColumns = 2;
        templatesRecycler.setHasFixedSize(true);
        templatesRecycler.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this, templatesTitles, iconsIDs);
        templatesRecycler.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
    }
}