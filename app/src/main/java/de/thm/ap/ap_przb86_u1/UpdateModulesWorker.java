package de.thm.ap.ap_przb86_u1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UpdateModulesWorker extends Worker {

    private static final int NOTIFICATION_ID = 42;
    private static final String CHANNEL_ID   = "4711";

    private static final String MODULES_URL = "https://homepages.thm.de/~hg10187/modules.json";
    private ModuleDAO moduleDAO = AppDatabase.getDb(getApplicationContext()).moduleDAO();

    public UpdateModulesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("MODULES", "starting work to update modules");
        SharedPreferences sharedPreferences = getApplicationContext()
                                            .getSharedPreferences("modules", Context.MODE_PRIVATE);
        HttpURLConnection connection = null;
        InputStream inputStream      = null;
        // creating notification
        NotificationManager notificationManager = (NotificationManager)getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel name",
                    NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        notifyBuilder
                .setContentTitle(getApplicationContext().getString(R.string.refresh_modules))
                .setSmallIcon(R.drawable.id_dialog_alert_24dp)
                .setContentText(getApplicationContext().getString(R.string.check_server_data))
                .setProgress(0,0,true);
        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());

        // processing connection
        try{
            connection = (HttpURLConnection) new URL(MODULES_URL).openConnection();
            // set into header to send json file if modified since
            connection.setIfModifiedSince(sharedPreferences.getLong("lastModified", 0));

            // if modified, read file
            if(connection.getResponseCode() == 200){
                Log.i("MODULES", "server data changed, downloading");
                notifyBuilder.setContentText(getApplicationContext().getString(R.string.loading_new_data));
                notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
                inputStream = connection.getInputStream();
                // read json file into modules array
                Module [] modules = new Gson().fromJson(new InputStreamReader(inputStream), Module[].class);
                // cleaning database and inserting new modules
                 moduleDAO.deleteAll();
                 moduleDAO.persistAll(modules);

                // update last modified after pulling json file
                sharedPreferences.edit()
                        .putLong("lastModified", connection.getLastModified())
                        .apply();

                notifyBuilder.setContentText(getApplicationContext().getString(R.string.loaded_success))
                        .setProgress(0,0,false);
                notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
            }else {
                Log.i("MODULES", "server data not changed");
                notificationManager.cancel(NOTIFICATION_ID);
            }
        } catch (Exception e) {
            Log.e("MODULES", "modules update failed");
            notificationManager.cancel(NOTIFICATION_ID);
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
