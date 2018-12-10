package de.thm.ap.ap_przb86_u1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class ModuleSelectActivity extends AppCompatActivity {

    private ModuleDAO moduleDAO;
    private List<Module> modules;
    private ListView modulesView;
    private ArrayAdapter<Module> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MODULES", "modules started");
        setContentView(R.layout.activity_modules);
        Optional.ofNullable(getSupportActionBar()).ifPresent(actionBar -> actionBar.setDisplayHomeAsUpEnabled(true));

        modulesView = findViewById(R.id.modules_list);
        modules     = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, modules);
        moduleDAO   = AppDatabase.getDb(getApplicationContext()).moduleDAO();
        moduleDAO.findAll().observe(this, modulesNew -> {
            Log.i("OBSERVER", "modules changed");
            this.modules = modulesNew;
            this.adapter.clear();
            this.adapter.addAll(Objects.requireNonNull(modulesNew));

            if(modules.isEmpty()){
                // updating modules from file when db is empty
                OneTimeWorkRequest workRequest = new OneTimeWorkRequest
                        .Builder(UpdateModulesFromFileWorker.class)
                        .build();

                WorkManager.getInstance().beginUniqueWork("updating_modules_from_file_one_time",
                        ExistingWorkPolicy.KEEP, workRequest).enqueue();
                Log.i("MODULES", "modules updated");

//                SharedPreferences sharedPreferences = getApplicationContext()
//                        .getSharedPreferences("modules", Context.MODE_PRIVATE);
//                sharedPreferences.edit().putLong("lastModified", 0).apply();
//
//                Log.i("MODULES", "no modules, pulling from the server");
//                Constraints constraints = new Constraints.Builder()
//                        .setRequiredNetworkType(NetworkType.CONNECTED)
//                        .setRequiresBatteryNotLow(true)
//                        .build();
//
//                OneTimeWorkRequest workRequest = new OneTimeWorkRequest
//                        .Builder(UpdateModulesWorker.class)
//                        .setConstraints(constraints)
//                        .build();
//
//                WorkManager.getInstance()
//                        .beginUniqueWork("updating_modules_one_time", ExistingWorkPolicy.KEEP, workRequest).enqueue();
//                Log.i("MODULES", "modules updated");
            }
        });
        modulesView.setEmptyView(findViewById(R.id.modules_list_empty));
        modulesView.setAdapter(adapter);
        modulesView.setOnItemClickListener((parent, view, position, id) -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("id", modules.get((int)id).getNr());
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
