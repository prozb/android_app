package de.thm.ap.ap_przb86_u1;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UpdateModulesWorker extends Worker {
    private static final String MODULES_URL = "https://homepages.thm.de/~hg10187/modules.json";

    public UpdateModulesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        SharedPreferences sharedPreferences = getApplicationContext()
                                            .getSharedPreferences("modules", Context.MODE_PRIVATE);
        HttpURLConnection connection = null;
        InputStream inputStream      = null;

        try{
            connection = (HttpURLConnection) new URL(MODULES_URL).openConnection();
            // set into header to send json file if modified since
            connection.setIfModifiedSince(sharedPreferences.getLong("lastModified", 0));

            // if modified, read file
            if(connection.getResponseCode() == 200){
                inputStream = connection.getInputStream();
                // read json file into modules array
                Module [] modules = new Gson().fromJson(new InputStreamReader(inputStream), Module[].class);

                //TODO: implement moduleDAO to store modules like:
                // moduleDAO.deleteAll();
                // moduleDAO.persistAll(modules);

                // update last modified after pulling json file
                sharedPreferences.edit()
                        .putLong("lastModified", connection.getLastModified())
                        .apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAILURE;
        }finally {
            IOUtils.closeQuietly(inputStream);
            if(connection != null)
                connection.disconnect();
        }
        return Result.SUCCESS;
    }
}
