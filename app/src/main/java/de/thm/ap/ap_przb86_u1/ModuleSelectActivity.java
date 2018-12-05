package de.thm.ap.ap_przb86_u1;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class ModuleSelectActivity extends AppCompatActivity {

    private ModuleDAO moduleDAO;
    private ArrayList<Module> modules;
    private ArrayAdapter<Module> adapter;
    private ListView modulesView;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_modules);

        modules     = new ArrayList<>();
        adapter     = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, modules);
        modulesView = findViewById(R.id.modules_list);
        modulesView.setAdapter(adapter);
        moduleDAO   = AppDatabase.getDb(getApplicationContext()).moduleDAO();
        moduleDAO.findAll().observe(this, modulesNew -> {
            Log.i("OBSERVER", "modules changed");
            modules.clear();
            modules.addAll(modulesNew);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
