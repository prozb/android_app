package de.thm.ap.ap_przb86_u1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;

import androidx.work.WorkerParameters;

public class UpdateModulesFromFileWorker extends UpdateModulesWorker {
    public UpdateModulesFromFileWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.modules);
        // read json file into modules array
        Module [] modules = new Gson().fromJson(new InputStreamReader(inputStream), Module[].class);
        // cleaning database and inserting new modules
        Log.i("AppDatabase", "adding raw values from file");
        moduleDAO.deleteAll();
        moduleDAO.persistAll(modules);

        return Result.SUCCESS;
    }
}
