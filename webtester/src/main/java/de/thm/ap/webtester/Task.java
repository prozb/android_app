package de.thm.ap.webtester;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class Task extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.i("Task", "Finished");
    }
}
